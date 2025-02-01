package com.hogimn.myanimechart.database.anime.repository;

import com.hogimn.myanimechart.database.anime.entity.AnimeEntity;
import com.hogimn.myanimechart.database.anime.entity.PollEntity;
import com.hogimn.myanimechart.database.anime.entity.PollOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PollRepository extends JpaRepository<PollEntity, Long> {
    Optional<PollEntity> findByAnimeAndPollOptionAndTopicId(AnimeEntity animeEntity, PollOptionEntity pollOption, Long topicId);

    List<PollEntity> findByAnime(AnimeEntity animeEntity);

    List<PollEntity> findByAnimeOrderByEpisodeAscPollOptionAsc(AnimeEntity animeEntity);
}