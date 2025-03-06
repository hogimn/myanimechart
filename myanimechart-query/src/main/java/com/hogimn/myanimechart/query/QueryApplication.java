package com.hogimn.myanimechart.query;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "com.hogimn.myanimechart.query",
        "com.hogimn.myanimechart.common",
})
@EnableJpaRepositories(basePackages = "com.hogimn.myanimechart.common")
@EntityScan(basePackages = "com.hogimn.myanimechart.common")
public class QueryApplication {
    public static void main(String[] args) {
        SpringApplication.run(QueryApplication.class, args);
    }
}
