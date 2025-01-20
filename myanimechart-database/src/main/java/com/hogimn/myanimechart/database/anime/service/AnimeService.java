package com.hogimn.myanimechart.database.anime.service;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.anime.dao.AnimeDao;
import com.hogimn.myanimechart.database.anime.dto.AnimeDto;
import com.hogimn.myanimechart.database.anime.repository.AnimeRepository;
import dev.katsute.mal4j.anime.Anime;
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

    public AnimeDao upsertAnime(Anime anime, int year, String season) {
        if (anime == null) {
            return null;
        }

        AnimeDao animeDao = AnimeDao.from(anime);
        if (animeDao.getYear() != year || !Objects.equals(animeDao.getSeason(), season)) {
            log.info("Skipping anime '{}': Year {} (expected: {}), Season {} (expected: {})",
                    animeDao.getTitle(), animeDao.getYear(), year, animeDao.getSeason(), season);
            return null;
        }

        if (animeDao.getScore() == 0.0) {
            log.info("Skipping anime '{}': Score {} (expected: > 0.0)",
                    animeDao.getTitle(), animeDao.getScore());
            return null;
        }

        Optional<AnimeDao> optional = animeRepository.findById(animeDao.getId());
        if (optional.isPresent()) {
            if (animeDao.getFinishedAt() != null) {
                animeDao.setFinishedAt(null);
            }
            animeDao.setUpdatedAt(DateUtil.now());
            AnimeDao saved = animeRepository.save(animeDao);
            log.info("Updated anime: {}", saved);
            return saved;
        }

        animeDao.setCreatedAt(DateUtil.now());
        AnimeDao saved = animeRepository.save(animeDao);
        log.info("Inserted new anime: {}", saved);
        return saved;
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
}
