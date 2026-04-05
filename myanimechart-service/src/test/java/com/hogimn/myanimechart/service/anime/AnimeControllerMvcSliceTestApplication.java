package com.hogimn.myanimechart.service.anime;

import com.hogimn.myanimechart.service.exception.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;

@SpringBootApplication(
        scanBasePackageClasses = {AnimeController.class, GlobalExceptionHandler.class},
        exclude = {
                DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class,
                FlywayAutoConfiguration.class,
                EurekaClientAutoConfiguration.class
        })
public class AnimeControllerMvcSliceTestApplication {
}
