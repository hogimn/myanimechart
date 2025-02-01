package com.hogimn.myanimechart.database.batch.dto;

import com.hogimn.myanimechart.database.batch.entity.BatchEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchDto {
    private String name;
    private String cron;

    public static BatchDto from(BatchEntity batchEntity) {
        BatchDto batchDto = new BatchDto();
        batchDto.setName(batchEntity.getName());
        batchDto.setCron(batchEntity.getCron());
        return batchDto;
    }
}
