package com.hogimn.myanimechart.monitor;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
        "com.hogimn.myanimechart.monitor",
        "com.hogimn.myanimechart.common"
})
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.hogimn.myanimechart.common")
@EntityScan(basePackages = "com.hogimn.myanimechart.common")
@EnableSchedulerLock(defaultLockAtMostFor = "PT3M")
public class MonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(MonitorApplication.class, args);
    }
}
