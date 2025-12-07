package com.hogimn.myanimechart.service.poll.status;

import com.hogimn.myanimechart.core.domain.poll.collectionstatus.CollectionStatus;
import com.hogimn.myanimechart.core.domain.poll.collectionstatus.PollCollectionStatusEntity;
import com.hogimn.myanimechart.service.anime.AnimeResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollCollectionStatusRequest {
    private Long animeId;
    private CollectionStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private LocalDateTime updatedAt;
    private AnimeResponse animeResponse;

    public static PollCollectionStatusRequest from(PollCollectionStatusEntity entity) {
        PollCollectionStatusRequest pollCollectionStatusRequest = new PollCollectionStatusRequest();
        pollCollectionStatusRequest.setAnimeId(entity.getAnimeId());
        pollCollectionStatusRequest.setStatus(entity.getStatus());
        pollCollectionStatusRequest.setStartedAt(entity.getStartedAt());
        pollCollectionStatusRequest.setFinishedAt(entity.getFinishedAt());
        pollCollectionStatusRequest.setUpdatedAt(entity.getUpdatedAt());
        return pollCollectionStatusRequest;
    }
}
