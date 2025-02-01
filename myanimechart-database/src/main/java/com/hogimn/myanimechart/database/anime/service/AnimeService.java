package com.hogimn.myanimechart.database.anime.service;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.anime.dao.AnimeDao;
import com.hogimn.myanimechart.database.anime.dto.AnimeDto;
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

        Optional<AnimeDao> optional = animeRepository.findById(animeDto.getId());
        if (optional.isPresent()) {
            AnimeDao animeDao = optional.get();
            animeDao.setFrom(animeDto);
            animeDao.setUpdatedAt(DateUtil.now());

            if (Objects.equals(animeDto.getAirStatus(), "finished_airing")
                    && animeDao.getFinishedAt() != null) {
                animeDto.setFinishedAt(DateUtil.now());
            }

            AnimeDao saved = animeRepository.save(animeDao);
            log.info("Updated anime: {}", saved);
            return;
        }

        AnimeDao animeDao = AnimeDao.from(animeDto);
        animeDao.setCreatedAt(DateUtil.now());

        if (Objects.equals(animeDto.getAirStatus(), "finished_airing")) {
            animeDto.setFinishedAt(DateUtil.now());
        }

        AnimeDao saved = animeRepository.save(animeDao);
        log.info("Inserted new anime: {}", saved);
    }

    public AnimeDao getAnimeDaoByTitle(String title) {
        Optional<AnimeDao> optional = animeRepository.findByTitle(title);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new IllegalArgumentException("Anime not found (" + title + ")");
    }

    public AnimeDto getAnimeDtoByTitle(String title) {
        AnimeDao animeDao = getAnimeDaoByTitle(title);
        return AnimeDto.from(animeDao);
    }

    public AnimeDto getAnimeDtoById(Long id) {
        AnimeDao animeDao = getAnimeDaoById(id);
        return AnimeDto.from(animeDao);
    }

    public AnimeDao getAnimeDaoById(Long id) {
        Optional<AnimeDao> optional = animeRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new IllegalArgumentException("Anime not found (" + id + ")");
    }

    public List<AnimeDao> getAnimeDaosByYearAndSeason(int year, String season) {
        return animeRepository.findByYearAndSeason(year, season);
    }

    public List<AnimeDto> getAnimeDtosByYearAndSeason(int year, String season) {
        List<AnimeDao> animeDaos = getAnimeDaosByYearAndSeason(year, season);
        return animeDaos.stream().map(AnimeDto::from).collect(Collectors.toList());
    }

    public List<AnimeDao> getAiringAnimeExcludingCurrentAndNextSeason(int year, String season,
                                                                      int nextYear, String nextSeason) {
        return animeRepository.findAiringAnimeExcludingCurrentAndNextSeason(
                year, season, nextYear, nextSeason, "currently_airing", "finished_airing");
    }

    public List<AnimeDao> getAnimeDaosByKeyword(String keyword) {
        return animeRepository.findAllByTitleContaining(keyword);
    }

    public List<AnimeDto> getAnimeDtosByKeyword(String keyword) {
        List<AnimeDao> animeDaos = getAnimeDaosByKeyword(keyword);
        return animeDaos.stream().map(AnimeDto::from).collect(Collectors.toList());
    }

    public List<AnimeDao> getAiringAnime() {
        return animeRepository.findAiringAnime("currently_airing", "finished_airing");
    }

    @Transactional
    public AnimeDao getAnimeDaoByIdWithLock(Long animeId) {
        return animeRepository.findByIdWithLock(animeId);
    }
}
