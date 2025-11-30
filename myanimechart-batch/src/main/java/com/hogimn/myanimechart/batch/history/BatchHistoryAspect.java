package com.hogimn.myanimechart.batch.history;

import com.hogimn.myanimechart.core.common.serviceregistry.RegisteredService;
import com.hogimn.myanimechart.core.common.serviceregistry.ServiceRegistryService;
import com.hogimn.myanimechart.core.common.util.SpelUtil;
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
            batchHistoryService.save(batchJobName);
        } else {
            serviceRegistryService.send(RegisteredService.APP, "/batch-histories/save", batchJobName);
        }

        log.info("Start batch job {}", batchJobName);
        Object object = joinPoint.proceed();
        log.info("End batch job {}", batchJobName);
        return object;
    }
}