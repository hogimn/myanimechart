package com.hogimn.myanimechart.execute.collect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/animeCollect")
@Slf4j
public class AnimeCollectController {
    private final AnimeCollectService animeCollectService;

    public AnimeCollectController(AnimeCollectService animeCollectService) {
        this.animeCollectService = animeCollectService;
    }

    @PostMapping("/collectAnimeStatistics")
    public void collectAnimeStatistics(@RequestBody String batchJobName) {
        animeCollectService.collectAnimeStatistics(batchJobName);
    }
}
