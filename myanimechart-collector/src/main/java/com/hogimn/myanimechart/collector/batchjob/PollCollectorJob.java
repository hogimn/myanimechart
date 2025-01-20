package com.hogimn.myanimechart.collector.batchjob;

import com.hogimn.myanimechart.collector.service.PollCollectService;
import com.hogimn.myanimechart.database.batch.domain.Batch;
import com.hogimn.myanimechart.database.batch.service.BatchService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PollCollectorJob {
    private final PollCollectService forumTopicCollectService;
    private final BatchService batchService;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    public PollCollectorJob(
            PollCollectService forumCollectService,
            BatchService batchService,
            ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.forumTopicCollectService = forumCollectService;
        this.batchService = batchService;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
    }

    @PostConstruct
    public void schedulePollCollectionTask() {
        Batch batch = batchService
                .getBatchByName(this.getClass().getSimpleName());

        threadPoolTaskScheduler.schedule(
                () -> collectPollStat(batch.getName()),
                new CronTrigger(batch.getCron()));
    }

    public void collectPollStat(String batchJobName) {
        forumTopicCollectService.collectPollStatistics(batchJobName);
    }
}
