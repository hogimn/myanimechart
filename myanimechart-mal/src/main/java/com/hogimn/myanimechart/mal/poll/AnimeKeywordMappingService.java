package com.hogimn.myanimechart.mal.poll;

import org.springframework.stereotype.Service;

@Service
public class AnimeKeywordMappingService {
    private final AnimeKeywordMappingRepository keywordMappingRepository;

    public AnimeKeywordMappingService(AnimeKeywordMappingRepository keywordMappingRepository) {
        this.keywordMappingRepository = keywordMappingRepository;
    }

    public String findSearchKeywordByAnimeId(long animeId) {
        return keywordMappingRepository.findByAnimeId(animeId)
                .map(AnimeKeywordMappingEntity::getSearchKeyword)
                .orElse(null);
    }
}
