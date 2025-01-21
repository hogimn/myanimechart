package com.hogimn.myanimechart.execute.controller;

import com.hogimn.myanimechart.database.anime.dto.AnimeDto;
import com.hogimn.myanimechart.database.anime.service.AnimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public AnimeDto getAnimeByTitle(@RequestParam("title") String title) {
        return animeService.getAnimeDtoByTitle(title);
    }

    @GetMapping("/getAnimeByKeyword")
    public List<AnimeDto> getAnimeByKeyword(@RequestParam("keyword") String keyword) {
        return animeService.getAnimeDtosByKeyword(keyword);
    }

    @GetMapping("/getAnimeById")
    public AnimeDto getAnimeById(@RequestParam("id") Long id) {
        return animeService.getAnimeDtoById(id);
    }

    @GetMapping("/{year}/{season}")
    public List<AnimeDto> getAnimeByYearAndSeason(@PathVariable Integer year, @PathVariable String season) {
        return animeService.getAnimeDtosByYearAndSeason(year, season);
    }
}
