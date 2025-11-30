package com.hogimn.myanimechart.batch.monitor;

import com.hogimn.myanimechart.batch.core.BatchDto;
import com.hogimn.myanimechart.batch.history.BatchHistoryService;
import com.hogimn.myanimechart.batch.core.BatchService;
import com.hogimn.myanimechart.batch.history.SaveBatchHistory;
import com.hogimn.myanimechart.core.common.alarm.AlarmService;
import com.hogimn.myanimechart.core.common.util.CronUtil;
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
    @SchedulerLock(name = "checkNotExecutedBatches")
    public void checkNotExecutedBatches(String batchJobName) {
        List<BatchDto> batchDtos = batchService.findAllBatchDtos();

        for (BatchDto batchDto : batchDtos) {
            validateBatchExecution(batchDto);
        }
    }

    private void validateBatchExecution(BatchDto batchDto) {
        String cron = batchDto.getCron();
        long period = CronUtil.getPeriodAsSeconds(cron);

        if (!batchHistoryService.checkBatchExecutedWithinPeriod(batchDto.getName(), period)) {
            String errorMessage = String.format(
                    "batch not executed within period: %s %s.",
                    batchDto.getName(), period
            );
            log.error(errorMessage);
            sendAlarm(errorMessage);
        } else {
            log.info(
                    "batch executed within period: {} {}.",
                    batchDto.getName(), period
            );
        }
    }

    private void sendAlarm(String message) {
        alarmServices.stream()
                .filter(AlarmService::isSupported)
                .forEach(service -> service.send(message));
    }
}
