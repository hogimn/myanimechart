package com.hogimn.myanimechart.monitor.service;

import com.hogimn.myanimechart.common.alarm.AlarmService;
import com.hogimn.myanimechart.common.util.CronUtil;
import com.hogimn.myanimechart.database.batch.aop.annotation.SaveBatchHistory;
import com.hogimn.myanimechart.database.batch.domain.Batch;
import com.hogimn.myanimechart.database.batch.service.BatchHistoryService;
import com.hogimn.myanimechart.database.batch.service.BatchService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MonitorService {
    private final BatchService batchService;
    private final BatchHistoryService batchHistoryService;
    private final List<AlarmService> alarmServices;

    public MonitorService(BatchService batchService,
                          BatchHistoryService batchHistoryService,
                          List<AlarmService> alarmServices) {
        this.batchService = batchService;
        this.batchHistoryService = batchHistoryService;
        this.alarmServices = alarmServices;
    }

    @Transactional
    @SaveBatchHistory(value = "#batchJobName", saveDirectly = true)
    @SchedulerLock(name = "checkBatchNotExecuted")
    public void checkBatchNotExecuted(String batchJobName) {
        List<Batch> batches = batchService.getAll();
        for (Batch batch : batches) {
            String cron = batch.getCron();
            long period = CronUtil.getPeriodAsSeconds(cron);
            if (!batchHistoryService.checkBatchExecutedWithinPeriod(batch.getName(), period)) {
                String errorMessage = String.format("batch not executed within period: %s %s.", batch.getName(), period);
                log.error(errorMessage);
                alarmServices.forEach(alarmService -> {
                    if (alarmService.isSupported()) {
                        alarmService.send(errorMessage);
                    }
                });
            } else {
                log.info("batch executed within period: {} {}.", batch.getName(), period);
            }
        }
    }
}
