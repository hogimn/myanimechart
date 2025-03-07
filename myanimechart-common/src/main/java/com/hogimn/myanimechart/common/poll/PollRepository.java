package com.hogimn.myanimechart.common.poll;

import com.hogimn.myanimechart.common.anime.AnimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PollRepository extends JpaRepository<PollEntity, Long> {
    Optional<PollEntity> findByAnimeAndPollOptionAndTopicId(AnimeEntity animeEntity, PollOptionEntity pollOption, long topicId);

    Optional<PollEntity> findByAnimeAndPollOptionAndEpisode(AnimeEntity animeEntity, PollOptionEntity pollOption, int episode);

    List<PollEntity> findByAnime(AnimeEntity animeEntity);

    List<PollEntity> findByAnimeOrderByEpisodeAscTopicIdAscPollOptionAsc(AnimeEntity animeEntity);
}