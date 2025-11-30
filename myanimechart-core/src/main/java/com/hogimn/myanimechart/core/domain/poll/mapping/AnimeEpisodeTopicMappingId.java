package com.hogimn.myanimechart.core.domain.poll.mapping;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class AnimeEpisodeTopicMappingId {
    private Long animeId;
    private Integer episode;
    private Long topicId;
}