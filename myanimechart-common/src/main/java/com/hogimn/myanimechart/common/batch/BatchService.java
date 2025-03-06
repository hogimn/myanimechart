package com.hogimn.myanimechart.common.batch;

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
