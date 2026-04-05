package com.hogimn.myanimechart.service.batch.history;

import com.hogimn.myanimechart.service.batch.BatchResponse;
import com.hogimn.myanimechart.service.batch.BatchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BatchHistoryServiceTest {

    @Mock
    private BatchHistoryRepository batchHistoryRepository;

    @Mock
    private BatchService batchService;

    @InjectMocks
    private BatchHistoryService batchHistoryService;

    @Test
    void save_rejectsBlankName() {
        assertThatThrownBy(() -> batchHistoryService.save("  "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Batch job name");

        verify(batchHistoryRepository, never()).save(any());
    }

    @Test
    void save_delegatesWhenNameValid() {
        when(batchService.findBatchByName("AnimeCollectJob"))
                .thenReturn(new BatchResponse("AnimeCollectJob", "0 0 * * * *"));

        batchHistoryService.save("AnimeCollectJob");

        verify(batchHistoryRepository).save(any(BatchHistoryEntity.class));
    }
}
