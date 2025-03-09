package com.hogimn.myanimechart.common.poll;

import org.springframework.stereotype.Service;

@Service
public class AnimeKeywordMappingService {
    private final AnimeKeywordMappingRepository keywordMappingRepository;

    public AnimeKeywordMappingService(AnimeKeywordMappingRepository keywordMappingRepository) {
        this.keywordMappingRepository = keywordMappingRepository;
    }

    public String getSearchKeywordByAnimeId(long animeId) {
        return keywordMappingRepository.findByAnimeId(animeId)
                .map(AnimeKeywordMappingEntity::getSearchKeyword)
                .orElse(null);
    }
}
