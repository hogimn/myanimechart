package com.hogimn.myanimechart.collector.poll;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pollCollect")
@Slf4j
public class PollCollectController {
    private final PollCollectService pollCollectService;

    public PollCollectController(PollCollectService pollCollectService) {
        this.pollCollectService = pollCollectService;
    }

    @PostMapping("/collectSeasonalPoll")
    public void collectSeasonalPoll() {
        pollCollectService.collectSeasonalPoll(this.getClass().getSimpleName());
    }

    @PostMapping("/collectPollByYearAndSeason")
    public void collectPollByYearAndSeason(
            @RequestParam("year") int year, @RequestParam("season") String season
    ) {
        pollCollectService.collectPollByYearAndSeason(year, season);
    }

    @PostMapping("/collectPollByAnimeId")
    public void collectSeasonalPoll(@RequestParam("animeId") Long animeId) {
        pollCollectService.collectPollByAnimeId(animeId);
    }
}
