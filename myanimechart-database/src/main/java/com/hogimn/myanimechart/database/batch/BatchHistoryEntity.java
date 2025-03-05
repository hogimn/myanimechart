package com.hogimn.myanimechart.database.batch;

import com.hogimn.myanimechart.common.util.DateUtil;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "batch_history")
@Data
@IdClass(BatchHistoryId.class)
public class BatchHistoryEntity {
    @Id
    @ManyToOne
    @JoinColumn(name = "name", referencedColumnName = "name")
    private BatchEntity batch;

    @Id
    private LocalDateTime recordedAt;

    public static BatchHistoryEntity from(BatchDto batchDto) {
        BatchHistoryEntity batchHistoryEntity = new BatchHistoryEntity();
        batchHistoryEntity.setBatch(BatchEntity.from(batchDto));
        batchHistoryEntity.setRecordedAt(DateUtil.now());
        return batchHistoryEntity;
    }
}
