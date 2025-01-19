package com.hogimn.myanimechart.collector.service;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.anime.dao.AnimeDao;
import com.hogimn.myanimechart.database.anime.service.AnimeService;
import com.hogimn.myanimechart.database.anime.service.AnimeStatService;
import com.hogimn.myanimechart.database.batch.aop.annotation.SaveBatchHistory;
import dev.katsute.mal4j.MyAnimeList;
import dev.katsute.mal4j.PaginatedIterator;
import dev.katsute.mal4j.anime.Anime;
import dev.katsute.mal4j.anime.property.time.Season;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public void collectAnime(int year, String season) {
        Set<Long> animeIdSet = new HashSet<>();

        try {
            PaginatedIterator<dev.katsute.mal4j.anime.Anime> animePaginatedIterator =
                    myAnimeList.getAnimeSeason(year, getSeason(season)).searchAll();

            while (animePaginatedIterator.hasNext()) {
                try {
                    dev.katsute.mal4j.anime.Anime anime = animePaginatedIterator.next();
                    Thread.sleep(2000);

                    if (!animeIdSet.contains(anime.getID())) {
                        animeIdSet.add(anime.getID());
                        animeService.upsertAnime(anime, year, season);
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
    }

    public Anime getAnime(Long id) {
        try {
            return myAnimeList.getAnime(id);
        } catch (Exception e) {
            log.error("Failed to retrieve anime '{}': {}", id, e.getMessage(), e);
            return null;
        }
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
        List<AnimeDao> animeDaos = animeService.getAiringAnimeExcludingCurrentAndNextSeason(
                DateUtil.getCurrentSeasonYear(), DateUtil.getCurrentSeason(),
                DateUtil.getNextSeasonYear(), DateUtil.getNextSeason());
        for (AnimeDao animeDao : animeDaos) {
            try {
                Anime anime = getAnime(animeDao.getId());
                animeService.upsertAnime(anime, animeDao.getYear(), animeDao.getSeason());
            } catch (Exception e) {
                log.error("Failed to collect anime statistics for anime '{}': {}", animeDao.getId(), e.getMessage(), e);
            }
        }
    }

    private void collectAnimeStatisticsNextSeason() {
        collectAnime(DateUtil.getNextSeasonYear(), DateUtil.getNextSeason());
    }

    private void collectAnimeStatisticsCurrentSeason() {
        collectAnime(DateUtil.getCurrentSeasonYear(), DateUtil.getCurrentSeason());
    }
}
