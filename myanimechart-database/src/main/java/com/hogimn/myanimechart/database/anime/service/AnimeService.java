package com.hogimn.myanimechart.database.anime.service;

import com.hogimn.myanimechart.database.anime.dao.AnimeDao;
import com.hogimn.myanimechart.database.anime.domain.Anime;
import com.hogimn.myanimechart.database.anime.repository.AnimeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AnimeService {
    private final AnimeRepository animeRepository;

    public AnimeService(AnimeRepository animeRepository) {
        this.animeRepository = animeRepository;
    }

    public AnimeDao upsertAnime(Anime anime) {
        if (anime == null) {
            return null;
        }

        AnimeDao animeDao = AnimeDao.from(anime);
        Optional<AnimeDao> optional = animeRepository.findByTitle(anime.getTitle());

        if (optional.isPresent()) {
            AnimeDao foundAnime = optional.get();

            if (foundAnime.getFinishedAt() != null) {
                foundAnime.setFrom(anime);
                foundAnime.setFinishedAt(null);
            } else {
                foundAnime.setFrom(anime);
            }

            animeRepository.save(foundAnime);
            return foundAnime;
        }

        return animeRepository.save(animeDao);
    }

    public Anime getAnimeByTitle(String title) {
        Optional<AnimeDao> optional = animeRepository.findByTitle(title);
        if (optional.isPresent()) {
            AnimeDao animeDao = optional.get();
            return Anime.from(animeDao);
        }
        throw new IllegalArgumentException("Anime not found (" + title + ")");
    }

    public Anime getAnimeById(Long id) {
        Optional<AnimeDao> optional = animeRepository.findById(id);
        if (optional.isPresent()) {
            AnimeDao animeDao = optional.get();
            return Anime.from(animeDao);
        }
        throw new IllegalArgumentException("Anime not found (" + id + ")");
    }

    public List<Anime> getAnimeByYearAndSeason(Integer year, String season) {
        List<AnimeDao> animeDaos = animeRepository.findByYearAndSeason(year, season);
        return animeDaos.stream().map(Anime::from).collect(Collectors.toList());
    }

    public List<Anime> getAiringAnimeExcludingCurrentAndNextSeason(Integer year, String season,
                                                                   Integer nextYear, String nextSeason) {
        return animeRepository.findAiringAnimeExcludingCurrentAndNextSeason(
                        year, season, nextYear, nextSeason, "currently_airing", "finished_airing")
                .stream().map(Anime::from).collect(Collectors.toList());
    }

    public List<Anime> getAnimeByKeyword(String keyword) {
        List<AnimeDao> animeDaos = animeRepository.findAllByTitleContaining(keyword);
        return animeDaos.stream().map(Anime::from).collect(Collectors.toList());
    }
}
