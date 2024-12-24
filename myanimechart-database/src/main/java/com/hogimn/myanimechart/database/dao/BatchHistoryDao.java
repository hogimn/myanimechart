package com.hogimn.myanimechart.database.dao;

import com.hogimn.myanimechart.database.dao.key.BatchHistoryId;
import com.hogimn.myanimechart.database.domain.BatchHistory;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "batch_history")
@Data
@IdClass(BatchHistoryId.class)
public class BatchHistoryDao {
    @Id
    private String name;

    @Id
    private LocalDateTime recordedAt;

    public static BatchHistoryDao from(BatchHistory batchHistory) {
        BatchHistoryDao batchHistoryDao = new BatchHistoryDao();
        batchHistoryDao.setName(batchHistory.getName());
        batchHistoryDao.setRecordedAt(batchHistory.getRecordedAt());
        return batchHistoryDao;
    }
}
