package com.hogimn.myanimechart.collector.service;

import com.hogimn.myanimechart.database.anime.dao.AnimeDao;
import com.hogimn.myanimechart.database.anime.dao.PollOptionDao;
import com.hogimn.myanimechart.database.anime.domain.Anime;
import com.hogimn.myanimechart.database.anime.service.AnimeService;
import com.hogimn.myanimechart.database.anime.service.PollOptionService;
import com.hogimn.myanimechart.database.anime.service.PollService;
import com.hogimn.myanimechart.database.batch.aop.annotation.SaveBatchHistory;
import dev.katsute.mal4j.MyAnimeList;
import dev.katsute.mal4j.PaginatedIterator;
import dev.katsute.mal4j.forum.ForumTopic;
import dev.katsute.mal4j.forum.ForumTopicDetail;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class PollCollectService {
    private final AnimeService animeService;
    private final MyAnimeList myAnimeList;
    private final PollOptionService pollOptionService;
    private final PollService pollService;

    public PollCollectService(
            AnimeService animeService,
            MyAnimeList myAnimeList,
            PollOptionService pollOptionService,
            PollService pollService) {
        this.animeService = animeService;
        this.myAnimeList = myAnimeList;
        this.pollOptionService = pollOptionService;
        this.pollService = pollService;
    }

    private int getEpisodeFromTopicTitle(String topicTitle) {
        String regex = "Episode (\\d+)";
        Pattern pattern = Pattern.compile(regex);

        try {
            Matcher matcher = pattern.matcher(topicTitle);
            if (matcher.find()) {
                String episodeNumber = matcher.group(1);
                return Integer.parseInt(episodeNumber);
            } else {
                System.out.println("No episode number found in: " + topicTitle);
                return -1;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return -1;
        }
    }

    @Transactional
    @SaveBatchHistory("#batchJobName")
    @SchedulerLock(name = "collectPollStatistics")
    public void collectPollStatistics(String batchJobName) {
        List<Anime> animeList = animeService.getAiringAnime();
        animeList.forEach(anime -> {
            try {
                PaginatedIterator<ForumTopic> forumTopicPaginatedIterator = myAnimeList.getForumTopics()
                        .withQuery(anime.getTitle() + " Episode")
                        .searchAll();

                while (forumTopicPaginatedIterator.hasNext()) {
                    ForumTopic forumTopic;
                    try {
                        forumTopic = forumTopicPaginatedIterator.next();
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        continue;
                    }
                    Long topicId = forumTopic.getID();
                    String topicTitle = forumTopic.getTitle();

                    if (!topicTitle.startsWith(anime.getTitle() + " Episode")) {
                        log.info("Topic name does not start with anime title. topic: {},  anime: {}", forumTopic.getTitle(), anime.getTitle());
                        continue;
                    }

                    if (!topicTitle.endsWith("Discussion")) {
                        log.info("Topic name does not end with Discussion. {}", forumTopic.getTitle());
                        continue;
                    }

                    ForumTopicDetail forumTopicDetail = myAnimeList.getForumTopicDetail(topicId);
                    dev.katsute.mal4j.forum.property.Poll katsutePoll = forumTopicDetail.getPoll();
                    dev.katsute.mal4j.forum.property.PollOption[] options = katsutePoll.getOptions();

                    if (options == null || options.length == 0) {
                        log.info("No poll options found for topicId: {}", topicId);
                        continue;
                    }

                    int episode = getEpisodeFromTopicTitle(topicTitle);
                    if (episode == -1) {
                        log.error("Failed to get episode from topic title: {}", topicTitle);
                        continue;
                    }

                    for (dev.katsute.mal4j.forum.property.PollOption option : options) {
                        try {
                            int votes = option.getVotes();
                            String text = option.getText();
                            PollOptionDao pollOptionDao = pollOptionService.getPollOptionDao(text);
                            pollService.upsertPoll(AnimeDao.from(anime), pollOptionDao, topicId, topicTitle, votes, episode);
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Failed to forumTopic  '{} {}': {}", anime.getId(), anime.getTitle(), e.getMessage(), e);
            }
        });
    }
}
