package com.hogimn.myanimechart.common.poll;

import com.hogimn.myanimechart.common.anime.AnimeEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    private List<PollEntity> getByAnimeOrderByEpisodeAscTopicIdAscPollOptionAsc(AnimeEntity animeEntity) {
        return pollRepository.findByAnimeOrderByEpisodeAscTopicIdAscPollOptionAsc(animeEntity);
    }

    public List<PollDto> getPollDtosByAnime(AnimeEntity animeEntity) {
        List<PollEntity> pollEntities = getByAnimeOrderByEpisodeAscTopicIdAscPollOptionAsc(animeEntity);
        List<PollEntity> uniquePollEntities = removeDuplicateForum(pollEntities);

        return uniquePollEntities.stream().map(PollDto::from).toList();
    }

    private List<PollEntity> removeDuplicateForum(List<PollEntity> pollEntities) {
        Map<String, Map<Long, Integer>> episodeOptionVotesMap = new HashMap<>();

        for (PollEntity poll : pollEntities) {
            String key = poll.getEpisode() + "-" + poll.getPollOption().getId();
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
                    String key = poll.getEpisode() + "-" + poll.getPollOption().getId();
                    return maxTopicMap.get(key).equals(poll.getTopicId());
                })
                .toList();
    }


    public Optional<PollEntity> findByAnimeAndPollOptionAndTopicId(
            AnimeEntity anime, PollOptionEntity pollOption, long topicId
    ) {
        return pollRepository.findByAnimeAndPollOptionAndTopicId(anime, pollOption, topicId);
    }

    public Optional<PollEntity> findByAnimeAndPollOptionAndEpisode(
            AnimeEntity anime, PollOptionEntity pollOption, int episode
    ) {
        return pollRepository.findByAnimeAndPollOptionAndTopicId(anime, pollOption, episode);
    }

    public PollEntity save(PollEntity pollEntity) {
        return pollRepository.save(pollEntity);
    }

    public void delete(PollEntity found) {
        pollRepository.delete(found);
    }
}
