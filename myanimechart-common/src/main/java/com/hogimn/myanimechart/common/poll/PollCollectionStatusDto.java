package com.hogimn.myanimechart.common.poll;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollCollectionStatusDto {
    private Long animeId;
    private CollectionStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private LocalDateTime updatedAt;

    public static PollCollectionStatusDto from(PollCollectionStatusEntity entity) {
        return new PollCollectionStatusDto(
                entity.getAnimeId(),
                entity.getStatus(),
                entity.getStartedAt(),
                entity.getFinishedAt(),
                entity.getUpdatedAt()
        );
    }
}
