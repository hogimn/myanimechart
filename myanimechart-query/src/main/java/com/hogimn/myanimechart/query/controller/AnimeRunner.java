package com.hogimn.myanimechart.query.controller;

import dev.katsute.mal4j.MyAnimeList;
import dev.katsute.mal4j.anime.Anime;
import dev.katsute.mal4j.forum.ForumCategory;
import dev.katsute.mal4j.forum.ForumTopic;
import dev.katsute.mal4j.query.ForumSearchQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AnimeRunner implements ApplicationRunner {

    private final MyAnimeList myAnimeList;

    public AnimeRunner(MyAnimeList myAnimeList) {
        this.myAnimeList = myAnimeList;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        ForumSearchQuery forumTopics = myAnimeList.getForumTopics();
//        ForumSearchQuery forumSearchQuery = forumTopics.withQuery("Ore dake");
//        List<ForumTopic> search = forumSearchQuery.search();
//        for (ForumTopic forumTopic : search) {
//            log.info("{}", forumTopic);
//        }
    }
}
