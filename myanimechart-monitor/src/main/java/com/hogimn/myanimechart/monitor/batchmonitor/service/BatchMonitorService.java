package com.hogimn.myanimechart.monitor.batchmonitor.service;

import com.hogimn.myanimechart.common.alarm.AlarmService;
import com.hogimn.myanimechart.common.util.CronUtil;
import com.hogimn.myanimechart.common.batch.BatchDto;
import com.hogimn.myanimechart.common.batch.BatchHistoryService;
import com.hogimn.myanimechart.common.batch.BatchService;
import com.hogimn.myanimechart.common.batch.SaveBatchHistory;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BatchMonitorService {
    private final BatchService batchService;
    private final BatchHistoryService batchHistoryService;
    private final List<AlarmService> alarmServices;

    public BatchMonitorService(BatchService batchService,
                               BatchHistoryService batchHistoryService,
                               List<AlarmService> alarmServices) {
        this.batchService = batchService;
        this.batchHistoryService = batchHistoryService;
        this.alarmServices = alarmServices;
    }

    @SaveBatchHistory("#batchJobName")
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
