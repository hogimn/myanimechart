package com.hogimn.myanimechart.query.anime;

import com.hogimn.myanimechart.database.anime.AnimeDto;
import com.hogimn.myanimechart.database.anime.AnimeService;
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

    @GetMapping("/getAnimeWithPollByKeyword")
    public List<AnimeDto> getAnimeWithPollByKeyword(@RequestParam("keyword") String keyword) {
        return animeService.getAnimeDtosWithPollByKeyword(keyword);
    }

    @GetMapping("/getAnimeWithPoll/{year}/{season}")
    public List<AnimeDto> getAnimeWithPollByYearAndSeason(@PathVariable int year, @PathVariable String season) {
        return animeService.getAnimeDtosWithPollByYearAndSeason(year, season);
    }
}
