package com.hogimn.myanimechart.common.collect;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PollCollectionStatusRepository extends JpaRepository<PollCollectionStatusEntity, Long> {
    List<PollCollectionStatusEntity> findByStatusAndFinishedAt(
            CollectionStatus collectionStatus, LocalDateTime finishedAt);
}
