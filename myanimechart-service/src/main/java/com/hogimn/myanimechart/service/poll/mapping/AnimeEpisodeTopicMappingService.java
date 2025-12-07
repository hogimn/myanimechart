package com.hogimn.myanimechart.service.poll.mapping;

import com.hogimn.myanimechart.core.domain.poll.mapping.AnimeEpisodeTopicMappingEntity;
import com.hogimn.myanimechart.core.domain.poll.mapping.AnimeEpisodeTopicMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeEpisodeTopicMappingService {
    private final AnimeEpisodeTopicMappingRepository episodeTopicMappingRepository;

    public List<AnimeEpisodeTopicMappingResponse> getAnimeEpisodeTopicMappings(long animeId) {
        List<AnimeEpisodeTopicMappingEntity> result = episodeTopicMappingRepository.findByAnimeId(animeId);
        return result.stream()
                .map(AnimeEpisodeTopicMappingResponse::from)
                .toList();
    }
}
