package com.hogimn.myanimechart.database.aop.aspect;

import com.hogimn.myanimechart.common.domain.DiscoveryService;
import com.hogimn.myanimechart.common.service.ServiceRegistryService;
import com.hogimn.myanimechart.common.util.SpelUtil;
import com.hogimn.myanimechart.database.aop.annotation.SaveBatchHistory;
import com.hogimn.myanimechart.database.service.BatchHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class BatchHistoryAspect {
    private final ServiceRegistryService serviceRegistryService;
    private final BatchHistoryService batchHistoryService;

    public BatchHistoryAspect(ServiceRegistryService serviceRegistryService,
                              BatchHistoryService batchHistoryService) {
        this.serviceRegistryService = serviceRegistryService;
        this.batchHistoryService = batchHistoryService;
    }

    @Around("@annotation(saveBatchHistory)")
    public Object saveBatchHistory(ProceedingJoinPoint joinPoint, SaveBatchHistory saveBatchHistory) throws Throwable {
        String batchJobName = SpelUtil.resolveSpelExpression(joinPoint, saveBatchHistory.value());

        if (saveBatchHistory.saveDirectly()) {
            batchHistoryService.saveBatchHistory(batchJobName);
        } else {
            serviceRegistryService.send(DiscoveryService.MONITOR, "/saveBatchHistory", batchJobName);
        }

        log.info("Start batch job {}", batchJobName);
        Object object = joinPoint.proceed();
        log.info("End batch job {}", batchJobName);
        return object;
    }
}