package com.hogimn.myanimechart.common.poll;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "poll")
@Data
@IdClass(PollId.class)
public class PollEntity {
    @Id
    private Long animeId;

    @Id
    private Integer pollOptionId;

    @Id
    private Long topicId;

    private Integer episode;
    private String title;
    private Integer votes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
