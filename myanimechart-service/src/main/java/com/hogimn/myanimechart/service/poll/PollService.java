package com.hogimn.myanimechart.service.poll;

import com.hogimn.myanimechart.core.common.result.SaveResult;
import com.hogimn.myanimechart.core.domain.poll.PollEntity;
import com.hogimn.myanimechart.core.domain.poll.PollRepository;
import com.hogimn.myanimechart.core.common.util.DateUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PollService {
    private final PollRepository pollRepository;

    @Transactional
    public SaveResult save(PollCreateRequest request) {
        Optional<PollEntity> optional = pollRepository.findByAnimeIdAndPollOptionIdAndTopicId(
                request.getAnimeId(), request.getPollOptionId(), request.getTopicId());

        LocalDateTime now = DateUtil.now();
        SaveResult result;

        if (optional.isPresent()) {
            PollEntity found = optional.get();

            found.setAnimeId(request.getAnimeId());
            found.setPollOptionId(request.getPollOptionId());
            found.setTopicId(request.getTopicId());
            found.setTitle(request.getTitle());
            found.setVotes(request.getVotes());
            found.setEpisode(request.getEpisode());
            found.setUpdatedAt(now);

            pollRepository.save(found);
            result = SaveResult.UPDATED;

        } else {
            PollEntity newPoll = new PollEntity();

            newPoll.setAnimeId(request.getAnimeId());
            newPoll.setPollOptionId(request.getPollOptionId());
            newPoll.setTopicId(request.getTopicId());
            newPoll.setTitle(request.getTitle());
            newPoll.setVotes(request.getVotes());
            newPoll.setEpisode(request.getEpisode());
            newPoll.setCreatedAt(now);
            newPoll.setUpdatedAt(now);

            pollRepository.save(newPoll);
            result = SaveResult.CREATED;
        }

        return result;
    }

    public List<PollResponse> filterByMaxTopicVotes(List<PollResponse> pollResponses) {
        if (pollResponses == null || pollResponses.isEmpty()) {
            return List.of();
        }

        Map<String, Long> maxTopicMap = pollResponses.stream()
                .collect(
                        Collectors.groupingBy(
                                poll -> poll.getEpisode() + "-" + poll.getPollOptionId(),
                                Collectors.groupingBy(
                                        PollResponse::getTopicId,
                                        Collectors.summingInt(PollResponse::getVotes)
                                )
                        )
                ).entrySet().stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().entrySet().stream()
                                        .max(Comparator.comparingInt(Map.Entry::getValue))
                                        .map(Map.Entry::getKey)
                                        .orElseThrow(() -> new IllegalStateException("Max topic not found"))
                        )
                );

        return pollResponses.stream()
                .filter(poll -> {
                    String key = poll.getEpisode() + "-" + poll.getPollOptionId();
                    return maxTopicMap.get(key).equals(poll.getTopicId());
                })
                .toList();
    }
}
