package com.hogimn.myanimechart.execute.controller;

import com.hogimn.myanimechart.execute.service.AnimeCollectService;
import com.hogimn.myanimechart.execute.service.PollCollectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/collect")
@Slf4j
public class CollectController {
    private final AnimeCollectService animeCollectService;
    private final PollCollectService pollCollectService;

    public CollectController(
            AnimeCollectService animeCollectService,
            PollCollectService pollCollectService
    ) {
        this.animeCollectService = animeCollectService;
        this.pollCollectService = pollCollectService;
    }

    @PostMapping("/collectAnimeStatistics")
    public void collectAnimeStatistics(@RequestBody String batchJobName) {
        animeCollectService.collectAnimeStatistics(batchJobName);
    }

    @PostMapping("/collectPollStatistics")
    public void collectPollStatistics(@RequestBody String batchJobName) {
        pollCollectService.collectPollStatistics(batchJobName);
    }
}
