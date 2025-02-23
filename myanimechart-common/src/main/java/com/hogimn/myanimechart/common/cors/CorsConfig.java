package com.hogimn.myanimechart.common.cors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    private final String activeProfile;

    public CorsConfig(
            @Value("${spring.profiles.active}")
            String activeProfile
    ) {
        this.activeProfile = activeProfile;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if ("dev".equals(activeProfile)) {
            registry.addMapping("/**")
                    .allowedOrigins("http://localhost:3001")
                    .allowedHeaders("*")
                    .allowedMethods("*")
                    .allowCredentials(true);
        } else {
            registry.addMapping("/**")
                    .allowedOrigins("https://myanimechart.com")
                    .allowedHeaders("*")
                    .allowedMethods("*")
                    .allowCredentials(true);
        }
    }
}
