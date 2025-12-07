package com.hogimn.myanimechart.service.batch.history;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch-histories")
@RequiredArgsConstructor
public class BatchHistoryController {
    private final BatchHistoryService batchHistoryService;

    @PostMapping
    public ResponseEntity<Void> saveBatchHistory(@RequestBody String batchJobName) {
        batchHistoryService.save(batchJobName);
        return ResponseEntity.noContent().build();
    }
}
