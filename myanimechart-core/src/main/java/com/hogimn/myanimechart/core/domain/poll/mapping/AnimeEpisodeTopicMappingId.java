package com.hogimn.myanimechart.core.domain.poll.mapping;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AnimeEpisodeTopicMappingId {
    private Long animeId;
    private Integer episode;
    private Long topicId;
}