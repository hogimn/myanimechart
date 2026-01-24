package com.hogimn.myanimechart.core.domain.poll;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "poll")
@IdClass(PollId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    public void update(String title, Integer votes, Integer episode) {
        this.title = title;
        this.votes = votes;
        this.episode = episode;
        this.updatedAt = LocalDateTime.now();
    }
}
