package com.hogimn.myanimechart.database.aop.aspect;

import com.hogimn.myanimechart.common.domain.DiscoveryService;
import com.hogimn.myanimechart.common.service.ServiceRegistryService;
import com.hogimn.myanimechart.common.util.SpelUtil;
import com.hogimn.myanimechart.database.aop.annotation.SaveBatchHistory;
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

    public BatchHistoryAspect(ServiceRegistryService serviceRegistryService) {
        this.serviceRegistryService = serviceRegistryService;
    }

    @Around("@annotation(saveBatchHistory)")
    public Object saveBatchHistory(ProceedingJoinPoint joinPoint, SaveBatchHistory saveBatchHistory) throws Throwable {
        String batchJobName = SpelUtil.resolveSpelExpression(joinPoint, saveBatchHistory.value());
        serviceRegistryService.send(DiscoveryService.MONITOR, "/saveBatchHistory", batchJobName);

        log.info("Start batch job {}", batchJobName);
        Object object = joinPoint.proceed();
        log.info("End batch job {}", batchJobName);
        return object;
    }
}