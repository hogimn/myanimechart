package com.hogimn.myanimechart.database.batch.service;

import com.hogimn.myanimechart.database.batch.entity.BatchEntity;
import com.hogimn.myanimechart.database.batch.dto.BatchDto;
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

    public BatchDto getBatchDtoByName(String name) {
        Optional<BatchEntity> optional = batchRepository.findById(name);
        if (optional.isPresent()) {
            return BatchDto.from(optional.get());
        }
        throw new IllegalArgumentException("Batch not found (" + name + ")");
    }

    public List<BatchDto> getAllBatchDtos() {
        return batchRepository.findAll().stream().map(BatchDto::from).collect(Collectors.toList());
    }
}
