package com.hogimn.myanimechart.common.config;

import org.springframework.boot.task.ThreadPoolTaskSchedulerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class BatchConfig {
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(ThreadPoolTaskSchedulerBuilder builder) {
        return builder.build();
    }
}
