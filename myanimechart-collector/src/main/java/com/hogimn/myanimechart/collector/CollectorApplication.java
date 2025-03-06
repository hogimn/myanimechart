package com.hogimn.myanimechart.collector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
        "com.hogimn.myanimechart.collector",
        "com.hogimn.myanimechart.common"
})
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.hogimn.myanimechart.common")
@EntityScan(basePackages = "com.hogimn.myanimechart.common")
public class CollectorApplication {
    public static void main(String[] args) {
        SpringApplication.run(CollectorApplication.class, args);
    }
}
