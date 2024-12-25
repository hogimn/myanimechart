package com.hogimn.myanimechart.database.dao;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.dao.key.BatchHistoryId;
import com.hogimn.myanimechart.database.domain.Batch;
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
    @ManyToOne
    @JoinColumn(name = "name", referencedColumnName = "name")
    private BatchDao batch;

    @Id
    private LocalDateTime recordedAt;

    public static BatchHistoryDao from(Batch batch) {
        BatchHistoryDao batchHistoryDao = new BatchHistoryDao();
        batchHistoryDao.setBatch(BatchDao.from(batch));
        batchHistoryDao.setRecordedAt(DateUtil.now());
        return batchHistoryDao;
    }
}
