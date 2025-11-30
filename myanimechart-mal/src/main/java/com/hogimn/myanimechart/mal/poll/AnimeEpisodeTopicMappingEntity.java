package com.hogimn.myanimechart.mal.poll;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "anime_episode_topic_mapping")
@Data
@IdClass(AnimeEpisodeTopicMappingId.class)
public class AnimeEpisodeTopicMappingEntity {
    @Id
    private Long animeId;

    @Id
    private Integer episode;

    @Id
    private Long topicId;
}
