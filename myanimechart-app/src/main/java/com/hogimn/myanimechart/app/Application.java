package com.hogimn.myanimechart.app;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
        "com.hogimn.myanimechart.mal",
        "com.hogimn.myanimechart.batch",
        "com.hogimn.myanimechart.core",
})
@EnableJpaRepositories(basePackages = {
        "com.hogimn.myanimechart.mal",
        "com.hogimn.myanimechart.batch",
        "com.hogimn.myanimechart.core",
})
@EntityScan(basePackages = {
        "com.hogimn.myanimechart.mal",
        "com.hogimn.myanimechart.batch",
        "com.hogimn.myanimechart.core",
})
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT24H")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
