package com.hogimn.myanimechart.collector.service;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.dao.AnimeDao;
import com.hogimn.myanimechart.database.dao.AnimeStatDao;
import com.hogimn.myanimechart.database.domain.Anime;
import com.hogimn.myanimechart.database.repository.AnimeRepository;
import com.hogimn.myanimechart.database.repository.AnimeStatRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class AnimeCollectService {
    private final String myanimelistUrl;
    private final RestTemplate restTemplate;
    private final AnimeRepository animeRepository;
    private final AnimeStatRepository animeStatRepository;

    public AnimeCollectService(
            @Value("${myanimelist.url}") String myanimelistUrl,
            RestTemplate restTemplate,
            AnimeRepository animeRepository,
            AnimeStatRepository animeStatRepository) {
        this.myanimelistUrl = myanimelistUrl;
        this.restTemplate = restTemplate;
        this.animeRepository = animeRepository;
        this.animeStatRepository = animeStatRepository;
    }

    public List<Anime> getAnime(int year, String season) {
        String pageSource = getPageSource(year, season, true);
        return parseAndGetAnimeList(year, season, pageSource);
    }

    private List<Anime> parseAndGetAnimeList(int year, String season, String pageSource) {
        Document dom = Jsoup.parse(Objects.requireNonNull(pageSource));
        Elements elements = dom.getElementsByClass("seasonal-anime");
        List<Anime> animeList = new ArrayList<>();
        for (Element doc : elements) {
            Anime anime = getAnime(doc);
            anime.setYear(year);
            anime.setSeason(season);

            if (!anime.getImage().isEmpty()) {
                animeList.add(anime);
            }
        }
        return animeList;
    }

    private String getPageSource(int year, String season, boolean selenium) {
        String url = myanimelistUrl + "/anime/season/" + year + "/" + season;

        if (!selenium) {
            return restTemplate.getForObject(url, String.class);
        }

        ClassPathResource resource = new ClassPathResource("chromedriver-win64/chromedriver.exe");
        File file;
        try {
            file = resource.getFile();
        } catch (IOException e) {
            return null;
        }

        System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
        WebDriver driver = new ChromeDriver();

        driver.get(url);

        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Get initial scroll position and document height
        long lastHeight = (long) js.executeScript("return document.documentElement.scrollHeight");

        // Gradually scroll down the page
        while (true) {
            // Scroll down by 1000px
            js.executeScript("window.scrollBy(0, 1000);");

            // Wait for content to load
            try {
                Thread.sleep(500); // Adjust wait time as needed for content to load
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Get the new scroll position and document height
            long newHeight = (long) js.executeScript("return document.documentElement.scrollHeight");
            long scrollPosition = (long) js.executeScript("return window.scrollY") + driver.manage().window().getSize().getHeight();

            // Check if we've reached the bottom
            if (scrollPosition >= newHeight) {
                break; // We've reached the bottom
            }

            // Update lastHeight for the next iteration
            lastHeight = newHeight;
        }

        String pageSource = driver.getPageSource();
        driver.close();
        driver.quit();

        return pageSource;
    }

    private static Anime getAnime(Element doc) {
        String link = doc.select("h2.h2_anime_title a").attr("href");
        Integer members = Integer.parseInt(doc.select(".js-members").text());
        Double score = Double.parseDouble(doc.select(".js-score").text());
        String title = doc.select(".link-title").text();
        String startDate = doc.select(".js-start_date").text();
        String imageLink = doc.select(".image img").attr("src");
        String synopsis = doc.select(".synopsis p.preline").text();

        Elements genres = doc.select(".js-genre .genre a");
        List<String> genreList = new ArrayList<>();
        for (Element genre : genres) {
            genreList.add(genre.text());
        }
        Elements studios = doc.select(".properties .property .caption:contains(Studio) + .item a");
        List<String> studiosList = new ArrayList<>();
        for (Element studio : studios) {
            studiosList.add(studio.text());
        }

        Elements sources = doc.select(".properties .property .caption:contains(Source) + .item");
        List<String> sourcesList = new ArrayList<>();
        for (Element source : sources) {
            sourcesList.add(source.text());
        }

        Anime anime = new Anime();
        anime.setTitle(title);
        anime.setLink(link);
        anime.setGenre(genreList);
        anime.setImage(imageLink);
        anime.setScore(score);
        anime.setMembers(members);
        anime.setStudios(studiosList);
        anime.setStartDate(startDate);
        anime.setSynopsis(synopsis);
        anime.setSources(sourcesList);

        return anime;
    }

    public void collectAnimeStatistics() {
        List<Anime> animeList = getAnime(DateUtil.currentYear(), DateUtil.currentSeason());
        animeList.forEach(this::saveAnimeStatistics);

        if (DateUtil.changingSeasonMonth()) {
            animeList = getAnime(DateUtil.nextMonthYear(), DateUtil.nextMonthSeason());
            animeList.forEach(this::saveAnimeStatistics);
        }
    }

    public void saveAnimeStatistics(Anime anime) {
        AnimeDao animeDao = upsertAnime(anime);
        saveAnimeStat(animeDao);
    }

    private void saveAnimeStat(AnimeDao anime) {
        if (anime == null) {
            return;
        }

        AnimeStatDao animeStatDao = AnimeStatDao.from(anime);
        animeStatRepository.save(animeStatDao);
    }

    private AnimeDao upsertAnime(Anime anime) {
        if (anime == null) {
            return null;
        }

        AnimeDao animeDao = AnimeDao.from(anime);
        Optional<AnimeDao> optional = animeRepository.findByTitle(anime.getTitle());

        try {
            if (optional.isPresent()) {
                AnimeDao foundAnime = optional.get();
                foundAnime.setFrom(anime);
                animeRepository.save(foundAnime);
                return foundAnime;
            } else {
                animeRepository.save(animeDao);
                return animeDao;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error(anime.toString());
            return null;
        }
    }
}
