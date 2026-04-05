package com.hogimn.myanimechart.service.batch;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BatchServiceTest {

    @Mock
    private BatchRepository batchRepository;

    @InjectMocks
    private BatchService batchService;

    @Test
    void findBatchByName_throwsWhenMissing() {
        when(batchRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> batchService.findBatchByName("missing"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Batch not found");
    }

    @Test
    void getAll_mapsEntities() {
        BatchEntity e = BatchEntity.builder().name("job1").cron("0 0 * * * *").build();
        when(batchRepository.findAll()).thenReturn(List.of(e));

        List<BatchResponse> all = batchService.getAll();

        assertThat(all).hasSize(1);
        assertThat(all.getFirst().name()).isEqualTo("job1");
    }
}
