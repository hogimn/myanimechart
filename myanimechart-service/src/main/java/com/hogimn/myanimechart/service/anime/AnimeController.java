package com.hogimn.myanimechart.service.anime;

import com.hogimn.myanimechart.core.common.apicalllog.ApiLoggable;
import com.hogimn.myanimechart.core.common.result.SaveResult;
import com.hogimn.myanimechart.core.domain.anime.AnimeSeason;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@RestController
@RequestMapping("/anime")
@RequiredArgsConstructor
public class AnimeController {
    private final AnimeService animeService;

    @ApiLoggable
    @GetMapping
    public ResponseEntity<List<AnimeResponse>> getByKeyword(
            @RequestParam @NotBlank @Size(max = 256) String keyword) {
        List<AnimeResponse> result = animeService.getByKeyword(keyword.strip());
        return ResponseEntity.ok(result);
    }

    @ApiLoggable
    @GetMapping("/by-year-and-season/{year}/{season}")
    public ResponseEntity<List<AnimeResponse>> getByYearAndSeason(
            @PathVariable @Min(1900) @Max(2100) int year,
            @PathVariable @NotBlank @Size(max = 16) String season) {
        String canonicalSeason = AnimeSeason.parse(season.strip()).apiValue();
        List<AnimeResponse> result = animeService.getByYearAndSeason(year, canonicalSeason);
        return ResponseEntity.ok(result);
    }

    @ApiLoggable
    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid AnimeCreateRequest request) {
        SaveResult result = animeService.save(request);
        if (result == SaveResult.CREATED) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
