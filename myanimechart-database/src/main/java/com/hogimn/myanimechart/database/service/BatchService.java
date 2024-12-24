package com.hogimn.myanimechart.database.service;

import com.hogimn.myanimechart.database.dao.BatchDao;
import com.hogimn.myanimechart.database.domain.Batch;
import com.hogimn.myanimechart.database.repository.BatchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class BatchService {
    private final BatchRepository batchRepository;

    public BatchService(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    public Batch getBatchByName(String name) {
        Optional<BatchDao> optional = batchRepository.findById(name);
        if (optional.isPresent()) {
            return Batch.from(optional.get());
        }
        throw new IllegalArgumentException("Batch not found");
    }
}
