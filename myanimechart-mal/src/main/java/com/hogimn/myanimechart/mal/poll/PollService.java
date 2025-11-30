package com.hogimn.myanimechart.mal.poll;

import com.hogimn.myanimechart.core.domain.poll.PollEntity;
import com.hogimn.myanimechart.core.domain.poll.PollRepository;
import com.hogimn.myanimechart.core.common.util.DateUtil;
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

    public List<PollDto> removeDuplicateForum(List<PollDto> pollDtos) {
        Map<String, Map<Long, Integer>> episodeOptionVotesMap = new HashMap<>();

        for (PollDto poll : pollDtos) {
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

        return pollDtos.stream()
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

    public void save(PollEntity pollEntity) {
        pollRepository.save(pollEntity);
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
            save(found);
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
        save(newPoll);
    }
}
