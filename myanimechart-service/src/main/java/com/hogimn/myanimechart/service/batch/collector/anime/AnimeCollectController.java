package com.hogimn.myanimechart.service.batch.collector.anime;

import com.hogimn.myanimechart.core.common.apicalllog.ApiLoggable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/collect-anime")
@ApiLoggable
@Slf4j
@RequiredArgsConstructor
public class AnimeCollectController {
    private final AnimeCollectService animeCollectService;

    @PostMapping
    public ResponseEntity<Void> collectByAnimeId(@RequestParam long animeId) {
        animeCollectService.collectByAnimeId(animeId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/all")
    public ResponseEntity<Void> all() {
        animeCollectService.collectAll();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/seasonal")
    public ResponseEntity<Void> collectSeasonal() {
        animeCollectService.collectSeasonal("AnimeCollectJob");
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/by-year-and-season")
    public ResponseEntity<Void> collectByYearAndSeason(
            @RequestParam int year,
            @RequestParam String season
    ) {
        animeCollectService.collectByYearAndSeason(year, season);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/between-years")
    public ResponseEntity<Void> collectBetweenYears(
            @RequestParam int fromYear,
            @RequestParam int toYear
    ) {
        animeCollectService.collectBetweenYears(fromYear, toYear);
        return ResponseEntity.noContent().build();
    }
}
