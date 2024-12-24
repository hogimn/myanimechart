package com.hogimn.myanimechart.database.aop.aspect;

import com.hogimn.myanimechart.database.aop.annotation.SaveBatchHistory;
import com.hogimn.myanimechart.database.service.BatchHistoryService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BatchHistoryAspect {
    private final BatchHistoryService batchHistoryService;

    public BatchHistoryAspect(BatchHistoryService batchHistoryService) {
        this.batchHistoryService = batchHistoryService;
    }

    @Before("@annotation(saveBatchHistory)")
    public void saveBatchHistory(JoinPoint joinPoint, SaveBatchHistory saveBatchHistory) {
        String methodName = joinPoint.getSignature().getName();
        batchHistoryService.saveBatchHistory(methodName);
    }
}