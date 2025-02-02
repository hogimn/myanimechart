package com.hogimn.myanimechart.execute.controller;

import com.hogimn.myanimechart.database.anime.dto.AnimeDto;
import com.hogimn.myanimechart.database.anime.service.AnimeStatService;
import com.hogimn.myanimechart.database.apicall.aop.ApiLoggable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/animeStat")
@Slf4j
public class AnimeStatController {
    private final AnimeStatService animeStatService;

    public AnimeStatController(AnimeStatService animeStatService) {
        this.animeStatService = animeStatService;
    }

    @GetMapping("/getAnimeStatById")
    public AnimeDto getAnimeStatById(@RequestParam("id") Long id) {
        return animeStatService.getAnimeStatDtoById(id);
    }

    @GetMapping("/getAnimeStatByTitle")
    public AnimeDto getAnimeStatByTitle(@RequestParam("title") String title) {
        return animeStatService.getAnimeStatDtoByTitle(title);
    }

    @GetMapping("/getAnimeStatByKeyword")
    public List<AnimeDto> getAnimeStatByKeyword(@RequestParam("keyword") String keyword) {
        return animeStatService.getAnimeStatDtoByKeyword(keyword);
    }

    @ApiLoggable
    @GetMapping("/{year}/{season}")
    public List<AnimeDto> getAnimeStats(@PathVariable Integer year, @PathVariable String season) {
        return animeStatService.getAnimeStatDtos(year, season);
    }
}
