package com.hogimn.myanimechart.common.anime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnimeRepository extends JpaRepository<AnimeEntity, Long> {
    List<AnimeEntity> findByYearAndSeason(Integer year, String season);

    @Query("SELECT a FROM AnimeEntity a WHERE NOT (a.year = :year AND a.season = :season) " +
            "AND NOT (a.year = :nextYear AND a.season = :nextSeason) " +
            "AND (a.airStatus = :currentlyAiring OR" +
            " (a.airStatus = :finishedAiring AND " +
            "(EXTRACT(MONTH FROM a.endDate) = EXTRACT(MONTH FROM CURRENT_TIMESTAMP) " +
            "OR EXTRACT(MONTH FROM CURRENT_TIMESTAMP) = CASE " +
            "WHEN EXTRACT(MONTH FROM a.endDate) = 12 THEN 1 " +
            "ELSE EXTRACT(MONTH FROM a.endDate) + 1 END)))")
    List<AnimeEntity> findAnimeEntitiesOldSeasonCurrentlyAiring(Integer year, String season,
                                                                Integer nextYear, String nextSeason,
                                                                String currentlyAiring, String finishedAiring);

    @Query("SELECT a FROM AnimeEntity a " +
            "WHERE a.airStatus = :currentlyAiring OR " +
            "(a.airStatus = :finishedAiring AND " +
            "(EXTRACT(MONTH FROM a.endDate) = EXTRACT(MONTH FROM CURRENT_TIMESTAMP) " +
            "OR EXTRACT(MONTH FROM CURRENT_TIMESTAMP) = CASE " +
            "WHEN EXTRACT(MONTH FROM a.endDate) = 12 THEN 1 " +
            "ELSE EXTRACT(MONTH FROM a.endDate) + 1 END))")
    List<AnimeEntity> findAnimeEntitiesAllSeasonCurrentlyAiring(String currentlyAiring, String finishedAiring);

    List<AnimeEntity> findAllByTitleContaining(String title);

    List<AnimeEntity> findByForceCollect(String forceCollect);
}