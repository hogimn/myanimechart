package com.hogimn.myanimechart.common.poll;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PollCollectionStatusRepository extends JpaRepository<PollCollectionStatusEntity, Long> {
    @Query("""
            SELECT p FROM PollCollectionStatusEntity p
            WHERE p.status = :collectionStatus AND p.finishedAt IS NULL
            """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<PollCollectionStatusEntity> findByStatusAndFinishedAtIsNullWithLock(CollectionStatus collectionStatus);

    @Query("""
            SELECT p FROM PollCollectionStatusEntity p
            WHERE p.status = :collectionStatus
            """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<PollCollectionStatusEntity> findByStatusWithLock(CollectionStatus collectionStatus);

    @Query("""
            SELECT a, b
            FROM PollCollectionStatusEntity a
              LEFT JOIN AnimeEntity b ON a.animeId = b.id
            ORDER BY
              b.year DESC,
              CASE b.season
                WHEN 'fall' THEN 1
                WHEN 'summer' THEN 2
                WHEN 'spring' THEN 3
                WHEN 'winter' THEN 4
                ELSE 5
              END,
              b.score DESC,
              b.rank
            """)
    List<Object[]> findAllOrderByYearAndSeasonAndScore();
}
