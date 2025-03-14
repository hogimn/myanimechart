package com.hogimn.myanimechart.execute.anime;

import com.hogimn.myanimechart.common.poll.PollCollectionStatusDto;
import com.hogimn.myanimechart.common.poll.PollCollectionStatusService;
import com.hogimn.myanimechart.common.poll.PollDto;
import com.hogimn.myanimechart.common.poll.PollService;
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

    @PostMapping("/savePoll")
    public void savePoll(@RequestBody PollDto pollDto) {
        pollService.save(pollDto);
    }

    @PostMapping("/savePollCollectionStatus")
    public void savePollCollectionStatus(@RequestBody PollCollectionStatusDto pollCollectionStatusDto) {
        pollCollectionStatusService.sendSave(pollCollectionStatusDto);
    }
}
