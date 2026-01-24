package com.hogimn.myanimechart.service.batch.collector.poll;

import com.hogimn.myanimechart.service.batch.BatchResponse;
import com.hogimn.myanimechart.service.batch.BatchService;
import com.hogimn.myanimechart.service.batch.collector.poll.status.BatchPollCollectionStatusService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PollCollectJob {
    private final BatchService batchService;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final PollCollectService pollCollectService;
    private final BatchPollCollectionStatusService batchPollCollectionStatusService;

    @PostConstruct
    public void schedulePollCollectionTask() {
        batchPollCollectionStatusService.setFailForStartedButNotFinished();
        batchPollCollectionStatusService.setFailForWait();

        BatchResponse batchResponse = batchService
                .findBatchByName(this.getClass().getSimpleName());

        threadPoolTaskScheduler.schedule(
                () -> collectPoll(batchResponse.name()),
                new CronTrigger(batchResponse.cron()));
    }

    public void collectPoll(String batchJobName) {
        pollCollectService.collectSeasonal(batchJobName);
        pollCollectService.resumeFailed();
    }
}
