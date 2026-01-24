package com.hogimn.myanimechart.service.poll;

import com.hogimn.myanimechart.core.common.result.SaveResult;
import com.hogimn.myanimechart.core.domain.poll.PollEntity;
import com.hogimn.myanimechart.core.domain.poll.PollRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PollService {
    private final PollRepository pollRepository;

    @Transactional
    public SaveResult save(PollCreateRequest request) {
        return pollRepository.findByAnimeIdAndPollOptionIdAndTopicId(
                        request.animeId(), request.pollOptionId(), request.topicId())
                .map(found -> {
                    found.update(request.title(), request.votes(), request.episode());
                    return SaveResult.UPDATED;
                })
                .orElseGet(() -> {
                    PollEntity newPoll = PollEntity.builder()
                            .animeId(request.animeId())
                            .pollOptionId(request.pollOptionId())
                            .topicId(request.topicId())
                            .title(request.title())
                            .votes(request.votes())
                            .episode(request.episode())
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    pollRepository.save(newPoll);
                    return SaveResult.CREATED;
                });
    }

    public List<PollResponse> filterByMaxTopicVotes(List<PollResponse> pollResponses) {
        if (pollResponses == null || pollResponses.isEmpty()) {
            return List.of();
        }

        Map<String, Long> maxTopicMap = pollResponses.stream()
                .collect(
                        Collectors.groupingBy(
                                poll -> poll.episode() + "-" + poll.pollOptionId(),
                                Collectors.groupingBy(
                                        PollResponse::topicId,
                                        Collectors.summingInt(PollResponse::votes)
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
                    String key = poll.episode() + "-" + poll.pollOptionId();
                    return maxTopicMap.get(key).equals(poll.topicId());
                })
                .toList();
    }
}
