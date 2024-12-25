package com.hogimn.myanimechart.monitor.batchjob;

import com.hogimn.myanimechart.database.domain.Batch;
import com.hogimn.myanimechart.database.service.BatchService;
import com.hogimn.myanimechart.monitor.service.MonitorService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BatchMonitorJob {
    private final BatchService batchService;
    private final MonitorService monitorService;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    public BatchMonitorJob(
            BatchService batchService, MonitorService monitorService,
            ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.batchService = batchService;
        this.monitorService = monitorService;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
    }

    @PostConstruct
    public void scheduleBatchMonitorTask() {
        Batch batch = batchService.getBatchByName(this.getClass().getSimpleName());

        threadPoolTaskScheduler.schedule(
                () -> checkBatchNotExecuted(batch.getName()),
                new CronTrigger(batch.getCron()));
    }

    public void checkBatchNotExecuted(String batchJobName) {
        monitorService.checkBatchNotExecuted(batchJobName);
    }
}
