package com.hogimn.myanimechart.collector.anime;

import com.hogimn.myanimechart.common.anime.AnimeDto;
import com.hogimn.myanimechart.common.anime.AnimeEntity;
import com.hogimn.myanimechart.common.anime.AnimeService;
import com.hogimn.myanimechart.common.batch.SaveBatchHistory;
import com.hogimn.myanimechart.common.myanimelist.MyAnimeListProvider;
import com.hogimn.myanimechart.common.serviceregistry.RegisteredService;
import com.hogimn.myanimechart.common.serviceregistry.ServiceRegistryService;
import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.common.util.SleepUtil;
import dev.katsute.mal4j.anime.Anime;
import dev.katsute.mal4j.anime.property.time.Season;
import lombok.Synchronized;
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

    public void collectAnimeByYearAndSeason(int year, String season) {
        collectAnime(year, season);
    }

    @SaveBatchHistory("#batchJobName")
    @SchedulerLock(name = "collectSeasonalAnime")
    public void collectSeasonalAnime(String batchJobName) {
        log.info("Start of collecting seasonal anime");

        collectAnimeCurrentSeason();
        if (DateUtil.changingSeasonMonth()) {
            collectAnimeNextSeason();
        }
        collectAnimeOldSeasonCurrentlyAiring();
        collectAnimeForceCollectTrue();

        log.info("End of collecting seasonal anime");
    }

    private void collectAnimeCurrentSeason() {
        collectAnime(DateUtil.getCurrentSeasonYear(), DateUtil.getCurrentSeason());
    }

    private void collectAnimeNextSeason() {
        collectAnime(DateUtil.getNextSeasonYear(), DateUtil.getNextSeason());
    }

    private void collectAnimeOldSeasonCurrentlyAiring() {
        List<AnimeEntity> animeEntities = animeService.findAnimeEntitiesOldSeasonCurrentlyAiring(
                DateUtil.getCurrentSeasonYear(), DateUtil.getCurrentSeason(),
                DateUtil.getNextSeasonYear(), DateUtil.getNextSeason());
        for (AnimeEntity animeEntity : animeEntities) {
            collectAnimeByAnimeId(animeEntity.getId());
            SleepUtil.sleep(30 * 1000);
        }
    }

    private void collectAnimeForceCollectTrue() {
        List<AnimeEntity> animeEntities = animeService.findAnimeEntitiesForceCollectTrue();
        for (var animeEntity : animeEntities) {
            collectAnimeByAnimeId(animeEntity.getId());
            SleepUtil.sleep(30 * 1000);
        }
    }

    public void collectAnimeByAnimeId(long animeId) {
        log.info("Start of collecting anime {}", animeId);

        try {
            Anime anime = getAnime(animeId);
            AnimeDto animeDto = AnimeDto.from(anime);
            serviceRegistryService.send(RegisteredService.EXECUTE, "/anime/saveAnime", animeDto);
        } catch (Exception e) {
            log.error("Failed to collect anime '{}': {}", animeId, e.getMessage(), e);
        }

        log.info("End of collecting anime {}", animeId);
    }

    public void collectAnime(int year, String season) {
        log.info("Start of collecting anime for season '{} {}'", season, year);

        try {
            List<Anime> animeList = fetchSeasonalAnime(year, season);
            for (Anime anime : animeList) {
                try {
                    if (anime.getStartSeason() == null ||
                            anime.getStartSeason().getYear() != year ||
                            !Objects.equals(anime.getStartSeason().getSeason().field(), season)) {
                        log.info("Skipping anime '{}': Year {} (expected: {}), Season {} (expected: {})",
                                anime.getTitle(), anime.getStartSeason().getYear(), year,
                                anime.getStartSeason().getSeason().field(), season);
                        continue;
                    }

                    AnimeDto animeDto = AnimeDto.from(anime);

                    if (animeDto.getScore() == 0.0) {
                        log.info("Skipping anime '{}': Score {} (expected: >= 0)",
                                animeDto.getTitle(), animeDto.getScore());
                        continue;
                    }
                    
                    serviceRegistryService.send(RegisteredService.EXECUTE, "/anime/saveAnime", animeDto);
                } catch (Exception e) {
                    log.error("Error processing anime. Skipping to the next item. Details: {}", e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            log.error("Failed to retrieve anime for season '{} {}': {}", season, year, e.getMessage(), e);
        }

        log.info("End of collecting anime for season '{} {}'", season, year);
    }

    private List<Anime> fetchSeasonalAnime(int year, String season) {
        int offset = 0;
        int limit = 500;
        List<Anime> animeList = new ArrayList<>();

        while (true) {
            List<Anime> tempAnimeList = myAnimeListProvider
                    .getMyAnimeList()
                    .getAnimeSeason(year, getSeason(season))
                    .withLimit(limit)
                    .withOffset(offset)
                    .search();

            animeList.addAll(tempAnimeList);

            log.info("offset: {}, limit: {}, size of list: {}", offset, limit, animeList.size());

            if (tempAnimeList.size() >= limit) {
                offset += limit;
            } else {
                break;
            }

            SleepUtil.sleep(30 * 1000);
        }
        return animeList;
    }

    public Anime getAnime(long id) {
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
}
