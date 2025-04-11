package com.hogimn.myanimechart.common.anime;

import com.hogimn.myanimechart.common.poll.CollectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnimeRepository extends JpaRepository<AnimeEntity, Long> {
    @Query("""
             SELECT a, b
             FROM AnimeEntity a
               LEFT JOIN PollEntity b ON a.id = b.animeId
             WHERE
               a.year = :year AND a.season = :season
             ORDER BY
               a.id DESC,
               b.episode,
               b.topicId,
               b.pollOptionId
            """)
    List<Object[]> findWithPollsByYearAndSeason(Integer year, String season);

    List<AnimeEntity> findByYearAndSeasonOrderByScoreDesc(Integer year, String season);

    @Query("""
             SELECT a
             FROM AnimeEntity a
               LEFT JOIN PollCollectionStatusEntity b ON a.id = b.animeId
             WHERE
               a.year = :year AND a.season = :season
               AND b.status = :status
             ORDER BY
               a.score DESC
            """)
    List<AnimeEntity> findByYearAndSeasonAndCollectStatusFailedOrderByScoreDesc(
            Integer year, String season, CollectionStatus status);

    @Query("""
            SELECT a
            FROM AnimeEntity a
            WHERE
              NOT (a.year = :year AND a.season = :season)
              AND NOT (a.year = :nextYear AND a.season = :nextSeason)
              AND (
                a.airStatus = :currentlyAiring
                OR (
                  a.airStatus = :finishedAiring
                  AND (
                    (
                      EXTRACT(YEAR FROM a.endDate) = EXTRACT(YEAR FROM CURRENT_TIMESTAMP)
                      AND EXTRACT(MONTH FROM a.endDate) = EXTRACT(MONTH FROM CURRENT_TIMESTAMP)
                    )
                    OR
                    (
                      EXTRACT(YEAR FROM a.endDate) = CASE
                        WHEN EXTRACT(MONTH FROM a.endDate) = 12 THEN EXTRACT(YEAR FROM CURRENT_TIMESTAMP) + 1
                        ELSE EXTRACT(YEAR FROM CURRENT_TIMESTAMP)
                        END
                      AND EXTRACT(MONTH FROM CURRENT_TIMESTAMP) = CASE
                        WHEN EXTRACT(MONTH FROM a.endDate) = 12 THEN 1
                        ELSE EXTRACT(MONTH FROM a.endDate) + 1
                        END
                    )
                  )
                )
              )
            """)
    List<AnimeEntity> findAnimeEntitiesOldSeasonCurrentlyAiring(Integer year, String season,
                                                                Integer nextYear, String nextSeason,
                                                                String currentlyAiring, String finishedAiring);

    @Query("""
            SELECT a
            FROM AnimeEntity a
            WHERE
              a.airStatus = :currentlyAiring
              OR (
                a.airStatus = :finishedAiring
                AND (
                  (
                    EXTRACT(YEAR FROM a.endDate) = EXTRACT(YEAR FROM CURRENT_TIMESTAMP)
                    AND EXTRACT(MONTH FROM a.endDate) = EXTRACT(MONTH FROM CURRENT_TIMESTAMP)
                  )
                  OR
                  (
                    EXTRACT(YEAR FROM a.endDate) = CASE
                      WHEN EXTRACT(MONTH FROM a.endDate) = 12 THEN EXTRACT(YEAR FROM CURRENT_TIMESTAMP) + 1
                      ELSE EXTRACT(YEAR FROM CURRENT_TIMESTAMP)
                      END
                    AND EXTRACT(MONTH FROM CURRENT_TIMESTAMP) = CASE
                      WHEN EXTRACT(MONTH FROM a.endDate) = 12 THEN 1
                      ELSE EXTRACT(MONTH FROM a.endDate) + 1
                      END
                  )
                )
              )
            ORDER BY
              a.year DESC,
              CASE a.season
                WHEN 'fall' THEN 1
                WHEN 'summer' THEN 2
                WHEN 'spring' THEN 3
                WHEN 'winter' THEN 4
                ELSE 5
              END,
              a.score DESC,
              a.rank
            """)
    List<AnimeEntity> findAnimeEntitiesAllSeasonCurrentlyAiring(String currentlyAiring, String finishedAiring);

    @Query("""
            SELECT a, b
            FROM AnimeEntity a
              LEFT JOIN PollEntity b ON a.id = b.animeId
            WHERE
              a.title LIKE CONCAT('%', :title, '%')
            ORDER BY
              a.id DESC,
              b.episode,
              b.topicId,
              b.pollOptionId
            """)
    List<Object[]> findAllWithPollsByTitleContaining(String title);

    List<AnimeEntity> findByForceCollect(String forceCollect);

    @Query("""
             SELECT a
             FROM AnimeEntity a
               LEFT JOIN PollCollectionStatusEntity b ON a.id = b.animeId
             WHERE
               AND b.status = :status
             ORDER BY
               a.score DESC
            """)
    List<AnimeEntity> findAnimesByCollectionStatus(CollectionStatus status);
}