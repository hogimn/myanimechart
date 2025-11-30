package com.hogimn.myanimechart.service.poll;

import com.hogimn.myanimechart.core.common.apicalllog.ApiLoggable;
import com.hogimn.myanimechart.service.poll.status.PollCollectionStatusService;
import com.hogimn.myanimechart.service.poll.status.PollCollectionStatusDto;
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
    private final PollCollectionStatusService pollCollectionStatusService;

    public PollController(
            PollService pollService,
            PollCollectionStatusService pollCollectionStatusService
    ) {
        this.pollService = pollService;
        this.pollCollectionStatusService = pollCollectionStatusService;
    }

    @ApiLoggable
    @PostMapping("/save")
    public void save(@RequestBody PollDto pollDto) {
        pollService.save(pollDto);
    }

    @ApiLoggable
    @PostMapping("/save-collection-status")
    public void savePollCollectionStatus(@RequestBody PollCollectionStatusDto pollCollectionStatusDto) {
        pollCollectionStatusService.save(pollCollectionStatusDto);
    }
}
