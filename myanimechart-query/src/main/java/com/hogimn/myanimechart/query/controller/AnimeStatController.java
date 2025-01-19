package com.hogimn.myanimechart.query.controller;

import com.hogimn.myanimechart.database.anime.dto.AnimeDto;
import com.hogimn.myanimechart.database.anime.service.AnimeStatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animeStat")
@Slf4j
public class AnimeStatController {
    private final AnimeStatService animeStatService;

    public AnimeStatController(AnimeStatService animeStatService) {
        this.animeStatService = animeStatService;
    }

    @GetMapping("/getAnimeStatsById")
    public AnimeDto getAnimeStatsById(@RequestParam("id") Long id) {
        return animeStatService.getAnimeStatsById(id);
    }

    @GetMapping("/getAnimeStatsByTitle")
    public AnimeDto getAnimeStatsByTitle(@RequestParam("title") String title) {
        return animeStatService.getAnimeStatsByTitle(title);
    }

    @GetMapping("/getAnimeStatsByKeyword")
    public List<AnimeDto> getAnimeStatsByKeyword(@RequestParam("keyword") String keyword) {
        return animeStatService.getAnimeStatsByKeyword(keyword);
    }

    @GetMapping("/{year}/{season}")
    public List<AnimeDto> getAnimeStats(@PathVariable Integer year, @PathVariable String season) {
        return animeStatService.getAnimeStats(year, season);
    }
}
