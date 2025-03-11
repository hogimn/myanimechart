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
    @GetMapping("/findAnimesWithPollsByKeyword")
    public List<AnimeDto> findAnimesWithPollsByKeyword(@RequestParam("keyword") String keyword) {
        return animeService.findAnimeDtosWithPollDtosByKeyword(keyword);
    }

    @ApiLoggable
    @GetMapping("/findAnimesWithPollsByYearAndSeason/{year}/{season}")
    public List<AnimeDto> findAnimesWithPollsByYearAndSeason(@PathVariable int year, @PathVariable String season) {
        return animeService.findAnimeDtosWithPollDtosByYearAndSeason(year, season);
    }
}
