package com.hogimn.myanimechart.execute.anime.collect.anime.service;

import com.hogimn.myanimechart.common.myanimelist.MyAnimeListProvider;
import com.hogimn.myanimechart.common.serviceregistry.RegisteredService;
import com.hogimn.myanimechart.common.serviceregistry.ServiceRegistryService;
import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.anime.dto.AnimeDto;
import com.hogimn.myanimechart.database.anime.dto.AnimeStatDto;
import com.hogimn.myanimechart.database.anime.entity.AnimeEntity;
import com.hogimn.myanimechart.database.anime.service.AnimeService;
import com.hogimn.myanimechart.database.batch.aop.SaveBatchHistory;
import dev.katsute.mal4j.PaginatedIterator;
import dev.katsute.mal4j.anime.Anime;
import dev.katsute.mal4j.anime.property.time.Season;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AnimeCollectService {
    private final AnimeService animeService;
    private final MyAnimeListProvider myAnimeListProvider;
    private final ServiceRegistryService serviceRegistryService;

    public AnimeCollectService(
            AnimeService animeService,
            MyAnimeListProvider myAnimeListProvider,
            ServiceRegistryService serviceRegistryService
    ) {
        this.animeService = animeService;
        this.myAnimeListProvider = myAnimeListProvider;
        this.serviceRegistryService = serviceRegistryService;
    }

    public void collectAnime(int year, String season) {
        try {
            int offset = 0;
            int limit = 500;
            List<Anime> animeList = new ArrayList<>();

            while (true) {
                animeList.addAll(
                        myAnimeListProvider
                                .getMyAnimeList()
                                .getAnimeSeason(year, getSeason(season))
                                .withLimit(limit)
                                .withOffset(offset)
                                .search());

                if (animeList.size() >= limit) {
                    offset += limit;
                } else {
                    break;
                }
            }

            for (Anime anime : animeList) {
                try {
                    if (anime.getStartSeason().getYear() != year ||
                            !Objects.equals(anime.getStartSeason().getSeason().field(), season)) {
                        log.info("Skipping anime '{}': Year {} (expected: {}), Season {} (expected: {})",
                                anime.getTitle(), anime.getStartSeason().getYear(), year,
                                anime.getStartSeason().getSeason().field(), season);
                        continue;
                    }

                    double score = anime.getMeanRating() != null ? anime.getMeanRating().doubleValue() : 0.0;
                    if (score == 0.0) {
                        log.info("Skipping anime '{}': Score {} (expected: > 0.0)", anime.getTitle(), score);
                        continue;
                    }

                    AnimeDto animeDto = AnimeDto.from(anime);
                    AnimeStatDto animeStatDto = AnimeStatDto.from(animeDto);
                    serviceRegistryService.send(RegisteredService.EXECUTE, "/anime/saveAnime", animeDto);
                    serviceRegistryService.send(RegisteredService.EXECUTE, "/animeStat/saveAnimeStat", animeStatDto);
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
            return myAnimeListProvider.getMyAnimeList().getAnime(id);
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

    @SaveBatchHistory("#batchJobName")
    @SchedulerLock(name = "collectAnimeStatistics")
    public void collectAnimeStatistics(String batchJobName) {
        collectAnimeStatisticsCurrentSeason();

        if (DateUtil.changingSeasonMonth()) {
            collectAnimeStatisticsNextSeason();
        }

        collectAnimeStatisticsOldSeasonCurrentlyAiring();

        collectAnimeStatisticsForceCollectTrue();
    }

    private void collectAnimeStatisticsForceCollectTrue() {
        List<AnimeEntity> animeEntities = animeService.getAnimeEntitiesForceCollectTrue();
        for (AnimeEntity animeEntity : animeEntities) {
            try {
                Anime anime = getAnime(animeEntity.getId());
                Thread.sleep(1000);
                AnimeDto animeDto = AnimeDto.from(anime);
                AnimeStatDto animeStatDto = AnimeStatDto.from(animeDto);
                serviceRegistryService.send(RegisteredService.EXECUTE, "/anime/saveAnime", animeDto);
                serviceRegistryService.send(RegisteredService.EXECUTE, "/animeStat/saveAnimeStat", animeStatDto);
            } catch (Exception e) {
                log.error("Failed to collect anime statistics for anime '{}': {}", animeEntity.getId(), e.getMessage(), e);
            }
        }
    }

    private void collectAnimeStatisticsOldSeasonCurrentlyAiring() {
        List<AnimeEntity> animeEntities = animeService.getAnimeEntitiesOldSeasonCurrentlyAiring(
                DateUtil.getCurrentSeasonYear(), DateUtil.getCurrentSeason(),
                DateUtil.getNextSeasonYear(), DateUtil.getNextSeason());
        for (AnimeEntity animeEntity : animeEntities) {
            try {
                Anime anime = getAnime(animeEntity.getId());
                Thread.sleep(1000);
                AnimeDto animeDto = AnimeDto.from(anime);
                AnimeStatDto animeStatDto = AnimeStatDto.from(animeDto);
                serviceRegistryService.send(RegisteredService.EXECUTE, "/anime/saveAnime", animeDto);
                serviceRegistryService.send(RegisteredService.EXECUTE, "/animeStat/saveAnimeStat", animeStatDto);
            } catch (Exception e) {
                log.error("Failed to collect anime statistics for anime '{}': {}", animeEntity.getId(), e.getMessage(), e);
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
