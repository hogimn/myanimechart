package com.hogimn.myanimechart.service.poll.mapping;

import com.hogimn.myanimechart.core.domain.poll.mapping.AnimeKeywordMappingEntity;
import com.hogimn.myanimechart.core.domain.poll.mapping.AnimeKeywordMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnimeKeywordMappingService {
    private final AnimeKeywordMappingRepository keywordMappingRepository;

    public String getSearchKeyword(long animeId) {
        return keywordMappingRepository.findByAnimeId(animeId)
                .map(AnimeKeywordMappingEntity::getSearchKeyword)
                .orElse(null);
    }
}
