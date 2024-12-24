package com.hogimn.myanimechart.collector.job;

import com.hogimn.myanimechart.collector.service.AnimeCollectService;
import com.hogimn.myanimechart.database.service.BatchService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AnimeCollectorJob {
    private final AnimeCollectService animeCollectService;
    private final BatchService batchService;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    public AnimeCollectorJob(
            AnimeCollectService animeCollectService,
            BatchService batchService,
            ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.animeCollectService = animeCollectService;
        this.batchService = batchService;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
    }

    @PostConstruct
    public void scheduleAnimeCollectionTask() {
        String batchJobName = AnimeCollectorJob.class.getSimpleName();
        String cronExpression = batchService
                .getBatchByName(batchJobName)
                .getCron();

        threadPoolTaskScheduler.schedule(
                () -> collectAnimeAndAnimeStat(batchJobName), new CronTrigger(cronExpression));
    }

    public void collectAnimeAndAnimeStat(String batchJobName) {
        animeCollectService.collectAnimeStatistics(batchJobName);
    }
}
