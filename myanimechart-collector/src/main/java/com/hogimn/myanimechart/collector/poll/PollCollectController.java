package com.hogimn.myanimechart.collector.poll;

import com.hogimn.myanimechart.common.apicalllog.ApiLoggable;
import com.hogimn.myanimechart.common.poll.PollCollectionStatusDto;
import com.hogimn.myanimechart.common.poll.PollCollectionStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pollCollect")
@Slf4j
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

    @ApiLoggable
    @PostMapping("/collectSeasonalPoll")
    public void collectSeasonalPoll() {
        pollCollectService.collectSeasonalPoll("PollCollectJob");
    }

    @ApiLoggable
    @PostMapping("/collectPollByYearAndSeason")
    public void collectPollByYearAndSeason(
            @RequestParam("year") int year, @RequestParam("season") String season
    ) {
        pollCollectService.collectPollByYearAndSeason(year, season);
    }

    @ApiLoggable
    @PostMapping("/collectPollByAnimeId")
    public void collectPollByAnimeId(@RequestParam("animeId") long animeId) {
        pollCollectService.collectPollByAnimeId(animeId);
    }

    @ApiLoggable
    @PostMapping("/collectPollByEpisode")
    public void collectPollByEpisode(
            @RequestParam("animeId") long animeId,
            @RequestParam("topicId") long topicId,
            @RequestParam("episode") int episode) {
        pollCollectService.collectPollByAnimeIdAndTopicId(animeId, topicId, episode);
    }

    @ApiLoggable
    @GetMapping("/findAllPollCollectionStatusWithAnime")
    public List<PollCollectionStatusDto> findAllPollCollectionStatusWithAnime() {
        return pollCollectionStatusService.findAllPollCollectionStatusDtosWithAnimeDto();
    }

    @ApiLoggable
    @PostMapping("/resumeCollectPollByYearAndSeason")
    public void resumeCollectPollByYearAndSeason(
            @RequestParam("year") int year, @RequestParam("season") String season
    ) {
        pollCollectService.resumeCollectPollByYearAndSeason(year, season);
    }

    @ApiLoggable
    @PostMapping("/resumeFailedCollection")
    public void resumeFailedCollection() {
        pollCollectService.resumeFailedCollection();
    }

    @ApiLoggable
    @PostMapping("/collectEmptyPoll")
    public void collectEmptyPoll() {
        pollCollectService.collectEmptyPoll();
    }
}
