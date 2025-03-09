package com.hogimn.myanimechart.common.poll;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimeEpisodeTopicMappingService {
    private final AnimeEpisodeTopicMappingRepository episodeTopicMappingRepository;

    public AnimeEpisodeTopicMappingService(AnimeEpisodeTopicMappingRepository episodeTopicMappingRepository) {
        this.episodeTopicMappingRepository = episodeTopicMappingRepository;
    }

    public List<AnimeEpisodeTopicMappingEntity> getByAnimeIdEpisode(long animeId) {
        return episodeTopicMappingRepository.findByAnimeId(animeId);
    }
}
