package com.hogimn.myanimechart.execute.service;

import com.hogimn.myanimechart.common.serviceregistry.domain.RegisteredService;
import com.hogimn.myanimechart.common.serviceregistry.service.ServiceRegistryService;
import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.anime.entity.AnimeEntity;
import com.hogimn.myanimechart.database.anime.dto.AnimeDto;
import com.hogimn.myanimechart.database.anime.dto.AnimeStatDto;
import com.hogimn.myanimechart.database.anime.service.AnimeService;
import com.hogimn.myanimechart.database.batch.aop.annotation.SaveBatchHistory;
import dev.katsute.mal4j.MyAnimeList;
import dev.katsute.mal4j.PaginatedIterator;
import dev.katsute.mal4j.anime.Anime;
import dev.katsute.mal4j.anime.property.time.Season;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
        try {
            PaginatedIterator<Anime> animePaginatedIterator =
                    myAnimeList.getAnimeSeason(year, getSeason(season)).searchAll();

            while (animePaginatedIterator.hasNext()) {
                try {
                    Anime anime = animePaginatedIterator.next();
                    Thread.sleep(1000);

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
        List<AnimeEntity> animeEntities = animeService.getAiringAnimeEntitiesExcludingCurrentAndNextSeason(
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
