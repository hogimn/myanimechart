package com.hogimn.myanimechart.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "com.hogimn.myanimechart.security",
        "com.hogimn.myanimechart.common",
        "com.hogimn.myanimechart.database"
})
@EnableJpaRepositories(basePackages = "com.hogimn.myanimechart.database")
@EntityScan(basePackages = "com.hogimn.myanimechart.database")
public class SecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }
}
