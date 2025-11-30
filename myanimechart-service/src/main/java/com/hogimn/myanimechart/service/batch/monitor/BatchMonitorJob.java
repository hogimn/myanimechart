package com.hogimn.myanimechart.service.batch.monitor;

import com.hogimn.myanimechart.service.batch.BatchDto;
import com.hogimn.myanimechart.service.batch.BatchService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BatchMonitorJob {
    private final BatchService batchService;
    private final BatchMonitorService batchMonitorService;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    public BatchMonitorJob(
            BatchService batchService,
            BatchMonitorService batchMonitorService,
            ThreadPoolTaskScheduler threadPoolTaskScheduler
    ) {
        this.batchService = batchService;
        this.batchMonitorService = batchMonitorService;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
    }

    @PostConstruct
    public void scheduleBatchMonitorTask() {
        BatchDto batchDto = batchService.findBatchDtoByName(this.getClass().getSimpleName());

        threadPoolTaskScheduler.schedule(
                () -> checkNotExecutedBatches(batchDto.getName()),
                new CronTrigger(batchDto.getCron()));
    }

    public void checkNotExecutedBatches(String batchJobName) {
        batchMonitorService.checkNotExecutedBatches(batchJobName);
    }
}
