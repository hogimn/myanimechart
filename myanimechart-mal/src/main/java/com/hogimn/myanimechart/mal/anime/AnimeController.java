package com.hogimn.myanimechart.mal.anime;

import com.hogimn.myanimechart.core.common.apicalllog.ApiLoggable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @ApiLoggable
    @GetMapping
    public List<AnimeDto> getByKeyword(@RequestParam("keyword") String keyword) {
        return animeService.getByKeyword(keyword);
    }

    @ApiLoggable
    @GetMapping("/by-year-and-season/{year}/{season}")
    public List<AnimeDto> getByYearAndSeason(@PathVariable int year, @PathVariable String season) {
        return animeService.getByYearAndSeason(year, season);
    }

    @ApiLoggable
    @PostMapping("/save")
    public void save(@RequestBody AnimeDto animeDto) {
        animeService.save(animeDto);
    }
}
