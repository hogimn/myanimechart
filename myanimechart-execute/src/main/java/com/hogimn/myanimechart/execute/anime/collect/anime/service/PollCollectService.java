package com.hogimn.myanimechart.execute.anime.collect.anime.service;

import com.hogimn.myanimechart.common.myanimelist.MyAnimeListProvider;
import com.hogimn.myanimechart.common.serviceregistry.RegisteredService;
import com.hogimn.myanimechart.common.serviceregistry.ServiceRegistryService;
import com.hogimn.myanimechart.database.anime.dto.PollDto;
import com.hogimn.myanimechart.database.anime.entity.AnimeEntity;
import com.hogimn.myanimechart.database.anime.entity.PollOptionEntity;
import com.hogimn.myanimechart.database.anime.service.AnimeService;
import com.hogimn.myanimechart.database.anime.service.PollOptionService;
import com.hogimn.myanimechart.database.batch.aop.SaveBatchHistory;
import dev.katsute.mal4j.PaginatedIterator;
import dev.katsute.mal4j.forum.ForumTopic;
import dev.katsute.mal4j.forum.ForumTopicDetail;
import dev.katsute.mal4j.forum.property.Poll;
import dev.katsute.mal4j.forum.property.PollOption;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class PollCollectService {
    private final AnimeService animeService;
    private final MyAnimeListProvider myAnimeListProvider;
    private final PollOptionService pollOptionService;
    private final ServiceRegistryService serviceRegistryService;

    public PollCollectService(
            AnimeService animeService,
            MyAnimeListProvider myAnimeListProvider,
            PollOptionService pollOptionService,
            ServiceRegistryService serviceRegistryService
    ) {
        this.animeService = animeService;
        this.myAnimeListProvider = myAnimeListProvider;
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
        List<AnimeEntity> animeEntities = animeService.getAnimeEntitiesForPollCollection();
        List<AnimeEntity> animeEntitiesForceCollectTrue = animeService.getAnimeEntitiesForceCollectTrue();
        animeEntities.addAll(animeEntitiesForceCollectTrue);

        animeEntities.forEach(animeEntity -> {
            try {
                log.info("Collecting poll statistics for anime: {}", animeEntity.getTitle());

                // TODO: Create keyword mapping table in database for irregular search keyword
                String keyword = animeEntity.getTitle() + " Poll Episode Discussion";
                if (animeEntity.getTitle().equals("Touhai: Ura Rate Mahjong Touhairoku")) {
                    keyword = "Touhai: Ura Rate Mahjong Touhai Roku" + " Poll Episode Discussion";
                }

                PaginatedIterator<ForumTopic> forumTopicPaginatedIterator = myAnimeListProvider
                        .getMyAnimeList()
                        .getForumTopics()
                        .withQuery(keyword)
                        .withLimit(100)
                        .searchAll();

                int firstWordDiffCnt = 0;
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

                    if (!topicTitle.startsWith(animeEntity.getTitle().split(" ")[0])) {
                        log.info("Topic name does not start with anime title first word. topic: {},  anime: {}",
                                forumTopic.getTitle(), animeEntity.getTitle());
                        firstWordDiffCnt++;
                        if (firstWordDiffCnt > 10) {
                            break;
                        } else {
                            continue;
                        }
                    }

                    if (!topicTitle.endsWith("Discussion")) {
                        log.info("Topic name does not end with Discussion. {}", forumTopic.getTitle());
                        break;
                    }

                    if (checkMangaTopic(topicTitle)) {
                        log.info("Topic name is manga discussion. topic: {},  anime: {}",
                                forumTopic.getTitle(), animeEntity.getTitle());
                        break;
                    }

                    if (!checkTitleSame(topicTitle, animeEntity.getTitle())) {
                        log.info("Topic name is far different from anime name. topic: {},  anime: {}",
                                forumTopic.getTitle(), animeEntity.getTitle());
                        continue;
                    }

                    int episode = getEpisodeFromTopicTitle(topicTitle);
                    if (episode == -1) {
                        log.error("Failed to get episode from topic title: {}", topicTitle);
                        break;
                    }

                    savePoll(topicId, topicTitle, episode, animeEntity);
                }
            } catch (Exception e) {
                log.error("Failed to get forumTopic  '{} {}': {}", animeEntity.getId(), animeEntity.getTitle(), e.getMessage(), e);
            }
        });
    }

    private void savePoll(
            Long topicId, String topicTitle, int episode, AnimeEntity animeEntity)
            throws InterruptedException {
        Set<Integer> voteZeroOptions = new HashSet<>();
        voteZeroOptions.add(1);
        voteZeroOptions.add(2);
        voteZeroOptions.add(3);
        voteZeroOptions.add(4);
        voteZeroOptions.add(5);

        ForumTopicDetail forumTopicDetail = myAnimeListProvider.getMyAnimeList().getForumTopicDetail(topicId);
        Thread.sleep(1000);
        Poll poll = forumTopicDetail.getPoll();
        PollOption[] options = poll.getOptions();

        for (PollOption option : options) {
            try {
                int votes = option.getVotes();
                String text = option.getText();
                PollOptionEntity pollOptionEntity = pollOptionService.getPollOptionEntity(text);

                Integer optionId = pollOptionEntity.getId();
                voteZeroOptions.remove(optionId);

                PollDto pollDto = new PollDto();
                pollDto.setPollOptionId(pollOptionEntity.getId());
                pollDto.setAnimeId(animeEntity.getId());
                pollDto.setTopicId(topicId);
                pollDto.setTitle(topicTitle);
                pollDto.setEpisode(episode);
                pollDto.setVotes(votes);

                serviceRegistryService.send(RegisteredService.EXECUTE, "/poll/savePoll", pollDto);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        voteZeroOptions.forEach((optionId) -> {
            PollDto pollDto = new PollDto();
            PollOptionEntity pollOptionEntity = pollOptionService.getPollOptionEntityById(optionId);
            pollDto.setPollOptionId(pollOptionEntity.getId());
            pollDto.setAnimeId(animeEntity.getId());
            pollDto.setTopicId(topicId);
            pollDto.setTitle(topicTitle);
            pollDto.setEpisode(episode);
            pollDto.setVotes(0);

            serviceRegistryService.send(RegisteredService.EXECUTE, "/poll/savePoll", pollDto);
        });
    }

    private boolean checkTitleSame(String topicTitle, String animeTitle) {
        if (topicTitle == null || animeTitle == null) {
            return false;
        }


        topicTitle = topicTitle.toLowerCase().replaceAll("[. :;\\-!?]", "");
        animeTitle = animeTitle.toLowerCase().replaceAll("[. :;\\-!?]", "");

        int indexOfEpisode = topicTitle.lastIndexOf("episode");
        if (indexOfEpisode == -1) {
            return false;
        }

        topicTitle = topicTitle.substring(0, indexOfEpisode);

        return topicTitle.equals(animeTitle);
    }

    private boolean checkMangaTopic(String topicTitle) {
        String regex = "Chapter \\d+ Discussion";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(topicTitle);
        return matcher.find();
    }
}
