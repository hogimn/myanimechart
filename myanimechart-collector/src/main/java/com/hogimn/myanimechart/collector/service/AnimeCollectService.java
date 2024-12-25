package com.hogimn.myanimechart.collector.service;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.batch.aop.annotation.SaveBatchHistory;
import com.hogimn.myanimechart.database.anime.dao.AnimeDao;
import com.hogimn.myanimechart.database.anime.domain.Anime;
import com.hogimn.myanimechart.database.anime.service.AnimeService;
import com.hogimn.myanimechart.database.anime.service.AnimeStatService;
import dev.katsute.mal4j.MyAnimeList;
import dev.katsute.mal4j.PaginatedIterator;
import dev.katsute.mal4j.anime.property.time.Season;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AnimeCollectService {
    private final AnimeService animeService;
    private final AnimeStatService animeStatService;
    private final MyAnimeList myAnimeList;

    public AnimeCollectService(
            AnimeService animeService,
            AnimeStatService animeStatService,
            MyAnimeList myAnimeList) {
        this.animeService = animeService;
        this.animeStatService = animeStatService;
        this.myAnimeList = myAnimeList;
    }

    public List<Anime> getAnime(int year, String season) {
        PaginatedIterator<dev.katsute.mal4j.anime.Anime> animePaginatedIterator =
                myAnimeList.getAnimeSeason(year, getSeason(season)).searchAll();

        List<Anime> animeList = new ArrayList<>();
        while (animePaginatedIterator.hasNext()) {
            try {
                dev.katsute.mal4j.anime.Anime katsuteAnime = animePaginatedIterator.next();
                Anime anime = Anime.from(katsuteAnime);
                animeList.add(anime);
                log.info("{}", anime);
                break;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        return animeList;
    }

    private Season getSeason(String season) {
        return switch (season) {
            case "spring" -> Season.Spring;
            case "summer" -> Season.Summer;
            case "fall" -> Season.Fall;
            case "winter" -> Season.Winter;
            default -> null;
        };
    }

    @Transactional
    @SaveBatchHistory("#batchJobName")
    @SchedulerLock(name = "collectAnimeStatistics")
    public void collectAnimeStatistics(String batchJobName) {
        List<Anime> animeList = getAnime(DateUtil.currentYear(), DateUtil.currentSeason());
        animeList.forEach(this::saveAnimeStatistics);

        if (DateUtil.changingSeasonMonth()) {
            animeList = getAnime(DateUtil.nextMonthYear(), DateUtil.nextMonthSeason());
            animeList.forEach(this::saveAnimeStatistics);
        }

        animeList = animeService.getAiringAnimeExcludingCurrentAndNextSeason(
                DateUtil.currentYear(), DateUtil.currentSeason());
        animeList.forEach(this::saveAnimeStatistics);
    }

    public void saveAnimeStatistics(Anime anime) {
        AnimeDao animeDao = animeService.upsertAnime(anime);
        animeStatService.saveAnimeStat(animeDao);
    }
}
