package com.hogimn.myanimechart.common.batch;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BatchHistoryRepository extends JpaRepository<BatchHistoryEntity, BatchHistoryId> {
    List<BatchHistoryEntity> findByNameAndRecordedAtBetween(String name, LocalDateTime before, LocalDateTime now);
}
