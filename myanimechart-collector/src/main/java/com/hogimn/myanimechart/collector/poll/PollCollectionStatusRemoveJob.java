package com.hogimn.myanimechart.collector.poll;

import com.hogimn.myanimechart.common.batch.BatchDto;
import com.hogimn.myanimechart.common.batch.BatchService;
import com.hogimn.myanimechart.common.poll.PollCollectionStatusService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PollCollectionStatusRemoveJob {
    private final BatchService batchService;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final PollCollectionStatusService pollCollectionStatusService;

    public PollCollectionStatusRemoveJob(
            BatchService batchService,
            ThreadPoolTaskScheduler threadPoolTaskScheduler,
            PollCollectionStatusService pollCollectionStatusService) {
        this.batchService = batchService;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.pollCollectionStatusService = pollCollectionStatusService;
    }

    @PostConstruct
    public void schedulePollCollectionTask() {
        BatchDto batchDto = batchService
                .findBatchDtoByName(this.getClass().getSimpleName());

        threadPoolTaskScheduler.schedule(
                () -> removeUnusedPollCollectionStatus(batchDto.getName()),
                new CronTrigger(batchDto.getCron()));
    }

    public void removeUnusedPollCollectionStatus(String batchJobName) {
        pollCollectionStatusService.removeUnusedPollCollectionStatus(batchJobName);
    }
}
