package com.hogimn.myanimechart.service.anime;

import com.hogimn.myanimechart.core.common.result.SaveResult;
import com.hogimn.myanimechart.core.domain.anime.AnimeEntity;
import com.hogimn.myanimechart.core.domain.anime.AnimePollRow;
import com.hogimn.myanimechart.core.domain.anime.AnimeRepository;
import com.hogimn.myanimechart.core.domain.poll.PollEntity;
import com.hogimn.myanimechart.service.poll.PollResponse;
import com.hogimn.myanimechart.service.poll.PollService;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnimeServiceTest {

    @Mock
    private AnimeRepository animeRepository;

    @Mock
    private PollService pollService;

    @InjectMocks
    private AnimeService animeService;

    @Test
    void save_createsWhenMissing() {
        when(animeRepository.findById(10L)).thenReturn(Optional.empty());

        SaveResult result = animeService.save(minimalCreateRequest(10L, "New title"));

        assertThat(result).isEqualTo(SaveResult.CREATED);
        ArgumentCaptor<AnimeEntity> captor = ArgumentCaptor.forClass(AnimeEntity.class);
        verify(animeRepository).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(10L);
        assertThat(captor.getValue().getTitle()).isEqualTo("New title");
        assertThat(captor.getValue().getCreatedAt()).isNotNull();
    }

    @Test
    void save_updatesWhenPresent() {
        AnimeEntity existing = AnimeEntity.builder().id(1L).title("Old").build();
        when(animeRepository.findById(1L)).thenReturn(Optional.of(existing));

        SaveResult result = animeService.save(minimalCreateRequest(1L, "Updated"));

        assertThat(result).isEqualTo(SaveResult.UPDATED);
        assertThat(existing.getTitle()).isEqualTo("Updated");
        verify(animeRepository, never()).save(any());
    }

    @Test
    void getByYearAndSeason_groupsPollsPerAnime() {
        AnimeEntity anime = AnimeEntity.builder().id(99L).title("A").build();
        PollEntity p1 = PollEntity.builder()
                .animeId(99L)
                .pollOptionId(1)
                .topicId(10L)
                .episode(1)
                .title("t1")
                .votes(5)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        PollEntity p2 = PollEntity.builder()
                .animeId(99L)
                .pollOptionId(1)
                .topicId(11L)
                .episode(1)
                .title("t2")
                .votes(3)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        when(animeRepository.findWithPollsByYearAndSeason(2024, "spring"))
                .thenReturn(List.of(new AnimePollRow(anime, p1), new AnimePollRow(anime, p2)));
        when(pollService.filterByMaxTopicVotes(anyList())).thenAnswer(inv -> inv.getArgument(0));

        List<AnimeResponse> responses = animeService.getByYearAndSeason(2024, "spring");

        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().polls()).hasSize(2);
        assertThat(responses.getFirst().polls().stream().map(PollResponse::topicId)).containsExactly(10L, 11L);
    }

    @Test
    void getByYearAndSeason_skipsFilterWhenNoPolls() {
        AnimeEntity anime = AnimeEntity.builder().id(1L).title("Solo").build();
        when(animeRepository.findWithPollsByYearAndSeason(2020, "winter"))
                .thenReturn(List.of(new AnimePollRow(anime, null)));

        List<AnimeResponse> responses = animeService.getByYearAndSeason(2020, "winter");

        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().polls()).isEmpty();
        verify(pollService, never()).filterByMaxTopicVotes(anyList());
    }

    private static AnimeCreateRequest minimalCreateRequest(long id, String title) {
        return new AnimeCreateRequest(
                id,
                title,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }
}
