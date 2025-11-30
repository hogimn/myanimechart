package com.hogimn.myanimechart.mal.poll;

import com.hogimn.myanimechart.core.common.apicalllog.ApiLoggable;
import com.hogimn.myanimechart.mal.poll.status.MalPollCollectionStatusService;
import com.hogimn.myanimechart.mal.poll.status.PollCollectionStatusDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/poll")
@Slf4j
public class PollController {
    private final PollService pollService;
    private final MalPollCollectionStatusService malPollCollectionStatusService;

    public PollController(
            PollService pollService,
            MalPollCollectionStatusService malPollCollectionStatusService
    ) {
        this.pollService = pollService;
        this.malPollCollectionStatusService = malPollCollectionStatusService;
    }

    @ApiLoggable
    @PostMapping("/savePoll")
    public void savePoll(@RequestBody PollDto pollDto) {
        pollService.save(pollDto);
    }

    @ApiLoggable
    @PostMapping("/savePollCollectionStatus")
    public void savePollCollectionStatus(@RequestBody PollCollectionStatusDto pollCollectionStatusDto) {
        malPollCollectionStatusService.save(pollCollectionStatusDto);
    }
}
