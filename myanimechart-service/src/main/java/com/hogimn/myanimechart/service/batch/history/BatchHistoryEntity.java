package com.hogimn.myanimechart.service.batch.history;

import com.hogimn.myanimechart.service.batch.BatchResponse;
import com.hogimn.myanimechart.core.common.util.DateUtil;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "batch_history")
@Data
@IdClass(BatchHistoryId.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchHistoryEntity {
    @Id
    private String name;

    @Id
    private LocalDateTime recordedAt;

    public static BatchHistoryEntity from(BatchResponse batchResponse) {
        return BatchHistoryEntity.builder()
                .name(batchResponse.getName())
                .recordedAt(DateUtil.now())
                .build();
    }
}
