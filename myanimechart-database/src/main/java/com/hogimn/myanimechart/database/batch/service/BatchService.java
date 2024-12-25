package com.hogimn.myanimechart.database.batch.service;

import com.hogimn.myanimechart.database.batch.dao.BatchDao;
import com.hogimn.myanimechart.database.batch.domain.Batch;
import com.hogimn.myanimechart.database.batch.repository.BatchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        throw new IllegalArgumentException("Batch not found (" + name + ")");
    }

    public List<Batch> getAll() {
        return batchRepository.findAll().stream().map(Batch::from).collect(Collectors.toList());
    }
}
