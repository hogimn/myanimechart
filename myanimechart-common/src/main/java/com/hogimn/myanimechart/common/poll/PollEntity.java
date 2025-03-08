package com.hogimn.myanimechart.common.poll;

import com.hogimn.myanimechart.common.anime.AnimeEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "poll")
@Data
@IdClass(PollId.class)
public class PollEntity {
    @Id
    @ManyToOne
    @JoinColumn(name = "anime_id", referencedColumnName = "id")
    private AnimeEntity anime;

    @Id
    @ManyToOne
    @JoinColumn(name = "poll_option_id", referencedColumnName = "id")
    private PollOptionEntity pollOption;

    @Id
    private Long topicId;

    @Id
    private Integer episode;

    private String title;
    private Integer votes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
