package com.hogimn.myanimechart.common.poll;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnimeKeywordMappingRepository extends JpaRepository<AnimeKeywordMappingEntity, Integer> {
    Optional<AnimeKeywordMappingEntity> findByAnimeId(long animeId);
}
