package com.hogimn.myanimechart.execute.controller;

import com.hogimn.myanimechart.database.anime.dto.PollDto;
import com.hogimn.myanimechart.database.anime.service.PollService;
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

    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    @PostMapping("/savePoll")
    public void savePoll(@RequestBody PollDto pollDto) {
        pollService.upsertPoll(pollDto);
    }
}
