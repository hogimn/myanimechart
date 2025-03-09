package com.hogimn.myanimechart.common.batch;

import com.hogimn.myanimechart.common.util.DateUtil;
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

    public static BatchHistoryEntity from(BatchDto batchDto) {
        BatchHistoryEntity batchHistoryEntity = new BatchHistoryEntity();
        batchHistoryEntity.setName(batchDto.getName());
        batchHistoryEntity.setRecordedAt(DateUtil.now());
        return batchHistoryEntity;
    }
}
