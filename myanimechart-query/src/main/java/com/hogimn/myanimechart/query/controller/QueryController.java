package com.hogimn.myanimechart.query.controller;

import com.hogimn.myanimechart.database.domain.Anime;
import com.hogimn.myanimechart.query.service.QueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/query")
@Slf4j
public class QueryController {
    private final QueryService queryService;

    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping("/anime-stat/{year}/{season}")
    public List<Anime> getAnimeStats(@PathVariable Integer year, @PathVariable String season) {
        return queryService.getAnimeStats(year, season);
    }

    @GetMapping("/anime-stat")
    public Anime getAnimeStat(@RequestParam("title") String title) {
        return queryService.getAnimeStatsByTitle(title);
    }
}
