package com.hogimn.myanimechart.common.collect;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "poll_collection_status")
@Data
public class PollCollectionStatusEntity {
    @Id
    private Long animeId;

    @Enumerated(EnumType.STRING)
    private CollectionStatus status;

    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private LocalDateTime updatedAt;
}
