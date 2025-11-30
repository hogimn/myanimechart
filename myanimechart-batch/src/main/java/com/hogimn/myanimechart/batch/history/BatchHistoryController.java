package com.hogimn.myanimechart.batch.history;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch-histories")
public class BatchHistoryController {
    private final BatchHistoryService batchHistoryService;

    public BatchHistoryController(BatchHistoryService batchHistoryService) {
        this.batchHistoryService = batchHistoryService;
    }

    @PostMapping
    public void saveBatchHistory(@RequestBody String batchJobName) {
        batchHistoryService.save(batchJobName);
    }
}
