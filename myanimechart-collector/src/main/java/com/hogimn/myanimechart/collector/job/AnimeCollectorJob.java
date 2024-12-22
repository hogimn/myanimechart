package com.hogimn.myanimechart.collector.job;

import com.hogimn.myanimechart.collector.service.AnimeCollectService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AnimeCollectorJob {
    private final AnimeCollectService animeCollectService;

    public AnimeCollectorJob(AnimeCollectService animeCollectService) {
        this.animeCollectService = animeCollectService;
    }

    @Scheduled(cron = "0 0 * * * ?")
    @Transactional
    public void collect() {
        log.info("start of collecting anime statistics");
        animeCollectService.collectAnimeStatistics();
        log.info("end of collecting anime statistics");
    }
}
