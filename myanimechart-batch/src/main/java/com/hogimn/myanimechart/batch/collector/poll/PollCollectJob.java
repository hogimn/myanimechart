package com.hogimn.myanimechart.batch.collector.poll;

import com.hogimn.myanimechart.batch.collector.poll.status.BatchPollCollectionStatusService;
import com.hogimn.myanimechart.batch.core.BatchDto;
import com.hogimn.myanimechart.batch.core.BatchService;
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
    private final BatchPollCollectionStatusService batchPollCollectionStatusService;

    public PollCollectJob(
            BatchService batchService,
            ThreadPoolTaskScheduler threadPoolTaskScheduler,
            PollCollectService pollCollectService,
            BatchPollCollectionStatusService batchPollCollectionStatusService) {
        this.batchService = batchService;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.pollCollectService = pollCollectService;
        this.batchPollCollectionStatusService = batchPollCollectionStatusService;
    }

    @PostConstruct
    public void schedulePollCollectionTask() {
        batchPollCollectionStatusService.setFailForStartedButNotFinished();
        batchPollCollectionStatusService.setFailForWait();

        BatchDto batchDto = batchService
                .findBatchDtoByName(this.getClass().getSimpleName());

        threadPoolTaskScheduler.schedule(
                () -> collectPoll(batchDto.getName()),
                new CronTrigger(batchDto.getCron()));
    }

    public void collectPoll(String batchJobName) {
        pollCollectService.collectSeasonal(batchJobName);
        pollCollectService.resumeFailed();
    }
}
