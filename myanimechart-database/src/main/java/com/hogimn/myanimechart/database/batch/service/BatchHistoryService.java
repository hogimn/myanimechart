package com.hogimn.myanimechart.database.batch.service;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.batch.dao.BatchDao;
import com.hogimn.myanimechart.database.batch.dao.BatchHistoryDao;
import com.hogimn.myanimechart.database.batch.dto.BatchDto;
import com.hogimn.myanimechart.database.batch.repository.BatchHistoryRepository;
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
    public void saveBatchHistory(String name) {
        BatchDto batchDto = batchService.getBatchDtoByName(name);
        batchHistoryRepository.save(BatchHistoryDao.from(batchDto));
    }

    public boolean checkBatchExecutedWithinPeriod(String name, long seconds) {
        LocalDateTime now = DateUtil.now();
        LocalDateTime beforePeriod = now.minusSeconds(seconds + seconds / 6);
        BatchDto batchDto = batchService.getBatchDtoByName(name);
        List<BatchHistoryDao> batchHistoryDaoList = batchHistoryRepository
                .findByBatchAndRecordedAtBetween(BatchDao.from(batchDto), beforePeriod, now);

        return !batchHistoryDaoList.isEmpty();
    }
}
