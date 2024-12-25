package com.hogimn.myanimechart.database.anime.repository;

import com.hogimn.myanimechart.database.anime.dao.AnimeDao;
import com.hogimn.myanimechart.database.anime.dao.AnimeStatDao;
import com.hogimn.myanimechart.database.anime.dao.key.AnimeStatId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimeStatRepository extends JpaRepository<AnimeStatDao, AnimeStatId> {
    List<AnimeStatDao> findByAnime(AnimeDao anime);
}