package com.hogimn.myanimechart.database.repository;

import com.hogimn.myanimechart.database.dao.AnimeDao;
import com.hogimn.myanimechart.database.dao.AnimeStatDao;
import com.hogimn.myanimechart.database.dao.key.AnimeStatId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimeStatRepository extends JpaRepository<AnimeStatDao, AnimeStatId> {
    List<AnimeStatDao> findByAnime(AnimeDao anime);
}