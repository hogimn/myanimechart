package com.hogimn.myanimechart.service.anime;

import com.hogimn.myanimechart.core.common.apicalllog.ApiLoggable;
import com.hogimn.myanimechart.core.common.result.SaveResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequiredArgsConstructor
public class AnimeController {
    private final AnimeService animeService;

    @ApiLoggable
    @GetMapping
    public ResponseEntity<List<AnimeResponse>> getByKeyword(@RequestParam String keyword) {
        List<AnimeResponse> result = animeService.getByKeyword(keyword);
        return ResponseEntity.ok(result);
    }

    @ApiLoggable
    @GetMapping("/by-year-and-season/{year}/{season}")
    public ResponseEntity<List<AnimeResponse>> getByYearAndSeason(@PathVariable int year, @PathVariable String season) {
        List<AnimeResponse> result = animeService.getByYearAndSeason(year, season);
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
