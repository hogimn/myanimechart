package com.hogimn.myanimechart.database.anime.repository;

import com.hogimn.myanimechart.database.anime.entity.AnimeEntity;
import com.hogimn.myanimechart.database.anime.entity.AnimeStatEntity;
import com.hogimn.myanimechart.database.anime.entity.key.AnimeStatId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimeStatRepository extends JpaRepository<AnimeStatEntity, AnimeStatId> {
    List<AnimeStatEntity> findByAnime(AnimeEntity anime);
    List<AnimeStatEntity> findByAnimeOrderByRecordedAtAsc(AnimeEntity anime);
}
