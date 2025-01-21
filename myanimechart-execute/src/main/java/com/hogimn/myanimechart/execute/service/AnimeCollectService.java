package com.hogimn.myanimechart.execute.service;

import com.hogimn.myanimechart.common.serviceregistry.domain.RegisteredService;
import com.hogimn.myanimechart.common.serviceregistry.service.ServiceRegistryService;
import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.anime.dao.AnimeDao;
import com.hogimn.myanimechart.database.anime.dto.AnimeDto;
import com.hogimn.myanimechart.database.anime.service.AnimeService;
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
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
public class AnimeCollectService {
    private final AnimeService animeService;
    private final MyAnimeList myAnimeList;
    private final ServiceRegistryService serviceRegistryService;

    public AnimeCollectService(
            AnimeService animeService,
            MyAnimeList myAnimeList,
            ServiceRegistryService serviceRegistryService
    ) {
        this.animeService = animeService;
        this.myAnimeList = myAnimeList;
        this.serviceRegistryService = serviceRegistryService;
    }

    public void collectAnime(int year, String season) {
        Set<Long> animeIdSet = new HashSet<>();

        try {
            PaginatedIterator<Anime> animePaginatedIterator =
                    myAnimeList.getAnimeSeason(year, getSeason(season)).searchAll();

            while (animePaginatedIterator.hasNext()) {
                try {
                    Anime anime = animePaginatedIterator.next();
                    Thread.sleep(2000);

                    if (anime.getStartSeason().getYear() != year ||
                            !Objects.equals(anime.getStartSeason().getSeason().field(), season)) {
                        log.info("Skipping anime '{}': Year {} (expected: {}), Season {} (expected: {})",
                                anime.getTitle(), anime.getStartSeason().getYear(), year,
                                anime.getStartSeason().getSeason().field(), season);
                        continue;
                    }

                    double score = anime.getMeanRating() != null ? anime.getMeanRating().doubleValue() : 0.0;
                    if (anime.getMeanRating() == 0.0) {
                        log.info("Skipping anime '{}': Score {} (expected: > 0.0)", anime.getTitle(), score);
                        continue;
                    }

                    if (!animeIdSet.contains(anime.getID())) {
                        animeIdSet.add(anime.getID());
                        AnimeDto animeDto = AnimeDto.from(anime);
                        serviceRegistryService.send(RegisteredService.EXECUTE, "/anime/saveAnime", animeDto);
                        serviceRegistryService.send(RegisteredService.EXECUTE, "/animeStat/saveAnimeStat", animeDto);
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
                AnimeDto animeDto = AnimeDto.from(anime);
                serviceRegistryService.send(RegisteredService.EXECUTE, "/anime/saveAnime", animeDto);
                serviceRegistryService.send(RegisteredService.EXECUTE, "/animeStat/saveAnimeStat", animeDto);
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
