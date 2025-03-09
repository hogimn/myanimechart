package com.hogimn.myanimechart.common.poll;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "anime_episode_topic_mapping")
@Data
public class AnimeEpisodeTopicMappingEntity {
    @Id
    private long animeId;

    @Id
    private int episode;

    @Id
    private long topicId;
}
