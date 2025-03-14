package com.hogimn.myanimechart.common.poll;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PollCollectionStatusRepository extends JpaRepository<PollCollectionStatusEntity, Long> {
    @Query("""
            SELECT p FROM PollCollectionStatusEntity p
            WHERE p.status = :collectionStatus AND p.finishedAt = :finishedAt
            """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<PollCollectionStatusEntity> findByStatusAndFinishedAtWithLock(
            CollectionStatus collectionStatus, LocalDateTime finishedAt);

    @Query("""
            SELECT p FROM PollCollectionStatusEntity p
             WHERE p.status = :collectionStatus
            """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<PollCollectionStatusEntity> findByStatusWithLock(CollectionStatus collectionStatus);

    @Query("SELECT b FROM PollCollectionStatusEntity b " +
            "WHERE b.animeId IN (" +
            "SELECT a.id FROM AnimeEntity a " +
            "WHERE NOT (" +
            "a.airStatus = :currentlyAiring OR " +
            "(a.airStatus = :finishedAiring AND " +
            "(EXTRACT(MONTH FROM a.endDate) = EXTRACT(MONTH FROM CURRENT_TIMESTAMP) " +
            "OR EXTRACT(MONTH FROM CURRENT_TIMESTAMP) = CASE " +
            "WHEN EXTRACT(MONTH FROM a.endDate) = 12 THEN 1 " +
            "ELSE EXTRACT(MONTH FROM a.endDate) + 1 END))))")
    List<PollCollectionStatusEntity> findUnusedPollCollectionStatus(String currentlyAiring, String finishedAiring);

    @Query("""
            SELECT a
            FROM PollCollectionStatusEntity a
            JOIN AnimeEntity b ON a.animeId = b.id
            ORDER BY
                b.year DESC,
                CASE b.season
                    WHEN 'fall' THEN 1
                    WHEN 'summer' THEN 2
                    WHEN 'spring' THEN 3
                    WHEN 'winter' THEN 4
                    ELSE 5
                END,
                b.score DESC
            """)
    List<PollCollectionStatusEntity> findAllOrderByYearAndSeasonAndScore();
}
