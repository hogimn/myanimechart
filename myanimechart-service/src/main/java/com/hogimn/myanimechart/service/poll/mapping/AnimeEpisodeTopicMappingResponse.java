package com.hogimn.myanimechart.service.poll.mapping;

import com.hogimn.myanimechart.core.domain.poll.mapping.AnimeEpisodeTopicMappingEntity;
import lombok.Data;

@Data
public class AnimeEpisodeTopicMappingResponse {
    private Long animeId;
    private Integer episode;
    private Long topicId;

    public static AnimeEpisodeTopicMappingResponse from(AnimeEpisodeTopicMappingEntity entity) {
        AnimeEpisodeTopicMappingResponse response = new AnimeEpisodeTopicMappingResponse();
        response.setAnimeId(entity.getAnimeId());
        response.setEpisode(entity.getEpisode());
        response.setTopicId(entity.getTopicId());
        return response;
    }
}
