package com.hogimn.myanimechart.common.config;

import dev.katsute.mal4j.MyAnimeList;
import dev.katsute.mal4j.property.ExperimentalFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyAnimeConfig {
    private final String clientId;

    public MyAnimeConfig(@Value("${myanimelist.clientId}") String clientId) {
        this.clientId = clientId;
    }

    @Bean
    public MyAnimeList myAnimeList() {
        MyAnimeList myAnimeList = MyAnimeList.withClientID(clientId);
        myAnimeList.enableExperimentalFeature(ExperimentalFeature.ALL);
        return myAnimeList;
    }
}
