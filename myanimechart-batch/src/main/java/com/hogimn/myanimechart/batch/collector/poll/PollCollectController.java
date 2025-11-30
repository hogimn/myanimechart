package com.hogimn.myanimechart.batch.collector.poll;

import com.hogimn.myanimechart.batch.collector.poll.status.PollCollectionStatusDto;
import com.hogimn.myanimechart.batch.collector.poll.status.BatchPollCollectionStatusService;
import com.hogimn.myanimechart.core.common.apicalllog.ApiLoggable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ApiLoggable
@RestController
@RequestMapping("/collect-poll")
public class PollCollectController {
    private final PollCollectService pollCollectService;
    private final BatchPollCollectionStatusService batchPollCollectionStatusService;

    public PollCollectController(
            PollCollectService pollCollectService,
            BatchPollCollectionStatusService batchPollCollectionStatusService
    ) {
        this.pollCollectService = pollCollectService;
        this.batchPollCollectionStatusService = batchPollCollectionStatusService;
    }

    @PostMapping("seasonal")
    public void collectSeasonal() {
        pollCollectService.collectSeasonal("PollCollectJob");
    }

    @PostMapping("by-year-and-season")
    public void collectByYearAndSeason(@RequestParam int year, @RequestParam String season) {
        pollCollectService.collectPollByYearAndSeason(year, season);
    }

    @PostMapping
    public void collectByAnimeId(@RequestParam long animeId) {
        pollCollectService.collectByAnimeId(animeId);
    }

    @PostMapping("/by-episode")
    public void collectByEpisode(
            @RequestParam long animeId,
            @RequestParam long topicId,
            @RequestParam int episode) {
        pollCollectService.collectByEpisode(animeId, topicId, episode);
    }

    @PostMapping("/all")
    public void collectAll() {
        pollCollectService.collectAll();
    }

    @PostMapping("/empty")
    public void collectEmpty() {
        pollCollectService.collectEmpty();
    }

    @PostMapping("/resume/by-year-and-season")
    public void resumeByYearAndSeason(
            @RequestParam int year,
            @RequestParam String season
    ) {
        pollCollectService.resumeByYearAndSeason(year, season);
    }

    @PostMapping("/resume/failed")
    public void resumeFailedCollection() {
        pollCollectService.resumeFailed();
    }

    @GetMapping("/statuses")
    public List<PollCollectionStatusDto> getStatuses() {
        return batchPollCollectionStatusService.getStatuses();
    }
}
