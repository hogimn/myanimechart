package com.hogimn.myanimechart.database.repository;

import com.hogimn.myanimechart.database.dao.BatchDao;
import com.hogimn.myanimechart.database.dao.BatchHistoryDao;
import com.hogimn.myanimechart.database.dao.key.BatchHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BatchHistoryRepository extends JpaRepository<BatchHistoryDao, BatchHistoryId> {
    List<BatchHistoryDao> findByBatchAndRecordedAtBetween(BatchDao batchDao, LocalDateTime before, LocalDateTime now);
}
