package com.hogimn.myanimechart.service.batch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchResponse {
    private String name;
    private String cron;

    public static BatchResponse from(BatchEntity batchEntity) {
        BatchResponse batchResponse = new BatchResponse();
        batchResponse.setName(batchEntity.getName());
        batchResponse.setCron(batchEntity.getCron());
        return batchResponse;
    }
}
