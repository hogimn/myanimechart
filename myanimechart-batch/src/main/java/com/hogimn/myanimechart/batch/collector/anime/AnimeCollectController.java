package com.hogimn.myanimechart.batch.collector.anime;

import com.hogimn.myanimechart.common.apicalllog.ApiLoggable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/animeCollect")
@ApiLoggable
@Slf4j
public class AnimeCollectController {
    private final AnimeCollectService animeCollectService;

    public AnimeCollectController(AnimeCollectService animeCollectService) {
        this.animeCollectService = animeCollectService;
    }

    @PostMapping("/seasonal")
    public void seasonal() {
        animeCollectService.collectSeasonalAnime("AnimeCollectJob");
    }

    @PostMapping("/by-year-season")
    public void byYearSeason(
            @RequestParam int year,
            @RequestParam String season
    ) {
        animeCollectService.collectAnimeByYearAndSeason(year, season);
    }

    @PostMapping("/between-years")
    public void betweenYears(
            @RequestParam int fromYear,
            @RequestParam int toYear
    ) {
        animeCollectService.collectAnimeBetweenYears(fromYear, toYear);
    }

    @PostMapping("/by-id")
    public void byId(@RequestParam long animeId) {
        animeCollectService.collectAnimeByAnimeId(animeId);
    }

    @PostMapping("/all")
    public void all() {
        animeCollectService.collectAllAnimes();
    }
}
