package com.hogimn.myanimechart.service.batch.history;

import com.hogimn.myanimechart.service.batch.BatchResponse;
import com.hogimn.myanimechart.core.common.util.DateUtil;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "batch_history")
@Data
@IdClass(BatchHistoryId.class)
public class BatchHistoryEntity {
    @Id
    private String name;

    @Id
    private LocalDateTime recordedAt;

    public static BatchHistoryEntity from(BatchResponse batchResponse) {
        BatchHistoryEntity batchHistoryEntity = new BatchHistoryEntity();
        batchHistoryEntity.setName(batchResponse.getName());
        batchHistoryEntity.setRecordedAt(DateUtil.now());
        return batchHistoryEntity;
    }
}
