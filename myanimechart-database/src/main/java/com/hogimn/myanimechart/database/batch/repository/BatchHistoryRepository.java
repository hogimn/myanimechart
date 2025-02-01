package com.hogimn.myanimechart.database.batch.repository;

import com.hogimn.myanimechart.database.batch.entity.BatchEntity;
import com.hogimn.myanimechart.database.batch.entity.BatchHistoryEntity;
import com.hogimn.myanimechart.database.batch.entity.key.BatchHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BatchHistoryRepository extends JpaRepository<BatchHistoryEntity, BatchHistoryId> {
    List<BatchHistoryEntity> findByBatchAndRecordedAtBetween(BatchEntity batchEntity, LocalDateTime before, LocalDateTime now);
}
