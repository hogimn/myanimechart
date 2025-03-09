package com.hogimn.myanimechart.common.poll;

import com.hogimn.myanimechart.common.util.DateUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class PollService {
    private final PollRepository pollRepository;

    public PollService(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    private List<PollEntity> findByAnimeIdOrderByEpisodeAscTopicIdAscPollOptionIdAsc(long animeId) {
        return pollRepository.findByAnimeIdOrderByEpisodeAscTopicIdAscPollOptionIdAsc(animeId);
    }

    public List<PollDto> findPollDtosByAnimeId(long animeId) {
        List<PollEntity> pollEntities = findByAnimeIdOrderByEpisodeAscTopicIdAscPollOptionIdAsc(animeId);
        List<PollEntity> uniquePollEntities = removeDuplicateForum(pollEntities);

        return uniquePollEntities.stream().map(PollDto::from).toList();
    }

    private List<PollEntity> removeDuplicateForum(List<PollEntity> pollEntities) {
        Map<String, Map<Long, Integer>> episodeOptionVotesMap = new HashMap<>();

        for (PollEntity poll : pollEntities) {
            String key = poll.getEpisode() + "-" + poll.getPollOptionId();
            episodeOptionVotesMap.putIfAbsent(key, new HashMap<>());
            episodeOptionVotesMap.get(key).merge(poll.getTopicId(), poll.getVotes(), Integer::sum);
        }

        Map<String, Long> maxTopicMap = new HashMap<>();
        for (Map.Entry<String, Map<Long, Integer>> entry : episodeOptionVotesMap.entrySet()) {
            maxTopicMap.put(entry.getKey(),
                    entry.getValue().entrySet().stream()
                            .max(Comparator.comparingInt(Map.Entry::getValue))
                            .get().getKey()
            );
        }

        return pollEntities.stream()
                .filter(poll -> {
                    String key = poll.getEpisode() + "-" + poll.getPollOptionId();
                    return maxTopicMap.get(key).equals(poll.getTopicId());
                })
                .toList();
    }


    public Optional<PollEntity> findByAnimeIdAndPollOptionIdAndTopicId(
            long animeId, int pollOptionId, long topicId
    ) {
        return pollRepository.findByAnimeIdAndPollOptionIdAndTopicId(animeId, pollOptionId, topicId);
    }

    public PollEntity save(PollEntity pollEntity) {
        return pollRepository.save(pollEntity);
    }

    public void delete(PollEntity found) {
        pollRepository.delete(found);
    }

    @Transactional
    public void save(PollDto pollDto) {
        Optional<PollEntity> optional = findByAnimeIdAndPollOptionIdAndTopicId(
                pollDto.getAnimeId(), pollDto.getPollOptionId(), pollDto.getTopicId());
        LocalDateTime now = DateUtil.now();
        if (optional.isPresent()) {
            PollEntity found = optional.get();
            found.setAnimeId(pollDto.getAnimeId());
            found.setPollOptionId(pollDto.getPollOptionId());
            found.setTopicId(pollDto.getTopicId());
            found.setTitle(pollDto.getTitle());
            found.setVotes(pollDto.getVotes());
            found.setUpdatedAt(now);
            found.setEpisode(pollDto.getEpisode());
            PollEntity saved = save(found);
            log.info("Updated existing poll: {}", saved);
            return;
        }

        PollEntity newPoll = new PollEntity();
        newPoll.setAnimeId(pollDto.getAnimeId());
        newPoll.setPollOptionId(pollDto.getPollOptionId());
        newPoll.setTopicId(pollDto.getTopicId());
        newPoll.setTitle(pollDto.getTitle());
        newPoll.setVotes(pollDto.getVotes());
        newPoll.setCreatedAt(now);
        newPoll.setEpisode(pollDto.getEpisode());
        PollEntity saved = save(newPoll);
        log.info("Inserted new poll: {}", saved);
    }
}
