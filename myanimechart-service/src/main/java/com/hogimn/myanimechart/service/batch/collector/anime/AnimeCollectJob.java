package com.hogimn.myanimechart.service.batch.collector.anime;

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
public class AnimeCollectJob {
    private final BatchService batchService;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final AnimeCollectService animeCollectService;

    @PostConstruct
    public void scheduleAnimeCollectionTask() {
        BatchResponse batchResponse = batchService
                .findBatchByName(this.getClass().getSimpleName());

        threadPoolTaskScheduler.schedule(
                () -> animeCollectService.collectSeasonal(batchResponse.getName()),
                new CronTrigger(batchResponse.getCron()));
    }
}
