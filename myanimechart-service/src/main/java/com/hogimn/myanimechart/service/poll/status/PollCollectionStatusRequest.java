package com.hogimn.myanimechart.service.poll.status;

import com.hogimn.myanimechart.core.domain.poll.collectionstatus.CollectionStatus;

import java.time.LocalDateTime;

public record PollCollectionStatusRequest(
        Long animeId,
        CollectionStatus status,
        LocalDateTime startedAt,
        LocalDateTime finishedAt,
        LocalDateTime updatedAt
) {
}
