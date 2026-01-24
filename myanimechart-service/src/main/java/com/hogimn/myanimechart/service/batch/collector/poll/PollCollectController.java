package com.hogimn.myanimechart.service.batch.collector.poll;

import com.hogimn.myanimechart.core.common.apicalllog.ApiLoggable;
import com.hogimn.myanimechart.service.batch.collector.poll.status.BatchPollCollectionStatusService;
import com.hogimn.myanimechart.service.batch.collector.poll.status.PollCollectionStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@ApiLoggable
@RestController
@RequestMapping("/collect-poll")
@RequiredArgsConstructor
public class PollCollectController {
    private final PollCollectService pollCollectService;
    private final BatchPollCollectionStatusService batchPollCollectionStatusService;

    @PostMapping("seasonal")
    public ResponseEntity<Void> collectSeasonal() {
        pollCollectService.collectSeasonal("PollCollectJob");
        return ResponseEntity.noContent().build();
    }

    @PostMapping("by-year-and-season")
    public ResponseEntity<Void> collectByYearAndSeason(@RequestParam int year, @RequestParam String season) {
        pollCollectService.collectPollByYearAndSeason(year, season);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Void> collectByAnimeId(@RequestParam long animeId) {
        pollCollectService.collectByAnimeId(animeId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/by-episode")
    public ResponseEntity<Void> collectByEpisode(
            @RequestParam long animeId,
            @RequestParam long topicId,
            @RequestParam int episode) {
        pollCollectService.collectByEpisode(animeId, topicId, episode);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/all")
    public ResponseEntity<Void> collectAll() {
        pollCollectService.collectAll();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/empty")
    public ResponseEntity<Void> collectEmpty() {
        pollCollectService.collectEmpty();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/resume/by-year-and-season")
    public ResponseEntity<Void> resumeByYearAndSeason(
            @RequestParam int year,
            @RequestParam String season
    ) {
        pollCollectService.resumeByYearAndSeason(year, season);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/resume/failed")
    public ResponseEntity<Void> resumeFailedCollection() {
        pollCollectService.resumeFailed();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statuses")
    public ResponseEntity<List<PollCollectionStatusResponse>> getStatuses() {
        return ResponseEntity.ok(batchPollCollectionStatusService.getStatuses());
    }
}
