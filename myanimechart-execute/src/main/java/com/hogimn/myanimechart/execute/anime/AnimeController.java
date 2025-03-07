package com.hogimn.myanimechart.execute.anime;

import com.hogimn.myanimechart.common.anime.AnimeDto;
import com.hogimn.myanimechart.common.anime.AnimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/anime")
@Slf4j
public class AnimeController {
    private final AnimeService animeService;

    public AnimeController(AnimeService animeService) {
        this.animeService = animeService;
    }

    @PostMapping("/saveAnime")
    public void saveAnime(@RequestBody AnimeDto animeDto) {
        animeService.upsertAnime(animeDto);
    }
}
