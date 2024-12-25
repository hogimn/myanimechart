package com.hogimn.myanimechart.database.service;

import com.hogimn.myanimechart.database.dao.BatchHistoryDao;
import com.hogimn.myanimechart.database.domain.Batch;
import com.hogimn.myanimechart.database.domain.BatchHistory;
import com.hogimn.myanimechart.database.repository.BatchHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BatchHistoryService {
    private final BatchHistoryRepository batchHistoryRepository;
    private final BatchService batchService;

    public BatchHistoryService(BatchHistoryRepository batchHistoryRepository, BatchService batchService) {
        this.batchHistoryRepository = batchHistoryRepository;
        this.batchService = batchService;
    }

    public void saveBatchHistory(String name) {
        Batch batch = batchService.getBatchByName(name);
        batchHistoryRepository.save(BatchHistoryDao.from(batch));
    }
}
