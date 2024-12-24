package com.hogimn.myanimechart.query.controller;

import com.hogimn.myanimechart.database.domain.Anime;
import com.hogimn.myanimechart.database.service.AnimeStatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/query/anime-stat")
@Slf4j
public class AnimeStatController {
    private final AnimeStatService animeStatService;

    public AnimeStatController(AnimeStatService animeStatService) {
        this.animeStatService = animeStatService;
    }

    @GetMapping("/")
    public Anime getAnimeStat(@RequestParam("title") String title) {
        return animeStatService.getAnimeStatsByTitle(title);
    }

    @GetMapping("/{year}/{season}")
    public List<Anime> getAnimeStats(@PathVariable Integer year, @PathVariable String season) {
        return animeStatService.getAnimeStats(year, season);
    }
}
