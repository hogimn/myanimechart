package com.hogimn.myanimechart.monitor.service;

import com.hogimn.myanimechart.common.util.CronUtil;
import com.hogimn.myanimechart.database.aop.annotation.SaveBatchHistory;
import com.hogimn.myanimechart.database.domain.Batch;
import com.hogimn.myanimechart.database.service.BatchHistoryService;
import com.hogimn.myanimechart.database.service.BatchService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MonitorService {
    private final BatchService batchService;
    private final BatchHistoryService
            batchHistoryService;

    public MonitorService(BatchService batchService, BatchHistoryService batchHistoryService) {
        this.batchService = batchService;
        this.batchHistoryService = batchHistoryService;
    }

    @Transactional
    public void checkBatchNotExecuted(String batchJobName) {
        List<Batch> batches = batchService.getAll();
        for (Batch batch : batches) {
            String cron = batch.getCron();
            long period = CronUtil.getPeriodAsSeconds(cron);
            if (!batchHistoryService.checkBatchExecutedWithinPeriod(batch.getName(), period)) {
                // TODO: alarm service via email
                log.error("batch not executed within period: {} {}.", batch.getName(), period);
            }
            else {
                log.info("batch executed within period: {} {}.", batch.getName(), period);
            }
        }
    }
}
