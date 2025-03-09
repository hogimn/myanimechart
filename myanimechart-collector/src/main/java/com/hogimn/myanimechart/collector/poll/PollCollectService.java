package com.hogimn.myanimechart.collector.poll;

import com.hogimn.myanimechart.common.anime.AnimeEntity;
import com.hogimn.myanimechart.common.anime.AnimeService;
import com.hogimn.myanimechart.common.batch.SaveBatchHistory;
import com.hogimn.myanimechart.common.myanimelist.MyAnimeListProvider;
import com.hogimn.myanimechart.common.poll.AnimeEpisodeTopicMappingEntity;
import com.hogimn.myanimechart.common.poll.AnimeEpisodeTopicMappingService;
import com.hogimn.myanimechart.common.poll.AnimeKeywordMappingService;
import com.hogimn.myanimechart.common.poll.PollDto;
import com.hogimn.myanimechart.common.poll.PollOptionEntity;
import com.hogimn.myanimechart.common.poll.PollOptionService;
import com.hogimn.myanimechart.common.serviceregistry.RegisteredService;
import com.hogimn.myanimechart.common.serviceregistry.ServiceRegistryService;
import com.hogimn.myanimechart.common.util.SleepUtil;
import dev.katsute.mal4j.forum.ForumTopic;
import dev.katsute.mal4j.forum.ForumTopicDetail;
import dev.katsute.mal4j.forum.property.Poll;
import dev.katsute.mal4j.forum.property.PollOption;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final AnimeKeywordMappingService animeKeywordMappingService;
    private final AnimeEpisodeTopicMappingService animeEpisodeTopicMappingService;

    public PollCollectService(
            AnimeService animeService,
            MyAnimeListProvider myAnimeListProvider,
            PollOptionService pollOptionService,
            ServiceRegistryService serviceRegistryService,
            AnimeKeywordMappingService animeKeywordMappingService,
            AnimeEpisodeTopicMappingService animeEpisodeTopicMappingService
    ) {
        this.animeService = animeService;
        this.myAnimeListProvider = myAnimeListProvider;
        this.pollOptionService = pollOptionService;
        this.serviceRegistryService = serviceRegistryService;
        this.animeKeywordMappingService = animeKeywordMappingService;
        this.animeEpisodeTopicMappingService = animeEpisodeTopicMappingService;
    }

    public void collectPollByAnimeId(long animeId) {
        AnimeEntity animeEntity = animeService.findAnimeEntityById(animeId);
        collectForumTopics(animeEntity);
    }

    public void collectPollByYearAndSeason(int year, String season) {
        List<AnimeEntity> animeEntities = animeService.findAnimeEntitiesByYearAndSeason(year, season);
        animeEntities.forEach(this::collectForumTopics);
    }

    @SaveBatchHistory("#batchJobName")
    @SchedulerLock(name = "collectPoll")
    public void collectSeasonalPoll(String batchJobName) {
        collectPollAllSeasonCurrentlyAiring();
        collectPollForceCollectTrue();
    }

    private void collectPollForceCollectTrue() {
        List<AnimeEntity> animeEntities = animeService.findAnimeEntitiesForceCollectTrue();
        animeEntities.forEach(this::collectForumTopics);
    }

    private void collectPollAllSeasonCurrentlyAiring() {
        List<AnimeEntity> animeEntities = animeService.findAnimeEntitiesAllSeasonCurrentlyAiring();
        List<AnimeEntity> animeEntitiesForceCollectTrue = animeService.findAnimeEntitiesForceCollectTrue();
        animeEntities.addAll(animeEntitiesForceCollectTrue);
        animeEntities.forEach(this::collectForumTopics);
    }

    @Synchronized
    private List<ForumTopic> fetchForumTopics(String keyword) {
        List<ForumTopic> forumTopics = new ArrayList<>();
        int offset = 0;
        int limit = 100;

        while (true) {
            List<ForumTopic> tempForumTopics = myAnimeListProvider
                    .getMyAnimeList()
                    .getForumTopics()
                    .withQuery(keyword)
                    .withLimit(limit)
                    .withOffset(offset)
                    .search();

            forumTopics.addAll(tempForumTopics);

            if (tempForumTopics.size() >= limit) {
                offset += limit;
            } else {
                break;
            }
        }
        return forumTopics;
    }

    private String getSearchKeyword(AnimeEntity animeEntity) {
        String searchKeyword = animeKeywordMappingService.findSearchKeywordByAnimeId(animeEntity.getId());
        return searchKeyword != null && !searchKeyword.isEmpty() ? searchKeyword : animeEntity.getTitle() + " Poll Episode Discussion";
    }

    private void collectForumTopics(AnimeEntity animeEntity) {
        try {
            log.info("Collecting poll for anime: {}", animeEntity.getTitle());

            String keyword = getSearchKeyword(animeEntity);
            List<ForumTopic> forumTopics = fetchForumTopics(keyword);
            SleepUtil.sleep(60 * 1000);

            int firstWordDiffCnt = 0;
            for (ForumTopic forumTopic : forumTopics) {
                long topicId = forumTopic.getID();
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

                savePoll(topicId, episode, animeEntity.getId());
                SleepUtil.sleep(60 * 1000);
            }
        } catch (Exception e) {
            log.error("Failed to get forumTopic  '{} {}': {}",
                    animeEntity.getId(), animeEntity.getTitle(), e.getMessage(), e);
        }

        collectPollByManualAnimeEpisodeTopicMapping(animeEntity);
    }

    private void collectPollByManualAnimeEpisodeTopicMapping(AnimeEntity animeEntity) {
        List<AnimeEpisodeTopicMappingEntity> animeEpisodeTopicMappingEntities = animeEpisodeTopicMappingService
                .findAnimeEpisodeTopicMappingEntityByAnimeIdEpisode(animeEntity.getId());

        for (AnimeEpisodeTopicMappingEntity animeEpisodeTopicMappingEntity : animeEpisodeTopicMappingEntities) {
            savePoll(animeEpisodeTopicMappingEntity.getTopicId(),
                    animeEpisodeTopicMappingEntity.getEpisode(),
                    animeEpisodeTopicMappingEntity.getAnimeId());
        }
    }

    @Synchronized
    private void savePoll(
            long topicId, int episode, long animeId) {
        Set<Integer> voteZeroOptions = new HashSet<>();
        voteZeroOptions.add(1);
        voteZeroOptions.add(2);
        voteZeroOptions.add(3);
        voteZeroOptions.add(4);
        voteZeroOptions.add(5);

        ForumTopicDetail forumTopicDetail = myAnimeListProvider.getMyAnimeList().getForumTopicDetail(topicId);
        Poll poll = forumTopicDetail.getPoll();
        String topicTitle = forumTopicDetail.getTitle();
        PollOption[] options = poll.getOptions();

        for (PollOption option : options) {
            try {
                int votes = option.getVotes();
                String text = option.getText();
                PollOptionEntity pollOptionEntity = pollOptionService.findPollOptionEntityByText(text);

                Integer optionId = pollOptionEntity.getId();
                voteZeroOptions.remove(optionId);

                PollDto pollDto = new PollDto();
                pollDto.setPollOptionId(pollOptionEntity.getId());
                pollDto.setAnimeId(animeId);
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
            PollOptionEntity pollOptionEntity = pollOptionService.findPollOptionEntityById(optionId);
            pollDto.setPollOptionId(pollOptionEntity.getId());
            pollDto.setAnimeId(animeId);
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
}
