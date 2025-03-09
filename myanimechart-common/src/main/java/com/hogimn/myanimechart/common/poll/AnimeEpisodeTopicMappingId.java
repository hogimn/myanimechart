package com.hogimn.myanimechart.common.poll;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class AnimeEpisodeTopicMappingId {
    private Long animeId;
    private Integer episode;
    private Long topicId;
}