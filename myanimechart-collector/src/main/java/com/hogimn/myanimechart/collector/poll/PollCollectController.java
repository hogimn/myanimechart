package com.hogimn.myanimechart.collector.poll;

import com.hogimn.myanimechart.common.apicalllog.ApiLoggable;
import com.hogimn.myanimechart.common.poll.PollCollectionStatusDto;
import com.hogimn.myanimechart.common.poll.PollCollectionStatusService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ApiLoggable
@RestController
@RequestMapping("/pollCollect")
public class PollCollectController {
    private final PollCollectService pollCollectService;
    private final PollCollectionStatusService pollCollectionStatusService;

    public PollCollectController(
            PollCollectService pollCollectService,
            PollCollectionStatusService pollCollectionStatusService
    ) {
        this.pollCollectService = pollCollectService;
        this.pollCollectionStatusService = pollCollectionStatusService;
    }

    @PostMapping("/collect/seasonal")
    public void collectSeasonalPoll() {
        pollCollectService.collectSeasonalPoll("PollCollectJob");
    }

    @PostMapping("/collect/byYearAndSeason")
    public void collectByYearAndSeason(@RequestParam int year, @RequestParam String season) {
        pollCollectService.collectPollByYearAndSeason(year, season);
    }

    @PostMapping("/collect/byAnimeId")
    public void collectByAnimeId(@RequestParam long animeId) {
        pollCollectService.collectPollByAnimeId(animeId);
    }

    @PostMapping("/collect/byEpisode")
    public void collectByEpisode(
            @RequestParam long animeId,
            @RequestParam long topicId,
            @RequestParam int episode) {
        pollCollectService.collectPollByAnimeIdAndTopicId(animeId, topicId, episode);
    }

    @PostMapping("/collect/all")
    public void collectAllPolls() {
        pollCollectService.collectAllPolls();
    }

    @PostMapping("/collect/empty")
    public void collectEmptyPoll() {
        pollCollectService.collectEmptyPoll();
    }

    @PostMapping("/resume/byYearAndSeason")
    public void resumeByYearAndSeason(
            @RequestParam int year,
            @RequestParam String season
    ) {
        pollCollectService.resumeCollectPollByYearAndSeason(year, season);
    }

    @PostMapping("/resume/failed")
    public void resumeFailedCollection() {
        pollCollectService.resumeFailedCollection();
    }

    @GetMapping("/status")
    public List<PollCollectionStatusDto> status() {
        return pollCollectionStatusService.findAllPollCollectionStatusDtosWithAnimeDto();
    }
}
