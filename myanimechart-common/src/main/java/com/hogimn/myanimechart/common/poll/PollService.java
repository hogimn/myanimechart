package com.hogimn.myanimechart.common.poll;

import com.hogimn.myanimechart.common.anime.AnimeEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PollService {
    private final PollRepository pollRepository;

    public PollService(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    private List<PollEntity> getPollEntitiesByAnime(AnimeEntity animeEntity) {
        return pollRepository.findByAnimeOrderByEpisodeAscPollOptionAsc(animeEntity);
    }

    public List<PollDto> getPollDtosByAnime(AnimeEntity animeEntity) {
        List<PollEntity> pollEntities = getPollEntitiesByAnime(animeEntity);
        return pollEntities.stream().map(PollDto::from).toList();
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
