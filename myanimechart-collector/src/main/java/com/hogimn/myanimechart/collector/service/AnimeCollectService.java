package com.hogimn.myanimechart.collector.service;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.anime.dao.AnimeDao;
import com.hogimn.myanimechart.database.anime.domain.Anime;
import com.hogimn.myanimechart.database.anime.service.AnimeService;
import com.hogimn.myanimechart.database.anime.service.AnimeStatService;
import com.hogimn.myanimechart.database.batch.aop.annotation.SaveBatchHistory;
import dev.katsute.mal4j.MyAnimeList;
import dev.katsute.mal4j.PaginatedIterator;
import dev.katsute.mal4j.anime.property.time.Season;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.stereotype.Service;

import java.util.*;

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
        List<Anime> animeList = new ArrayList<>();
        Set<Long> animeIdSet = new HashSet<>();

        try {
            PaginatedIterator<dev.katsute.mal4j.anime.Anime> animePaginatedIterator =
                    myAnimeList.getAnimeSeason(year, getSeason(season)).searchAll();

            while (animePaginatedIterator.hasNext()) {
                try {
                    dev.katsute.mal4j.anime.Anime katsuteAnime = animePaginatedIterator.next();
                    Anime anime = Anime.from(katsuteAnime);

                    if (anime.getYear() != year || !Objects.equals(anime.getSeason(), season)) {
                        log.info("Skipping anime '{}': Year {} (expected: {}), Season {} (expected: {})",
                                anime.getTitle(), anime.getYear(), year, anime.getSeason(), season);
                        continue;
                    }

                    if (anime.getScore() == 0.0) {
                        log.info("Skipping anime '{}': Score {} (expected: > 0.0)",
                                anime.getTitle(), anime.getScore());
                        continue;
                    }

                    if (!animeIdSet.contains(anime.getId())) {
                        animeList.add(anime);
                        animeIdSet.add(anime.getId());
                        log.info("Anime added: {}", anime);
                    } else {
                        log.warn("Anime Id Duplicate: {}", anime);
                    }
                } catch (Exception e) {
                    log.error("Error processing anime. Skipping to the next item. Details: {}", e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            log.error("Failed to retrieve anime for season '{} {}': {}", season, year, e.getMessage(), e);
        }

        return animeList;
    }

    public Anime getAnime(Long id) {
        dev.katsute.mal4j.anime.Anime katsuteAnime = myAnimeList.getAnime(id);
        return Anime.from(katsuteAnime);
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
        collectAnimeStatisticsCurrentSeason();

        if (DateUtil.changingSeasonMonth()) {
            collectAnimeStatisticsNextSeason();
        }

        collectAnimeStatisticsOldSeasonCurrentlyAiring();
    }

    private void collectAnimeStatisticsOldSeasonCurrentlyAiring() {
        List<Anime> animeList = animeService.getAiringAnimeExcludingCurrentAndNextSeason(
                DateUtil.getCurrentSeasonYear(), DateUtil.getCurrentSeason(),
                DateUtil.getNextSeasonYear(), DateUtil.getNextSeason());
        animeList = animeList.stream()
                .map((anime) -> getAnime(anime.getId()))
                .toList();
        SaveAnimeAndAnimeStat(animeList);
    }

    private void collectAnimeStatisticsNextSeason() {
        List<Anime> animeList = getAnime(DateUtil.getNextSeasonYear(), DateUtil.getNextSeason());
        SaveAnimeAndAnimeStat(animeList);
    }

    private void collectAnimeStatisticsCurrentSeason() {
        List<Anime> animeList = getAnime(DateUtil.getCurrentSeasonYear(), DateUtil.getCurrentSeason());
        SaveAnimeAndAnimeStat(animeList);
    }

    private void SaveAnimeAndAnimeStat(List<Anime> animeList) {
        for (Anime anime : animeList) {
            try {
                AnimeDao animeDao = animeService.upsertAnime(anime);
                animeStatService.saveAnimeStat(animeDao);
            } catch (Exception e) {
                log.error("Error processing anime DML '{}'. Details: {}", anime.getTitle(), e.getMessage(), e);
            }
        }
    }
}
