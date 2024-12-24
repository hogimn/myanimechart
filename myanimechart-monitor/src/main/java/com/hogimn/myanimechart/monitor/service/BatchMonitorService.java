package com.hogimn.myanimechart.monitor.service;

import com.hogimn.myanimechart.monitor.dao.BatchHistoryDao;
import com.hogimn.myanimechart.monitor.domain.BatchHistory;
import com.hogimn.myanimechart.monitor.repository.BatchHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BatchMonitorService {
    private final BatchHistoryRepository batchHistoryRepository;

    public BatchMonitorService(BatchHistoryRepository batchHistoryRepository) {
        this.batchHistoryRepository = batchHistoryRepository;
    }

    public void saveBatchHistory(BatchHistory batchHistory) {
        batchHistoryRepository.save(BatchHistoryDao.from(batchHistory));
    }
}
