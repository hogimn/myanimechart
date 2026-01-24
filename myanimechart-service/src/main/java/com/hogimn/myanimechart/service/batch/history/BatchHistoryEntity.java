package com.hogimn.myanimechart.service.batch.history;

import com.hogimn.myanimechart.core.common.util.DateUtil;
import com.hogimn.myanimechart.service.batch.BatchResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "batch_history")
@IdClass(BatchHistoryId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchHistoryEntity {
    @Id
    private String name;

    @Id
    private LocalDateTime recordedAt;

    public static BatchHistoryEntity from(BatchResponse batchResponse) {
        return new BatchHistoryEntity(
                batchResponse.name(),
                DateUtil.now()
        );
    }
}
