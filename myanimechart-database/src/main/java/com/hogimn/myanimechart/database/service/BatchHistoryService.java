package com.hogimn.myanimechart.database.service;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.dao.BatchHistoryDao;
import com.hogimn.myanimechart.database.domain.BatchHistory;
import com.hogimn.myanimechart.database.repository.BatchHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BatchHistoryService {
    private final BatchHistoryRepository batchHistoryRepository;

    public BatchHistoryService(BatchHistoryRepository batchHistoryRepository) {
        this.batchHistoryRepository = batchHistoryRepository;
    }

    public void saveBatchHistory(String name) {
        batchHistoryRepository.save(
                BatchHistoryDao.from(new BatchHistory(name, DateUtil.now())));
    }
}
