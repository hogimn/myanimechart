package com.hogimn.myanimechart.execute.anime.batch;

import com.hogimn.myanimechart.common.batch.BatchHistoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
public class BatchController {
    private final BatchHistoryService batchHistoryService;

    public BatchController(BatchHistoryService batchHistoryService) {
        this.batchHistoryService = batchHistoryService;
    }

    @PostMapping("/saveBatchHistory")
    public void saveBatchHistory(@RequestBody String batchJobName) {
        batchHistoryService.saveBatchHistory(batchJobName);
    }
}
