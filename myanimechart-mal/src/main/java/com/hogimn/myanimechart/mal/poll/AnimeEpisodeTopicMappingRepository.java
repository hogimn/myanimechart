package com.hogimn.myanimechart.mal.poll;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimeEpisodeTopicMappingRepository extends JpaRepository<AnimeEpisodeTopicMappingEntity, Integer> {
    List<AnimeEpisodeTopicMappingEntity> findByAnimeId(long animeId);
}
