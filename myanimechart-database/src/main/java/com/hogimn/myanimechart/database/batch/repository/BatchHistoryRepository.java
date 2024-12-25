package com.hogimn.myanimechart.database.batch.repository;

import com.hogimn.myanimechart.database.batch.dao.BatchDao;
import com.hogimn.myanimechart.database.batch.dao.BatchHistoryDao;
import com.hogimn.myanimechart.database.batch.dao.key.BatchHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BatchHistoryRepository extends JpaRepository<BatchHistoryDao, BatchHistoryId> {
    List<BatchHistoryDao> findByBatchAndRecordedAtBetween(BatchDao batchDao, LocalDateTime before, LocalDateTime now);
}
