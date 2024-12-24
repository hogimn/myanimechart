package com.hogimn.myanimechart.database.repository;

import com.hogimn.myanimechart.database.dao.BatchHistoryDao;
import com.hogimn.myanimechart.database.dao.key.BatchHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchHistoryRepository extends JpaRepository<BatchHistoryDao, BatchHistoryId> {
}
