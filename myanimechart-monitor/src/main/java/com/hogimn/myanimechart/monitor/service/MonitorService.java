package com.hogimn.myanimechart.monitor.service;

import com.hogimn.myanimechart.common.alarm.AlarmService;
import com.hogimn.myanimechart.common.util.CronUtil;
import com.hogimn.myanimechart.database.batch.aop.annotation.SaveBatchHistory;
import com.hogimn.myanimechart.database.batch.dto.BatchDto;
import com.hogimn.myanimechart.database.batch.service.BatchHistoryService;
import com.hogimn.myanimechart.database.batch.service.BatchService;
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

    @SaveBatchHistory(value = "#batchJobName", saveDirectly = true)
    @SchedulerLock(name = "checkBatchNotExecuted")
    public void checkBatchNotExecuted(String batchJobName) {
        List<BatchDto> batchDtos = batchService.getAllBatchDtos();
        for (BatchDto batchDto : batchDtos) {
            String cron = batchDto.getCron();
            long period = CronUtil.getPeriodAsSeconds(cron);
            if (!batchHistoryService.checkBatchExecutedWithinPeriod(batchDto.getName(), period)) {
                String errorMessage = String.format("batch not executed within period: %s %s.", batchDto.getName(), period);
                log.error(errorMessage);
                alarmServices.forEach(alarmService -> {
                    if (alarmService.isSupported()) {
                        alarmService.send(errorMessage);
                    }
                });
            } else {
                log.info("batch executed within period: {} {}.", batchDto.getName(), period);
            }
        }
    }
}
