package com.hogimn.myanimechart.core.domain.poll.mapping;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "anime_episode_topic_mapping")
@IdClass(AnimeEpisodeTopicMappingId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnimeEpisodeTopicMappingEntity {
    @Id
    private Long animeId;

    @Id
    private Integer episode;

    @Id
    private Long topicId;
}
