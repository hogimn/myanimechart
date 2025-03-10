package com.hogimn.myanimechart.common.poll;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PollCollectionStatusRepository extends JpaRepository<PollCollectionStatusEntity, Long> {
    List<PollCollectionStatusEntity> findByStatusAndFinishedAt(
            CollectionStatus collectionStatus, LocalDateTime finishedAt);

    List<PollCollectionStatusEntity> findByStatus(CollectionStatus collectionStatus);

    @Query("SELECT b FROM PollCollectionStatus b " +
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
}
