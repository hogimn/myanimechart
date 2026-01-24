package com.hogimn.myanimechart.service.batch;

public record BatchResponse(String name, String cron) {
    public static BatchResponse from(BatchEntity entity) {
        return new BatchResponse(entity.getName(), entity.getCron());
    }
}
