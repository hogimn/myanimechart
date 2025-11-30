package com.hogimn.myanimechart.service.batch.collector.anime;

import com.hogimn.myanimechart.service.batch.BatchDto;
import com.hogimn.myanimechart.service.batch.BatchService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AnimeCollectJob {
    private final BatchService batchService;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final AnimeCollectService animeCollectService;

    public AnimeCollectJob(
            BatchService batchService,
            ThreadPoolTaskScheduler threadPoolTaskScheduler,
            AnimeCollectService animeCollectService
    ) {
        this.batchService = batchService;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.animeCollectService = animeCollectService;
    }

    @PostConstruct
    public void scheduleAnimeCollectionTask() {
        BatchDto batchDto = batchService
                .findBatchDtoByName(this.getClass().getSimpleName());

        threadPoolTaskScheduler.schedule(
                () -> animeCollectService.collectSeasonal(batchDto.getName()),
                new CronTrigger(batchDto.getCron()));
    }
}
