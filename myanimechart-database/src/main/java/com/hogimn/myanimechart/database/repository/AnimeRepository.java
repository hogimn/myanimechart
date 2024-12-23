package com.hogimn.myanimechart.database.repository;

import com.hogimn.myanimechart.database.dao.AnimeDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnimeRepository extends JpaRepository<AnimeDao, Long> {
    Optional<AnimeDao> findByTitle(String title);

    List<AnimeDao> findByYearAndSeason(Integer year, String season);
}