package com.hogimn.myanimechart.execute.anime;

import com.hogimn.myanimechart.common.poll.PollDto;
import com.hogimn.myanimechart.common.poll.PollFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/poll")
@Slf4j
public class PollController {
    private final PollFacadeService pollFacadeService;

    public PollController(PollFacadeService pollFacadeService) {
        this.pollFacadeService = pollFacadeService;
    }

    @PostMapping("/savePoll")
    public void savePoll(@RequestBody PollDto pollDto) {
        pollFacadeService.save(pollDto);
    }
}
