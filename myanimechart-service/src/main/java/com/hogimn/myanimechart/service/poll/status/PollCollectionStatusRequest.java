package com.hogimn.myanimechart.service.poll.status;

import com.hogimn.myanimechart.core.domain.poll.collectionstatus.CollectionStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record PollCollectionStatusRequest(
        @NotNull(message = "animeId is required.")
        Long animeId,

        @NotNull(message = "status is required.")
        CollectionStatus status,

        LocalDateTime startedAt,
        LocalDateTime finishedAt,

        @NotNull(message = "updatedAt is required.")
        LocalDateTime updatedAt
) {
}
