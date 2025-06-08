package com.hogimn.myanimechart.collector.anime;

import com.hogimn.myanimechart.common.apicalllog.ApiLoggable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/animeCollect")
@Slf4j
public class AnimeCollectController {
    private final AnimeCollectService animeCollectService;

    public AnimeCollectController(AnimeCollectService animeCollectService) {
        this.animeCollectService = animeCollectService;
    }

    @ApiLoggable
    @PostMapping("/collectSeasonalAnime")
    public void collectSeasonalAnime() {
        animeCollectService.collectSeasonalAnime("AnimeCollectJob");
    }

    @ApiLoggable
    @PostMapping("/collectAnimeByYearAndSeason")
    public void collectAnimeByYearAndSeason(
            @RequestParam("year") int year, @RequestParam("season") String season
    ) {
        animeCollectService.collectAnimeByYearAndSeason(year, season);
    }

    @ApiLoggable
    @PostMapping("/collectAnimeBetweenYears")
    public void collectAnimeBetweenYears(
            @RequestParam("fromYear") int fromYear, @RequestParam("toYear") int toYear
    ) {
        animeCollectService.collectAnimeBetweenYears(fromYear, toYear);
    }

    @ApiLoggable
    @PostMapping("/collectAnimeByAnimeId")
    public void collectAnimeByAnimeId(@RequestParam("animeId") long animeId) {
        animeCollectService.collectAnimeByAnimeId(animeId);
    }

    @ApiLoggable
    @PostMapping("/collectAllAnimes")
    public void collectAllAnimes() {
        animeCollectService.collectAllAnimes();
    }
}
