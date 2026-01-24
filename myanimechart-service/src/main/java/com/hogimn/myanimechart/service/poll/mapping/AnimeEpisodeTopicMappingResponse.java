package com.hogimn.myanimechart.service.poll.mapping;

import com.hogimn.myanimechart.core.domain.poll.mapping.AnimeEpisodeTopicMappingEntity;

public record AnimeEpisodeTopicMappingResponse(
        Long animeId,
        Integer episode,
        Long topicId
) {
    public static AnimeEpisodeTopicMappingResponse from(AnimeEpisodeTopicMappingEntity entity) {
        return new AnimeEpisodeTopicMappingResponse(
                entity.getAnimeId(),
                entity.getEpisode(),
                entity.getTopicId()
        );
    }
}
