package com.hogimn.myanimechart.service.batch.monitor;

import com.hogimn.myanimechart.service.batch.BatchResponse;
import com.hogimn.myanimechart.service.batch.history.BatchHistoryService;
import com.hogimn.myanimechart.service.batch.BatchService;
import com.hogimn.myanimechart.service.batch.history.SaveBatchHistory;
import com.hogimn.myanimechart.core.common.alarm.AlarmService;
import com.hogimn.myanimechart.core.common.util.CronUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BatchMonitorService {
    private final BatchService batchService;
    private final BatchHistoryService batchHistoryService;
    private final List<AlarmService> alarmServices;

    @SaveBatchHistory("#batchJobName")
    @SchedulerLock(name = "checkNotExecutedBatches")
    public void checkNotExecutedBatches(String batchJobName) {
        List<BatchResponse> batchResponses = batchService.getAll();

        for (BatchResponse batchResponse : batchResponses) {
            validateBatchExecution(batchResponse);
        }
    }

    private void validateBatchExecution(BatchResponse batchResponse) {
        String cron = batchResponse.getCron();
        long period = CronUtil.getPeriodAsSeconds(cron);

        if (!batchHistoryService.checkBatchExecutedWithinPeriod(batchResponse.getName(), period)) {
            String errorMessage = String.format(
                    "batch not executed within period: %s %s.",
                    batchResponse.getName(), period
            );
            log.error(errorMessage);
            sendAlarm(errorMessage);
        } else {
            log.info(
                    "batch executed within period: {} {}.",
                    batchResponse.getName(), period
            );
        }
    }

    private void sendAlarm(String message) {
        alarmServices.stream()
                .filter(AlarmService::isSupported)
                .forEach(service -> service.send(message));
    }
}
