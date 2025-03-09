package com.hogimn.myanimechart.common.batch;

import com.hogimn.myanimechart.common.util.DateUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class BatchHistoryService {
    private final BatchHistoryRepository batchHistoryRepository;
    private final BatchService batchService;

    public BatchHistoryService(BatchHistoryRepository batchHistoryRepository, BatchService batchService) {
        this.batchHistoryRepository = batchHistoryRepository;
        this.batchService = batchService;
    }

    @Transactional
    public void save(String name) {
        BatchDto batchDto = batchService.findBatchDtoByName(name);
        batchHistoryRepository.save(BatchHistoryEntity.from(batchDto));
    }

    public boolean checkBatchExecutedWithinPeriod(String name, long seconds) {
        LocalDateTime now = DateUtil.now();
        LocalDateTime beforePeriod = now.minusSeconds(seconds + seconds / 6);
        List<BatchHistoryEntity> batchHistoryEntityList = batchHistoryRepository
                .findByNameAndRecordedAtBetween(name, beforePeriod, now);

        return !batchHistoryEntityList.isEmpty();
    }
}
