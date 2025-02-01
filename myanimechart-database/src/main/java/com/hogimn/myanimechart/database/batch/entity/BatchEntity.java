package com.hogimn.myanimechart.database.batch.entity;

import com.hogimn.myanimechart.database.batch.dto.BatchDto;
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

    public static BatchEntity from(BatchDto batchDto) {
        BatchEntity batchEntity = new BatchEntity();
        batchEntity.setName(batchDto.getName());
        batchEntity.setCron(batchDto.getCron());
        return batchEntity;
    }
}
