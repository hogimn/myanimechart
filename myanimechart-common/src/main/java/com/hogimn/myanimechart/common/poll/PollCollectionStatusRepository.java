package com.hogimn.myanimechart.common.poll;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PollCollectionStatusRepository extends JpaRepository<PollCollectionStatusEntity, Long> {
    List<PollCollectionStatusEntity> findByStatusAndFinishedAt(
            CollectionStatus collectionStatus, LocalDateTime finishedAt);

    List<PollCollectionStatusEntity> findByStatus(CollectionStatus collectionStatus);
}
