package com.hogimn.myanimechart.database.service;

import com.hogimn.myanimechart.database.dao.AnimeDao;
import com.hogimn.myanimechart.database.dao.AnimeStatDao;
import com.hogimn.myanimechart.database.domain.Anime;
import com.hogimn.myanimechart.database.domain.AnimeStat;
import com.hogimn.myanimechart.database.repository.AnimeRepository;
import com.hogimn.myanimechart.database.repository.AnimeStatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        throw new IllegalArgumentException("Anime not found");
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
        throw new IllegalArgumentException("Anime not found");
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
        throw new IllegalArgumentException("Anime not found");
    }
}
