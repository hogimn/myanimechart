package com.hogimn.myanimechart.query;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
        "com.hogimn.myanimechart.query",
        "com.hogimn.myanimechart.common",
        "com.hogimn.myanimechart.database"
})
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.hogimn.myanimechart.database.repository")
@EntityScan(basePackages = "com.hogimn.myanimechart.database.dao")
public class QueryApplication {
    public static void main(String[] args) {
        SpringApplication.run(QueryApplication.class, args);
    }
}
