package com.hogimn.myanimechart.service.poll;

import com.hogimn.myanimechart.core.common.result.SaveResult;
import com.hogimn.myanimechart.core.domain.poll.PollEntity;
import com.hogimn.myanimechart.core.domain.poll.PollRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
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

    /**
     * For each (episode, pollOption) group, keeps only rows whose topic has the highest total vote sum
     * (ties: one arbitrary winning topic id, same as {@link Comparator} max behavior).
     */
    public List<PollResponse> filterByMaxTopicVotes(List<PollResponse> pollResponses) {
        if (pollResponses == null || pollResponses.isEmpty()) {
            return List.of();
        }

        Map<String, Long> winningTopicByEpisodeOption = pollResponses.stream()
                .collect(Collectors.groupingBy(
                        PollService::episodeOptionKey,
                        Collectors.groupingBy(PollResponse::topicId, Collectors.summingInt(PollResponse::votes))))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().entrySet().stream()
                                .max(Comparator.comparingInt(Map.Entry::getValue))
                                .map(Map.Entry::getKey)
                                .orElseThrow(() -> new IllegalStateException("No topic votes for " + entry.getKey()))));

        return pollResponses.stream()
                .filter(poll -> winningTopicByEpisodeOption.get(episodeOptionKey(poll)).equals(poll.topicId()))
                .toList();
    }

    private static String episodeOptionKey(PollResponse poll) {
        return poll.episode() + "-" + poll.pollOptionId();
    }
}
