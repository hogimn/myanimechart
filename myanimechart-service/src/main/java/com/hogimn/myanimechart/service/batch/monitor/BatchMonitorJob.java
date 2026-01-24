package com.hogimn.myanimechart.service.batch.monitor;

import com.hogimn.myanimechart.service.batch.BatchResponse;
import com.hogimn.myanimechart.service.batch.BatchService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BatchMonitorJob {
    private final BatchService batchService;
    private final BatchMonitorService batchMonitorService;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @PostConstruct
    public void scheduleBatchMonitorTask() {
        BatchResponse batchResponse = batchService.findBatchByName(this.getClass().getSimpleName());

        threadPoolTaskScheduler.schedule(
                () -> checkNotExecutedBatches(batchResponse.name()),
                new CronTrigger(batchResponse.cron()));
    }

    public void checkNotExecutedBatches(String batchJobName) {
        batchMonitorService.checkNotExecutedBatches(batchJobName);
    }
}
