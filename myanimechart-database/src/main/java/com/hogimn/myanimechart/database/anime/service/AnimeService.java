package com.hogimn.myanimechart.database.anime.service;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.anime.dto.AnimeDto;
import com.hogimn.myanimechart.database.anime.entity.AnimeEntity;
import com.hogimn.myanimechart.database.anime.repository.AnimeRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AnimeService {
    private final AnimeRepository animeRepository;

    public AnimeService(AnimeRepository animeRepository) {
        this.animeRepository = animeRepository;
    }

    @Transactional
    public void upsertAnime(AnimeDto animeDto) {
        if (animeDto == null) {
            return;
        }

        Optional<AnimeEntity> optional = animeRepository.findById(animeDto.getId());
        if (optional.isPresent()) {
            AnimeEntity animeEntity = optional.get();
            animeEntity.setFrom(animeDto);
            animeEntity.setUpdatedAt(DateUtil.now());

            if (Objects.equals(animeDto.getAirStatus(), "finished_airing")
                    && animeEntity.getFinishedAt() != null) {
                animeDto.setFinishedAt(DateUtil.now());
            }

            AnimeEntity saved = animeRepository.save(animeEntity);
            log.info("Updated anime: {}", saved);
            return;
        }

        AnimeEntity animeEntity = AnimeEntity.from(animeDto);
        animeEntity.setCreatedAt(DateUtil.now());

        if (Objects.equals(animeDto.getAirStatus(), "finished_airing")) {
            animeDto.setFinishedAt(DateUtil.now());
        }

        AnimeEntity saved = animeRepository.save(animeEntity);
        log.info("Inserted new anime: {}", saved);
    }

    public AnimeEntity getAnimeEntityByTitle(String title) {
        Optional<AnimeEntity> optional = animeRepository.findByTitle(title);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new IllegalArgumentException("Anime not found (" + title + ")");
    }

    public AnimeDto getAnimeDtoByTitle(String title) {
        AnimeEntity animeEntity = getAnimeEntityByTitle(title);
        return AnimeDto.from(animeEntity);
    }

    public AnimeDto getAnimeDtoById(Long id) {
        AnimeEntity animeEntity = getAnimeEntityById(id);
        return AnimeDto.from(animeEntity);
    }

    public AnimeEntity getAnimeEntityById(Long id) {
        Optional<AnimeEntity> optional = animeRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new IllegalArgumentException("Anime not found (" + id + ")");
    }

    public List<AnimeEntity> getAnimeEntitiesByYearAndSeason(int year, String season) {
        return animeRepository.findByYearAndSeason(year, season);
    }

    public List<AnimeDto> getAnimeDtosByYearAndSeason(int year, String season) {
        List<AnimeEntity> animeEntities = getAnimeEntitiesByYearAndSeason(year, season);
        return animeEntities.stream().map(AnimeDto::from).collect(Collectors.toList());
    }

    public List<AnimeEntity> getAiringAnimeEntitiesExcludingCurrentAndNextSeason(int year, String season,
                                                                                 int nextYear, String nextSeason) {
        return animeRepository.findAiringAnimeExcludingCurrentAndNextSeason(
                year, season, nextYear, nextSeason, "currently_airing", "finished_airing");
    }

    public List<AnimeEntity> getAnimeEntitiesByKeyword(String keyword) {
        return animeRepository.findAllByTitleContaining(keyword);
    }

    public List<AnimeDto> getAnimeDtosByKeyword(String keyword) {
        List<AnimeEntity> animeEntities = getAnimeEntitiesByKeyword(keyword);
        return animeEntities.stream().map(AnimeDto::from).collect(Collectors.toList());
    }

    public List<AnimeEntity> getAiringAnimeEntities() {
        return animeRepository.findAiringAnime("currently_airing", "finished_airing");
    }

    @Transactional
    public AnimeEntity getAnimeEntityByIdWithLock(Long animeId) {
        return animeRepository.findByIdWithLock(animeId);
    }
}
