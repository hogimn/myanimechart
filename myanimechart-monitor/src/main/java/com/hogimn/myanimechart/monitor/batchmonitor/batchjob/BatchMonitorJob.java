package com.hogimn.myanimechart.monitor.batchmonitor.batchjob;

import com.hogimn.myanimechart.common.batch.BatchDto;
import com.hogimn.myanimechart.common.batch.BatchService;
import com.hogimn.myanimechart.monitor.batchmonitor.service.BatchMonitorService;
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
                () -> checkBatchNotExecuted(batchDto.getName()),
                new CronTrigger(batchDto.getCron()));
    }

    public void checkBatchNotExecuted(String batchJobName) {
        batchMonitorService.checkBatchNotExecuted(batchJobName);
    }
}
