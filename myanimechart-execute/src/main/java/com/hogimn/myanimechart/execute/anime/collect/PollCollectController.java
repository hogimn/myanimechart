package com.hogimn.myanimechart.execute.anime.collect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pollCollect")
@Slf4j
public class PollCollectController {
    private final PollCollectService pollCollectService;

    public PollCollectController(PollCollectService pollCollectService) {
        this.pollCollectService = pollCollectService;
    }

    @PostMapping("/collectPollStatistics")
    public void collectPollStatistics(@RequestBody String batchJobName) {
        pollCollectService.collectPollStatistics(batchJobName);
    }
}
