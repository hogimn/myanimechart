package com.hogimn.myanimechart.collector.poll;

import com.hogimn.myanimechart.common.batch.BatchDto;
import com.hogimn.myanimechart.common.batch.BatchService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PollCollectJob {
    private final BatchService batchService;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final PollCollectService pollCollectService;

    public PollCollectJob(
            BatchService batchService,
            ThreadPoolTaskScheduler threadPoolTaskScheduler,
            PollCollectService pollCollectService) {
        this.batchService = batchService;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.pollCollectService = pollCollectService;
    }

    @PostConstruct
    public void schedulePollCollectionTask() {
        BatchDto batchDto = batchService
                .getBatchDtoByName(this.getClass().getSimpleName());

        threadPoolTaskScheduler.schedule(
                () -> collectPoll(batchDto.getName()),
                new CronTrigger(batchDto.getCron()));
    }

    public void collectPoll(String batchJobName) {
        pollCollectService.collectSeasonalPoll(batchJobName);
    }
}
