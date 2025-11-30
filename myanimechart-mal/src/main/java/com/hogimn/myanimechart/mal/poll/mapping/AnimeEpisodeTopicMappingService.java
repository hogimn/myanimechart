package com.hogimn.myanimechart.mal.poll.mapping;

import com.hogimn.myanimechart.core.domain.poll.mapping.AnimeEpisodeTopicMappingEntity;
import com.hogimn.myanimechart.core.domain.poll.mapping.AnimeEpisodeTopicMappingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimeEpisodeTopicMappingService {
    private final AnimeEpisodeTopicMappingRepository episodeTopicMappingRepository;

    public AnimeEpisodeTopicMappingService(AnimeEpisodeTopicMappingRepository episodeTopicMappingRepository) {
        this.episodeTopicMappingRepository = episodeTopicMappingRepository;
    }

    public List<AnimeEpisodeTopicMappingEntity> findAnimeEpisodeTopicMappingEntityByAnimeIdEpisode(long animeId) {
        return episodeTopicMappingRepository.findByAnimeId(animeId);
    }
}
