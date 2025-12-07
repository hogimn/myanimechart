package com.hogimn.myanimechart.service.batch.collector.poll.status;

import com.hogimn.myanimechart.core.domain.poll.collectionstatus.CollectionStatus;
import com.hogimn.myanimechart.core.domain.poll.collectionstatus.PollCollectionStatusEntity;
import com.hogimn.myanimechart.service.anime.AnimeResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class PollCollectionStatusResponse {
    private Long animeId;
    private CollectionStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private LocalDateTime updatedAt;
    private AnimeResponse animeResponse;

    public static PollCollectionStatusResponse from(PollCollectionStatusEntity entity) {
        PollCollectionStatusResponse pollCollectionStatusResponse = new PollCollectionStatusResponse();
        pollCollectionStatusResponse.setAnimeId(entity.getAnimeId());
        pollCollectionStatusResponse.setStatus(entity.getStatus());
        pollCollectionStatusResponse.setStartedAt(entity.getStartedAt());
        pollCollectionStatusResponse.setFinishedAt(entity.getFinishedAt());
        pollCollectionStatusResponse.setUpdatedAt(entity.getUpdatedAt());
        return pollCollectionStatusResponse;
    }
}
