package com.hogimn.myanimechart.database.anime.dao;

import com.hogimn.myanimechart.database.anime.dao.key.PollId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "poll")
@Data
@IdClass(PollId.class)
public class PollDao {
    @Id
    @ManyToOne
    @JoinColumn(name = "anime_id", referencedColumnName = "id")
    private AnimeDao anime;

    @Id
    @ManyToOne
    @JoinColumn(name = "poll_id", referencedColumnName = "id")
    private PollOptionDao pollOption;

    @Id
    private Long topicId;

    private Integer episode;
    private String title;
    private Integer votes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
