package com.hogimn.myanimechart.core.domain.poll.collectionstatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "poll_collection_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PollCollectionStatusEntity {
    @Id
    private Long animeId;

    @Enumerated(EnumType.STRING)
    private CollectionStatus status;

    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private LocalDateTime updatedAt;
}
