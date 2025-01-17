package com.hogimn.myanimechart.database.anime.service;

import com.hogimn.myanimechart.database.anime.dao.AnimeDao;
import com.hogimn.myanimechart.database.anime.dao.AnimeStatDao;
import com.hogimn.myanimechart.database.anime.domain.Anime;
import com.hogimn.myanimechart.database.anime.domain.AnimeStat;
import com.hogimn.myanimechart.database.anime.repository.AnimeRepository;
import com.hogimn.myanimechart.database.anime.repository.AnimeStatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AnimeStatService {
    private final AnimeStatRepository animeStatRepository;
    private final AnimeRepository animeRepository;

    public AnimeStatService(AnimeStatRepository animeStatRepository, AnimeRepository animeRepository) {
        this.animeStatRepository = animeStatRepository;
        this.animeRepository = animeRepository;
    }

    public void saveAnimeStat(AnimeDao anime) {
        if (anime == null) {
            return;
        }

        AnimeStatDao animeStatDao = AnimeStatDao.from(anime);
        animeStatRepository.save(animeStatDao);
    }


    public List<Anime> getAnimeByYearAndSeason(Integer year, String season) {
        List<AnimeDao> animeDaos = animeRepository.findByYearAndSeason(year, season);
        return animeDaos.stream().map(Anime::from).toList();
    }

    public List<AnimeStat> getAnimeStatByAnime(Anime anime) {
        Optional<AnimeDao> animeDao = animeRepository.findById(anime.getId());
        if (animeDao.isPresent()) {
            List<AnimeStatDao> animeStatDaos = animeStatRepository.findByAnime(animeDao.get());
            return animeStatDaos.stream().map(AnimeStat::from).toList();
        }
        throw new IllegalArgumentException("Anime not found (" + anime.getId() + ")");
    }

    public List<Anime> getAnimeStats(Integer year, String season) {
        List<Anime> animeList = getAnimeByYearAndSeason(year, season);
        animeList.forEach(anime -> {
            List<AnimeStat> animeStat = getAnimeStatByAnime(anime);
            anime.setAnimeStats(animeStat);
        });
        return animeList;
    }

    public Anime getAnimeStatsByTitle(String title) {
        Optional<AnimeDao> optional = animeRepository.findByTitle(title);
        if (optional.isPresent()) {
            AnimeDao animeDao = optional.get();
            Anime anime = Anime.from(animeDao);
            List<AnimeStat> animeStat = getAnimeStatByAnime(Anime.from(animeDao));
            anime.setAnimeStats(animeStat);
            return anime;
        }
        throw new IllegalArgumentException("Anime not found (" + title + ")");
    }

    public Anime getAnimeStatsById(Long id) {
        Optional<AnimeDao> optional = animeRepository.findById(id);
        if (optional.isPresent()) {
            AnimeDao animeDao = optional.get();
            Anime anime = Anime.from(animeDao);
            List<AnimeStat> animeStat = getAnimeStatByAnime(Anime.from(animeDao));
            anime.setAnimeStats(animeStat);
            return anime;
        }
        throw new IllegalArgumentException("Anime not found (" + id + ")");
    }

    public List<Anime> getAnimeStatsByKeyword(String keyword) {
        List<AnimeDao> startsWithResults = animeRepository.findAllByTitleStartingWith(keyword);
        List<AnimeDao> containsResults = animeRepository.findAllByTitleContaining(keyword);

        Set<AnimeDao> combinedResults = new LinkedHashSet<>(startsWithResults);
        combinedResults.addAll(containsResults);

        return combinedResults.stream().map(animeDao -> {
            Anime anime = Anime.from(animeDao);
            List<AnimeStat> animeStat = getAnimeStatByAnime(Anime.from(animeDao));
            anime.setAnimeStats(animeStat);
            return anime;
        }).toList();
    }
}
