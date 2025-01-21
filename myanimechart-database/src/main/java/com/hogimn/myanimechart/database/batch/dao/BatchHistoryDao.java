package com.hogimn.myanimechart.database.batch.dao;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.batch.dao.key.BatchHistoryId;
import com.hogimn.myanimechart.database.batch.domain.Batch;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
