package com.hogimn.myanimechart.batch.collector.poll;

import com.hogimn.myanimechart.batch.core.BatchDto;
import com.hogimn.myanimechart.batch.core.BatchService;
import com.hogimn.myanimechart.batch.collector.poll.status.PollCollectionStatusService;
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
    private final PollCollectionStatusService pollCollectionStatusService;

    public PollCollectJob(
            BatchService batchService,
            ThreadPoolTaskScheduler threadPoolTaskScheduler,
            PollCollectService pollCollectService,
            PollCollectionStatusService pollCollectionStatusService) {
        this.batchService = batchService;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.pollCollectService = pollCollectService;
        this.pollCollectionStatusService = pollCollectionStatusService;
    }

    @PostConstruct
    public void schedulePollCollectionTask() {
        pollCollectionStatusService.setFailForStartedButNotFinished();
        pollCollectionStatusService.setFailForWait();

        BatchDto batchDto = batchService
                .findBatchDtoByName(this.getClass().getSimpleName());

        threadPoolTaskScheduler.schedule(
                () -> collectPoll(batchDto.getName()),
                new CronTrigger(batchDto.getCron()));
    }

    public void collectPoll(String batchJobName) {
        pollCollectService.collectSeasonalPoll(batchJobName);
        pollCollectService.resumeFailedCollection();
    }
}
