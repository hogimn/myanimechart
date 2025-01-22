package com.hogimn.myanimechart.execute.service;

import com.hogimn.myanimechart.common.serviceregistry.domain.RegisteredService;
import com.hogimn.myanimechart.common.serviceregistry.service.ServiceRegistryService;
import com.hogimn.myanimechart.database.anime.dao.AnimeDao;
import com.hogimn.myanimechart.database.anime.dao.PollOptionDao;
import com.hogimn.myanimechart.database.anime.dto.PollDto;
import com.hogimn.myanimechart.database.anime.service.AnimeService;
import com.hogimn.myanimechart.database.anime.service.PollOptionService;
import com.hogimn.myanimechart.database.batch.aop.annotation.SaveBatchHistory;
import dev.katsute.mal4j.MyAnimeList;
import dev.katsute.mal4j.PaginatedIterator;
import dev.katsute.mal4j.forum.ForumTopic;
import dev.katsute.mal4j.forum.ForumTopicDetail;
import dev.katsute.mal4j.forum.property.Poll;
import dev.katsute.mal4j.forum.property.PollOption;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class PollCollectService {
    private final AnimeService animeService;
    private final MyAnimeList myAnimeList;
    private final PollOptionService pollOptionService;
    private final ServiceRegistryService serviceRegistryService;

    public PollCollectService(
            AnimeService animeService,
            MyAnimeList myAnimeList,
            PollOptionService pollOptionService,
            ServiceRegistryService serviceRegistryService
    ) {
        this.animeService = animeService;
        this.myAnimeList = myAnimeList;
        this.pollOptionService = pollOptionService;
        this.serviceRegistryService = serviceRegistryService;
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

    @SaveBatchHistory("#batchJobName")
    @SchedulerLock(name = "collectPollStatistics")
    public void collectPollStatistics(String batchJobName) {
        List<AnimeDao> animeDaos = animeService.getAiringAnime();
        animeDaos.forEach(animeDao -> {
            try {
                PaginatedIterator<ForumTopic> forumTopicPaginatedIterator = myAnimeList.getForumTopics()
                        .withQuery(animeDao.getTitle() + " Poll Episode Discussion")
                        .searchAll();

                while (forumTopicPaginatedIterator.hasNext()) {
                    ForumTopic forumTopic;
                    try {
                        forumTopic = forumTopicPaginatedIterator.next();
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        continue;
                    }
                    Long topicId = forumTopic.getID();
                    String topicTitle = forumTopic.getTitle();

                    if (!topicTitle.startsWith(animeDao.getTitle().split(" ")[0])) {
                        log.info("Topic name does not start with anime title first word. topic: {},  anime: {}",
                                forumTopic.getTitle(), animeDao.getTitle());
                        break;
                    }

                    if (checkMangaTopic(topicTitle)) {
                        log.info("Topic name is manga discussion. topic: {},  anime: {}",
                                forumTopic.getTitle(), animeDao.getTitle());
                        break;
                    }

                    if (!topicTitle.contains(animeDao.getTitle())) {
                        log.info("Topic name does not contain anime title. topic: {},  anime: {}",
                                forumTopic.getTitle(), animeDao.getTitle());
                        break;
                    }

                    if (!topicTitle.startsWith(animeDao.getTitle())) {
                        log.info("Topic name does not start with anime title. topic: {},  anime: {}",
                                forumTopic.getTitle(), animeDao.getTitle());
                        continue;
                    }

                    if (!topicTitle.endsWith("Discussion")) {
                        log.info("Topic name does not end with Discussion. {}", forumTopic.getTitle());
                        continue;
                    }

                    log.info("Collecting poll statistics for topic: {} {}", topicId, topicTitle);

                    ForumTopicDetail forumTopicDetail = myAnimeList.getForumTopicDetail(topicId);
                    Thread.sleep(2000);
                    Poll katsutePoll = forumTopicDetail.getPoll();
                    PollOption[] options = katsutePoll.getOptions();

                    if (options == null || options.length == 0) {
                        log.info("No poll options found for topicId: {}", topicId);
                        continue;
                    }

                    int episode = getEpisodeFromTopicTitle(topicTitle);
                    if (episode == -1) {
                        log.error("Failed to get episode from topic title: {}", topicTitle);
                        continue;
                    }

                    for (PollOption option : options) {
                        try {
                            int votes = option.getVotes();
                            String text = option.getText();
                            PollOptionDao pollOptionDao = pollOptionService.getPollOptionDao(text);

                            PollDto pollDto = new PollDto();
                            pollDto.setPollOptionId(pollOptionDao.getId());
                            pollDto.setAnimeId(animeDao.getId());
                            pollDto.setTopicId(topicId);
                            pollDto.setTitle(topicTitle);
                            pollDto.setEpisode(episode);
                            pollDto.setVotes(votes);

                            serviceRegistryService.send(RegisteredService.EXECUTE, "/poll/savePoll", pollDto);
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Failed to get forumTopic  '{} {}': {}", animeDao.getId(), animeDao.getTitle(), e.getMessage(), e);
            }
        });
    }

    private boolean checkMangaTopic(String topicTitle) {
        String regex = "Chapter \\d+ Discussion";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(topicTitle);
        return matcher.find();
    }
}
