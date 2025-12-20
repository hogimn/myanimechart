package com.hogimn.myanimechart.service.batch.history;

import com.hogimn.myanimechart.service.batch.BatchResponse;
import com.hogimn.myanimechart.service.batch.BatchService;
import com.hogimn.myanimechart.core.common.util.DateUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BatchHistoryService {
    private final BatchHistoryRepository batchHistoryRepository;
    private final BatchService batchService;
    private static final String ALARM_SUFFIX = ":ALARM";

    @Transactional
    public void save(String name) {
        BatchResponse batchResponse = batchService.findBatchByName(name);
        batchHistoryRepository.save(BatchHistoryEntity.from(batchResponse));
    }

    @Transactional
    public void saveAlarmHistory(String name) {
        BatchHistoryEntity alarmEntity = BatchHistoryEntity.builder()
                .name(name + ALARM_SUFFIX)
                .recordedAt(DateUtil.now())
                .build();
        batchHistoryRepository.save(alarmEntity);
    }

    public boolean checkBatchExecutedWithinPeriod(String name, long seconds) {
        LocalDateTime now = DateUtil.now();
        LocalDateTime beforePeriod = now.minusSeconds(seconds + seconds / 6);
        List<BatchHistoryEntity> batchHistoryEntityList = batchHistoryRepository
                .findByNameAndRecordedAtBetween(name, beforePeriod, now);

        return !batchHistoryEntityList.isEmpty();
    }

    public boolean checkAlarmSentWithinPeriod(String name, long seconds) {
        return checkBatchExecutedWithinPeriod(name + ALARM_SUFFIX, seconds);
    }
}