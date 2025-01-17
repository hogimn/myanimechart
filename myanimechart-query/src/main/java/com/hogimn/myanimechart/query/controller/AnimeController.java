package com.hogimn.myanimechart.query.controller;

import com.hogimn.myanimechart.database.anime.domain.Anime;
import com.hogimn.myanimechart.database.anime.service.AnimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/anime")
@Slf4j
public class AnimeController {
    private final AnimeService animeService;

    public AnimeController(AnimeService animeService) {
        this.animeService = animeService;
    }

    @GetMapping("/getAnimeByTitle")
    public Anime getAnimeByTitle(@RequestParam("title") String title) {
        return animeService.getAnimeByTitle(title);
    }

    @GetMapping("/getAnimeByKeyword")
    public List<Anime> getAnimeByKeyword(@RequestParam("keyword") String keyword) {
        return animeService.getAnimeByKeyword(keyword);
    }

    @GetMapping("/getAnimeById")
    public Anime getAnimeById(@RequestParam("id") Long id) {
        return animeService.getAnimeById(id);
    }

    @GetMapping("/{year}/{season}")
    public List<Anime> getAnimeByYearAndSeason(@PathVariable Integer year, @PathVariable String season) {
        return animeService.getAnimeByYearAndSeason(year, season);
    }
}
