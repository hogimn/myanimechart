package com.hogimn.myanimechart.service.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BatchService {
    private final BatchRepository batchRepository;

    public BatchResponse findBatchByName(String name) {
        Optional<BatchEntity> optional = batchRepository.findById(name);
        if (optional.isPresent()) {
            return BatchResponse.from(optional.get());
        }
        throw new IllegalArgumentException("Batch not found (" + name + ")");
    }

    public List<BatchResponse> getAll() {
        return batchRepository.findAll()
                .stream()
                .map(BatchResponse::from)
                .collect(Collectors.toList());
    }
}
