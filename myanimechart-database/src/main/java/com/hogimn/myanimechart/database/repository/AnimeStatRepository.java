package com.hogimn.myanimechart.database.repository;

import com.hogimn.myanimechart.database.dao.AnimeDao;
import com.hogimn.myanimechart.database.dao.AnimeStatDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimeStatRepository extends JpaRepository<AnimeStatDao, Long> {
    List<AnimeStatDao> findByAnime(AnimeDao anime);
}