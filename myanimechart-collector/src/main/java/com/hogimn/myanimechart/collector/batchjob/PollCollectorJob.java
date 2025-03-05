package com.hogimn.myanimechart.collector.batchjob;

import com.hogimn.myanimechart.common.serviceregistry.RegisteredService;
import com.hogimn.myanimechart.common.serviceregistry.ServiceRegistryService;
import com.hogimn.myanimechart.database.batch.BatchDto;
import com.hogimn.myanimechart.database.batch.BatchService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PollCollectorJob {
    private final BatchService batchService;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final ServiceRegistryService serviceRegistryService;

    public PollCollectorJob(
            BatchService batchService,
            ThreadPoolTaskScheduler threadPoolTaskScheduler,
            ServiceRegistryService serviceRegistryService
    ) {
        this.batchService = batchService;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.serviceRegistryService = serviceRegistryService;
    }

    @PostConstruct
    public void schedulePollCollectionTask() {
        BatchDto batchDto = batchService
                .getBatchDtoByName(this.getClass().getSimpleName());

        threadPoolTaskScheduler.schedule(
                () -> collectPollStat(batchDto.getName()),
                new CronTrigger(batchDto.getCron()));
    }

    public void collectPollStat(String batchJobName) {
        serviceRegistryService.send(RegisteredService.EXECUTE, "/pollCollect/collectPollStatistics", batchJobName);
    }
}
