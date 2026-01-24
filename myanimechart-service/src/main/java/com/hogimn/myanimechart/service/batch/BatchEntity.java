package com.hogimn.myanimechart.service.batch;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "batch")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchEntity {
    @Id
    private String name;

    private String cron;

    public static BatchEntity from(BatchResponse batchResponse) {
        return new BatchEntity(
                batchResponse.name(),
                batchResponse.cron()
        );
    }
}
