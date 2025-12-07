package com.hogimn.myanimechart.service.batch.collector.poll;

import com.hogimn.myanimechart.service.anime.AnimeResponse;
import com.hogimn.myanimechart.service.batch.collector.poll.status.BatchPollCollectionStatusService;
import com.hogimn.myanimechart.service.batch.history.SaveBatchHistory;
import com.hogimn.myanimechart.core.common.myanimelist.MyAnimeListProvider;
import com.hogimn.myanimechart.core.common.serviceregistry.RegisteredService;
import com.hogimn.myanimechart.core.common.serviceregistry.ServiceRegistryService;
import com.hogimn.myanimechart.core.common.util.SleepUtil;
import com.hogimn.myanimechart.service.anime.AnimeService;
import com.hogimn.myanimechart.service.poll.mapping.AnimeEpisodeTopicMappingResponse;
import com.hogimn.myanimechart.service.poll.mapping.AnimeEpisodeTopicMappingService;
import com.hogimn.myanimechart.service.poll.mapping.AnimeKeywordMappingService;
import com.hogimn.myanimechart.service.poll.PollResponse;
import com.hogimn.myanimechart.service.poll.option.PollOptionResponse;
import com.hogimn.myanimechart.service.poll.option.PollOptionService;
import dev.katsute.mal4j.forum.ForumTopic;
import dev.katsute.mal4j.forum.ForumTopicDetail;
import dev.katsute.mal4j.forum.property.Poll;
import dev.katsute.mal4j.forum.property.PollOption;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class PollCollectService {
    private final AnimeService animeService;
    private final MyAnimeListProvider myAnimeListProvider;
    private final PollOptionService pollOptionService;
    private final ServiceRegistryService serviceRegistryService;
    private final AnimeKeywordMappingService animeKeywordMappingService;
    private final AnimeEpisodeTopicMappingService animeEpisodeTopicMappingService;
    private final BatchPollCollectionStatusService batchPollCollectionStatusService;

    @SchedulerLock(name = "collectPollByAnimeId")
    public void collectByAnimeId(long animeId) {
        AnimeResponse anime = animeService.getAnimeById(animeId);
        batchPollCollectionStatusService.sendSavePollCollectionStatusForWait(anime.getId());
        collectForumTopics(anime);
    }

    @SchedulerLock(name = "collectPollByTopicId")
    public void collectByEpisode(long animeId, long topicId, int episode) {
        savePoll(topicId, episode, animeId);
    }

    @SchedulerLock(name = "collectPollByYearAndSeason")
    public void collectPollByYearAndSeason(int year, String season) {
        List<AnimeResponse> animeResponses = animeService.getAnimesByYearAndSeasonOrderByScore(year, season);
        animeResponses.forEach(animeEntity ->
                batchPollCollectionStatusService.sendSavePollCollectionStatusForWait(animeEntity.getId()));
        animeResponses.forEach(this::collectForumTopics);
    }

    @SaveBatchHistory("#batchJobName")
    @SchedulerLock(name = "collectSeasonalPoll")
    public void collectSeasonal(String batchJobName) {
        log.info("Start of collecting seasonal poll");

        collectPollAllSeasonCurrentlyAiring();
        collectPollForceCollectTrue();

        log.info("End of collecting seasonal poll");
    }

    private void collectPollForceCollectTrue() {
        List<AnimeResponse> animeResponses = animeService.getForceCollectTrueAnimes();
        animeResponses.forEach(animeEntity ->
                batchPollCollectionStatusService.sendSavePollCollectionStatusForWait(animeEntity.getId()));
        animeResponses.forEach(this::collectForumTopics);
    }

    private void collectPollAllSeasonCurrentlyAiring() {
        List<AnimeResponse> currentlyAiringAnimes = animeService.getCurrentAiringAnimes();
        List<AnimeResponse> forceCollectAnimes = animeService.getForceCollectTrueAnimes();
        currentlyAiringAnimes.addAll(forceCollectAnimes);
        currentlyAiringAnimes.forEach(anime ->
                batchPollCollectionStatusService.sendSavePollCollectionStatusForWait(anime.getId()));
        currentlyAiringAnimes.forEach(this::collectForumTopics);
    }

    private List<ForumTopic> fetchForumTopics(String keyword) {
        List<ForumTopic> forumTopics = new ArrayList<>();
        int offset = 0;
        int limit = 100;

        log.info("keyword is {}", keyword);

        if (keyword.isEmpty()) {
            return null;
        }

        while (true) {
            List<ForumTopic> tempForumTopics = myAnimeListProvider
                    .getMyAnimeList()
                    .getForumTopics()
                    .withQuery(keyword)
                    .withLimit(limit)
                    .withOffset(offset)
                    .search();

            forumTopics.addAll(tempForumTopics);

            log.info("offset: {}, limit: {}, size of list: {}", offset, limit, forumTopics.size());

            if (tempForumTopics.size() >= limit) {
                offset += limit;
            } else {
                break;
            }

            SleepUtil.sleepForMAL();
        }
        return forumTopics;
    }

    private String getSearchKeyword(AnimeResponse animeResponse) {
        String searchKeyword = animeKeywordMappingService
                .getSearchKeyword(animeResponse.getId());

        return (searchKeyword != null && !searchKeyword.isEmpty())
                ? searchKeyword
                : animeResponse.getTitle();
    }

    private void collectForumTopics(AnimeResponse animeResponse) {
        log.info("Start of collecting poll for anime: {}", animeResponse.getId());

        batchPollCollectionStatusService.sendSavePollCollectionStatusForStart(animeResponse.getId());

        boolean isFail = false;
        try {
            String animeTitle = getSearchKeyword(animeResponse);
            String searchKeyword = animeTitle + " Poll Episode Discussion";
            List<ForumTopic> forumTopics = fetchForumTopics(searchKeyword);
            SleepUtil.sleepForMAL();

            if (!findMatchingTopicAndSavePollResult(animeResponse, forumTopics, animeTitle)) {
                animeTitle = animeResponse.getEnglishTitle();
                if (!animeTitle.isEmpty()) {
                    searchKeyword = animeTitle + " Poll Episode Discussion";
                    forumTopics = fetchForumTopics(searchKeyword);
                    SleepUtil.sleepForMAL();
                    findMatchingTopicAndSavePollResult(animeResponse, forumTopics, animeTitle);
                }
            }

            collectPollByAnimeEpisodeTopicMapping(animeResponse);
        } catch (Exception e) {
            batchPollCollectionStatusService.sendSavePollCollectionStatusForFail(animeResponse.getId());
            log.error("Failed to get forumTopic  '{} {}': {}",
                    animeResponse.getId(), animeResponse.getTitle(), e.getMessage(), e);
            isFail = true;
        }

        if (!isFail) {
            batchPollCollectionStatusService.sendSavePollCollectionStatusForEnd(animeResponse.getId());
        }

        log.info("End of collecting poll for anime: {}", animeResponse.getId());
    }

    private boolean findMatchingTopicAndSavePollResult(
            AnimeResponse animeResponse, List<ForumTopic> forumTopics, String animeTitle) {
        boolean found = false;

        if (forumTopics == null) {
            return false;
        }

        for (ForumTopic forumTopic : forumTopics) {
            long topicId = forumTopic.getID();
            String topicTitle = forumTopic.getTitle();

            if (!isFirstWordMatching(topicTitle, animeTitle)) {
                log.info("Topic title does not start with anime title first word, or vice versa. topic: {},  anime: {}",
                        topicTitle, animeTitle);
                continue;
            }

            if (!topicTitle.endsWith("Discussion")) {
                log.info("Topic name does not end with Discussion. {}", forumTopic.getTitle());
                continue;
            }

            if (checkMangaTopic(topicTitle)) {
                log.info("Topic name is manga discussion. topic: {},  anime: {}",
                        forumTopic.getTitle(), animeResponse.getTitle());
                continue;
            }

            if (!checkTitleSame(topicTitle, animeTitle)) {
                log.info("Topic name is far different from anime name. topic: {},  anime: {}",
                        forumTopic.getTitle(), animeTitle);
                continue;
            }

            int episode = getEpisodeFromTopicTitle(topicTitle);
            if (episode == -1) {
                log.error("Failed to get episode from topic title: {}", topicTitle);
                continue;
            }

            savePoll(topicId, episode, animeResponse.getId());
            found = true;
            SleepUtil.sleepForMAL();
        }

        return found;
    }

    private boolean isFirstWordMatching(String topicTitle, String animeTitle) {
        if (topicTitle == null || animeTitle == null) {
            return false;
        }

        if (topicTitle.isEmpty() || animeTitle.isEmpty()) {
            return false;
        }


        topicTitle = topicTitle.toLowerCase().replaceAll("[\\[\\]\".:;\\-!?]", "");
        animeTitle = animeTitle.toLowerCase().replaceAll("[\\[\\]\".:;\\-!?]", "");

        String topicFirstWord = topicTitle.split("\\s", 2)[0];
        String animeFirstWord = animeTitle.split("\\s", 2)[0];

        return topicTitle.startsWith(animeFirstWord) || animeTitle.startsWith(topicFirstWord);
    }


    private void collectPollByAnimeEpisodeTopicMapping(AnimeResponse animeResponse) {
        List<AnimeEpisodeTopicMappingResponse> mappingResponses = animeEpisodeTopicMappingService
                .getAnimeEpisodeTopicMappings(animeResponse.getId());

        for (AnimeEpisodeTopicMappingResponse mappingResponse : mappingResponses) {
            savePoll(mappingResponse.getTopicId(),
                    mappingResponse.getEpisode(),
                    mappingResponse.getAnimeId());
        }
    }

    private void savePoll(
            long topicId, int episode, long animeId) {
        Set<Integer> voteZeroOptions = new HashSet<>();
        voteZeroOptions.add(1);
        voteZeroOptions.add(2);
        voteZeroOptions.add(3);
        voteZeroOptions.add(4);
        voteZeroOptions.add(5);

        ForumTopicDetail forumTopicDetail;
        try {
            forumTopicDetail = myAnimeListProvider.getMyAnimeList().getForumTopicDetail(topicId);
        } catch (Exception e) {
            log.error("getForumTopicDetail Failed: Anime Id: {}, Episode: {}, Topic Id:{}, Error Message: {}",
                    animeId, episode, topicId, e.getMessage(), e);
            return;
        }

        Poll poll = forumTopicDetail.getPoll();
        String topicTitle = forumTopicDetail.getTitle();
        PollOption[] options = poll.getOptions();

        if (options == null) {
            log.info("There is no poll options (null)");
            return;
        }

        for (PollOption option : options) {
            try {
                int votes = option.getVotes();
                String text = option.getText();
                PollOptionResponse pollOptionResponse = pollOptionService.getPollOptionByText(text);

                Integer optionId = pollOptionResponse.getId();
                voteZeroOptions.remove(optionId);

                PollResponse pollResponse = new PollResponse();
                pollResponse.setPollOptionId(pollOptionResponse.getId());
                pollResponse.setAnimeId(animeId);
                pollResponse.setTopicId(topicId);
                pollResponse.setTitle(topicTitle);
                pollResponse.setEpisode(episode);
                pollResponse.setVotes(votes);

                serviceRegistryService.send(RegisteredService.APPLICATION, "/poll", pollResponse);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        voteZeroOptions.forEach((optionId) -> {
            PollResponse pollResponse = new PollResponse();
            PollOptionResponse pollOptionResponse = pollOptionService.getPollOptionById(optionId);
            pollResponse.setPollOptionId(pollOptionResponse.getId());
            pollResponse.setAnimeId(animeId);
            pollResponse.setTopicId(topicId);
            pollResponse.setTitle(topicTitle);
            pollResponse.setEpisode(episode);
            pollResponse.setVotes(0);

            serviceRegistryService.send(RegisteredService.APPLICATION, "/poll", pollResponse);
        });
    }

    private boolean checkTitleSame(String topicTitle, String animeTitle) {
        if (topicTitle == null || animeTitle == null) {
            return false;
        }


        topicTitle = topicTitle.toLowerCase()
                .replaceAll("[\\[\\]\". :;\\-!?]", "")
                .replaceAll("(TV)", "");
        animeTitle = animeTitle.toLowerCase().replaceAll("[\\[\\]\". :;\\-!?]", "")
                .replaceAll("(TV)", "");

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

    public void resumeByYearAndSeason(int year, String season) {
        List<AnimeResponse> animeResponses = animeService
                .getFailedCollectionAnimesByYearAndSeason(year, season);
        animeResponses.forEach(animeEntity ->
                batchPollCollectionStatusService.sendSavePollCollectionStatusForWait(animeEntity.getId()));
        animeResponses.forEach(this::collectForumTopics);
    }

    public void resumeFailed() {
        List<AnimeResponse> animeResponses = animeService
                .getFailedCollectionAnimes();
        animeResponses.forEach(animeEntity ->
                batchPollCollectionStatusService.sendSavePollCollectionStatusForWait(animeEntity.getId()));
        animeResponses.forEach(this::collectForumTopics);
    }

    public void collectEmpty() {
        List<AnimeResponse> animeResponses = animeService
                .getAnimesWithEmptyPoll();
        animeResponses.forEach(animeEntity ->
                batchPollCollectionStatusService.sendSavePollCollectionStatusForWait(animeEntity.getId()));
        animeResponses.forEach(this::collectForumTopics);
    }

    public void collectAll() {
        List<AnimeResponse> animeResponses = animeService.getAllAnimes();
        animeResponses.forEach(animeEntity ->
                batchPollCollectionStatusService.sendSavePollCollectionStatusForWait(animeEntity.getId()));
        animeResponses.forEach(this::collectForumTopics);
    }
}
