package com.hogimn.myanimechart.batch.collector.anime;

import com.hogimn.myanimechart.core.common.apicalllog.ApiLoggable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/collect-anime")
@ApiLoggable
@Slf4j
public class AnimeCollectController {
    private final AnimeCollectService animeCollectService;

    public AnimeCollectController(AnimeCollectService animeCollectService) {
        this.animeCollectService = animeCollectService;
    }

    @PostMapping
    public void collectByAnimeId(@RequestParam long animeId) {
        animeCollectService.collectByAnimeId(animeId);
    }

    @PostMapping("/all")
    public void all() {
        animeCollectService.collectAll();
    }

    @PostMapping("/seasonal")
    public void collectSeasonal() {
        animeCollectService.collectSeasonal("AnimeCollectJob");
    }

    @PostMapping("/by-year-and-season")
    public void collectByYearAndSeason(
            @RequestParam int year,
            @RequestParam String season
    ) {
        animeCollectService.collectByYearAndSeason(year, season);
    }

    @PostMapping("/between-years")
    public void collectBetweenYears(
            @RequestParam int fromYear,
            @RequestParam int toYear
    ) {
        animeCollectService.collectBetweenYears(fromYear, toYear);
    }
}
