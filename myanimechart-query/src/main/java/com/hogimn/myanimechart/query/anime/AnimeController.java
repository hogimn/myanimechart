package com.hogimn.myanimechart.query.anime;

import com.hogimn.myanimechart.common.anime.AnimeDto;
import com.hogimn.myanimechart.common.anime.AnimeService;
import com.hogimn.myanimechart.common.apicalllog.ApiLoggable;
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

    @ApiLoggable
    @GetMapping("/findAnimeWithPollByKeyword")
    public List<AnimeDto> findAnimeWithPollByKeyword(@RequestParam("keyword") String keyword) {
        return animeService.findAnimeDtosWithPollByKeyword(keyword);
    }

    @ApiLoggable
    @GetMapping("/findAnimeWithPoll/{year}/{season}")
    public List<AnimeDto> findAnimeWithPollByYearAndSeason(@PathVariable int year, @PathVariable String season) {
        return animeService.findAnimeDtosWithPollByYearAndSeason(year, season);
    }
}
