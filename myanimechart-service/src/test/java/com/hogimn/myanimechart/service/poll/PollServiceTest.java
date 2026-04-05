package com.hogimn.myanimechart.service.poll;

import com.hogimn.myanimechart.core.common.result.SaveResult;
import com.hogimn.myanimechart.core.domain.poll.PollEntity;
import com.hogimn.myanimechart.core.domain.poll.PollRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PollServiceTest {

    @Mock
    private PollRepository pollRepository;

    @InjectMocks
    private PollService pollService;

    @Test
    void save_createsWhenMissing() {
        when(pollRepository.findByAnimeIdAndPollOptionIdAndTopicId(1L, 2, 3L)).thenReturn(Optional.empty());

        SaveResult result = pollService.save(sampleRequest());

        assertThat(result).isEqualTo(SaveResult.CREATED);
        ArgumentCaptor<PollEntity> captor = ArgumentCaptor.forClass(PollEntity.class);
        verify(pollRepository).save(captor.capture());
        PollEntity saved = captor.getValue();
        assertThat(saved.getAnimeId()).isEqualTo(1L);
        assertThat(saved.getPollOptionId()).isEqualTo(2);
        assertThat(saved.getTopicId()).isEqualTo(3L);
        assertThat(saved.getTitle()).isEqualTo("Topic");
        assertThat(saved.getVotes()).isEqualTo(10);
        assertThat(saved.getEpisode()).isEqualTo(5);
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void save_updatesWhenPresent() {
        PollEntity existing = PollEntity.builder()
                .animeId(1L)
                .pollOptionId(2)
                .topicId(3L)
                .title("Old")
                .votes(1)
                .episode(5)
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now().minusDays(1))
                .build();
        when(pollRepository.findByAnimeIdAndPollOptionIdAndTopicId(1L, 2, 3L)).thenReturn(Optional.of(existing));

        PollCreateRequest req = new PollCreateRequest(1L, 2, 3L, 5, "New", 99, null, null);
        SaveResult result = pollService.save(req);

        assertThat(result).isEqualTo(SaveResult.UPDATED);
        assertThat(existing.getTitle()).isEqualTo("New");
        assertThat(existing.getVotes()).isEqualTo(99);
        verify(pollRepository, never()).save(any());
    }

    @Test
    void filterByMaxTopicVotes_keepsOnlyWinningTopicPerEpisodeOption() {
        List<PollResponse> input = List.of(
                new PollResponse(1L, 1, 100L, 1, "t", 10),
                new PollResponse(1L, 1, 101L, 1, "t", 3),
                new PollResponse(1L, 2, 200L, 1, "t", 7));

        List<PollResponse> out = pollService.filterByMaxTopicVotes(input);

        assertThat(out).hasSize(2);
        assertThat(out.stream().map(PollResponse::topicId)).containsExactly(100L, 200L);
    }

    @Test
    void filterByMaxTopicVotes_emptyInput_returnsEmpty() {
        assertThat(pollService.filterByMaxTopicVotes(List.of())).isEmpty();
        assertThat(pollService.filterByMaxTopicVotes(null)).isEmpty();
    }

    private static PollCreateRequest sampleRequest() {
        return new PollCreateRequest(1L, 2, 3L, 5, "Topic", 10, null, null);
    }
}
