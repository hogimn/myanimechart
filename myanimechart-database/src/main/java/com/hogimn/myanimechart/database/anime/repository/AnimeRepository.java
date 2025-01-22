package com.hogimn.myanimechart.database.anime.repository;

import com.hogimn.myanimechart.database.anime.dao.AnimeDao;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AnimeRepository extends JpaRepository<AnimeDao, Long> {
    Optional<AnimeDao> findByTitle(String title);

    List<AnimeDao> findByYearAndSeason(Integer year, String season);

    @Query("SELECT a FROM AnimeDao a WHERE NOT (a.year = :year AND a.season = :season) " +
            "AND NOT (a.year = :nextYear AND a.season = :nextSeason) " +
            "AND (a.airStatus = :currentlyAiring OR" +
            " (a.airStatus = :finishedAiring AND EXTRACT(MONTH FROM a.finishedAt) = EXTRACT(MONTH FROM CURRENT_TIMESTAMP)))")
    List<AnimeDao> findAiringAnimeExcludingCurrentAndNextSeason(Integer year, String season,
                                                                Integer nextYear, String nextSeason,
                                                                String currentlyAiring, String finishedAiring);

    @Query("SELECT a FROM AnimeDao a" +
            " WHERE a.airStatus = :currentlyAiring OR" +
            " (a.airStatus = :finishedAiring AND EXTRACT(MONTH FROM a.finishedAt) = EXTRACT(MONTH FROM CURRENT_TIMESTAMP))")
    List<AnimeDao> findAiringAnime(String currentlyAiring, String finishedAiring);

    List<AnimeDao> findAllByTitleContaining(String title);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM AnimeDao a WHERE a.id = :id")
    AnimeDao findByIdWithLock(@Param("id") Long id);
}