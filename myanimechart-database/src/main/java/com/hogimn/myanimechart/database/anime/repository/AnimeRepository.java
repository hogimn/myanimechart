package com.hogimn.myanimechart.database.anime.repository;

import com.hogimn.myanimechart.database.anime.entity.AnimeEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AnimeRepository extends JpaRepository<AnimeEntity, Long> {
    Optional<AnimeEntity> findByTitle(String title);

    List<AnimeEntity> findByYearAndSeason(Integer year, String season);

    @Query("SELECT a FROM AnimeEntity a WHERE NOT (a.year = :year AND a.season = :season) " +
            "AND NOT (a.year = :nextYear AND a.season = :nextSeason) " +
            "AND (a.airStatus = :currentlyAiring OR" +
            " (a.airStatus = :finishedAiring AND " +
            "(EXTRACT(MONTH FROM a.finishedAt) = EXTRACT(MONTH FROM CURRENT_TIMESTAMP) " +
            "OR EXTRACT(MONTH FROM CURRENT_TIMESTAMP) = CASE " +
            "WHEN EXTRACT(MONTH FROM a.finishedAt) = 12 THEN 1 " +
            "ELSE EXTRACT(MONTH FROM a.finishedAt) + 1 END)))")
    List<AnimeEntity> findAnimeEntitiesOldSeasonCurrentlyAiring(Integer year, String season,
                                                                Integer nextYear, String nextSeason,
                                                                String currentlyAiring, String finishedAiring);

    @Query("SELECT a FROM AnimeEntity a " +
            "WHERE a.airStatus = :currentlyAiring OR " +
            "(a.airStatus = :finishedAiring AND " +
            "(EXTRACT(MONTH FROM a.finishedAt) = EXTRACT(MONTH FROM CURRENT_TIMESTAMP) " +
            "OR EXTRACT(MONTH FROM CURRENT_TIMESTAMP) = CASE " +
            "WHEN EXTRACT(MONTH FROM a.finishedAt) = 12 THEN 1 " +
            "ELSE EXTRACT(MONTH FROM a.finishedAt) + 1 END))")
    List<AnimeEntity> findAnimeEntitiesForPollCollection(String currentlyAiring, String finishedAiring);

    List<AnimeEntity> findAllByTitleContaining(String title);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM AnimeEntity a WHERE a.id = :id")
    AnimeEntity findByIdWithLock(@Param("id") Long id);

    List<AnimeEntity> findByForceCollect(String forceCollect);
}