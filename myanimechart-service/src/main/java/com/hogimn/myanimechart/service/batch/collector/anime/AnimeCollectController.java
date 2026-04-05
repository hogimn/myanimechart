package com.hogimn.myanimechart.service.batch.collector.anime;

import com.hogimn.myanimechart.core.common.apicalllog.ApiLoggable;
import com.hogimn.myanimechart.core.domain.anime.AnimeSeason;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/collect-anime")
@ApiLoggable
@RequiredArgsConstructor
public class AnimeCollectController {
    private final AnimeCollectService animeCollectService;

    @PostMapping
    public ResponseEntity<Void> collectByAnimeId(@RequestParam @Positive long animeId) {
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
            @RequestParam @Min(1900) @Max(2100) int year,
            @RequestParam String season
    ) {
        String canonicalSeason = AnimeSeason.parse(season.strip()).apiValue();
        animeCollectService.collectByYearAndSeason(year, canonicalSeason);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/between-years")
    public ResponseEntity<Void> collectBetweenYears(
            @RequestParam @Min(1900) @Max(2100) int fromYear,
            @RequestParam @Min(1900) @Max(2100) int toYear
    ) {
        animeCollectService.collectBetweenYears(fromYear, toYear);
        return ResponseEntity.noContent().build();
    }
}
