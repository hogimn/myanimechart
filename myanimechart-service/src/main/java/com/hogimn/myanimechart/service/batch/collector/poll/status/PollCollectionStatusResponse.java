package com.hogimn.myanimechart.service.batch.collector.poll.status;

import com.hogimn.myanimechart.core.domain.poll.collectionstatus.CollectionStatus;
import com.hogimn.myanimechart.core.domain.poll.collectionstatus.PollCollectionStatusEntity;
import com.hogimn.myanimechart.service.anime.AnimeResponse;

import java.time.LocalDateTime;

public record PollCollectionStatusResponse(
        Long animeId,
        CollectionStatus status,
        LocalDateTime startedAt,
        LocalDateTime finishedAt,
        LocalDateTime updatedAt,
        AnimeResponse animeResponse
) {
    public static PollCollectionStatusResponse from(
            PollCollectionStatusEntity entity
    ) {
        return new PollCollectionStatusResponse(
                entity.getAnimeId(),
                entity.getStatus(),
                entity.getStartedAt(),
                entity.getFinishedAt(),
                entity.getUpdatedAt(),
                null
        );
    }

    public static PollCollectionStatusResponse from(
            PollCollectionStatusEntity entity, AnimeResponse animeResponse
    ) {
        return new PollCollectionStatusResponse(
                entity.getAnimeId(),
                entity.getStatus(),
                entity.getStartedAt(),
                entity.getFinishedAt(),
                entity.getUpdatedAt(),
                animeResponse
        );
    }
}