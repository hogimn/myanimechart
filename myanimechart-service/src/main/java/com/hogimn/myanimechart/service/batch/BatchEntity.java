package com.hogimn.myanimechart.service.batch;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "batch")
@Data
public class BatchEntity {
    @Id
    private String name;

    private String cron;

    public static BatchEntity from(BatchResponse batchResponse) {
        BatchEntity batchEntity = new BatchEntity();
        batchEntity.setName(batchResponse.getName());
        batchEntity.setCron(batchResponse.getCron());
        return batchEntity;
    }
}
