package com.hogimn.myanimechart.service.poll.status;

import com.hogimn.myanimechart.core.common.result.SaveResult;
import com.hogimn.myanimechart.core.domain.poll.collectionstatus.CollectionStatus;
import com.hogimn.myanimechart.core.domain.poll.collectionstatus.PollCollectionStatusEntity;
import com.hogimn.myanimechart.core.domain.poll.collectionstatus.PollCollectionStatusRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PollCollectionStatusServiceTest {

    @Mock
    private PollCollectionStatusRepository pollCollectionStatusRepository;

    @InjectMocks
    private PollCollectionStatusService pollCollectionStatusService;

    @Test
    void save_createsWhenMissing() {
        when(pollCollectionStatusRepository.findById(7L)).thenReturn(Optional.empty());
        LocalDateTime updated = LocalDateTime.of(2024, 6, 1, 12, 0);
        PollCollectionStatusRequest req = new PollCollectionStatusRequest(
                7L, CollectionStatus.WAIT, null, null, updated);

        SaveResult result = pollCollectionStatusService.save(req);

        assertThat(result).isEqualTo(SaveResult.CREATED);
        ArgumentCaptor<PollCollectionStatusEntity> captor = ArgumentCaptor.forClass(PollCollectionStatusEntity.class);
        verify(pollCollectionStatusRepository).save(captor.capture());
        assertThat(captor.getValue().getAnimeId()).isEqualTo(7L);
        assertThat(captor.getValue().getStatus()).isEqualTo(CollectionStatus.WAIT);
        assertThat(captor.getValue().getUpdatedAt()).isEqualTo(updated);
    }

    @Test
    void save_updatesWhenPresent() {
        PollCollectionStatusEntity existing = PollCollectionStatusEntity.builder()
                .animeId(1L)
                .status(CollectionStatus.WAIT)
                .updatedAt(LocalDateTime.MIN)
                .build();
        when(pollCollectionStatusRepository.findById(1L)).thenReturn(Optional.of(existing));
        LocalDateTime updated = LocalDateTime.of(2025, 1, 1, 0, 0);
        PollCollectionStatusRequest req = new PollCollectionStatusRequest(
                1L, CollectionStatus.COMPLETED, null, LocalDateTime.of(2025, 1, 2, 0, 0), updated);

        SaveResult result = pollCollectionStatusService.save(req);

        assertThat(result).isEqualTo(SaveResult.UPDATED);
        assertThat(existing.getStatus()).isEqualTo(CollectionStatus.COMPLETED);
        assertThat(existing.getFinishedAt()).isEqualTo(LocalDateTime.of(2025, 1, 2, 0, 0));
        assertThat(existing.getUpdatedAt()).isEqualTo(updated);
    }
}
