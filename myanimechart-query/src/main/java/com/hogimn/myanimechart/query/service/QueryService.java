package com.hogimn.myanimechart.query.service;

import com.hogimn.myanimechart.database.dao.AnimeDao;
import com.hogimn.myanimechart.database.dao.AnimeStatDao;
import com.hogimn.myanimechart.database.domain.Anime;
import com.hogimn.myanimechart.database.domain.AnimeStat;
import com.hogimn.myanimechart.database.repository.AnimeRepository;
import com.hogimn.myanimechart.database.repository.AnimeStatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QueryService {
    private final AnimeRepository animeRepository;
    private final AnimeStatRepository animeStatRepository;

    public QueryService(AnimeRepository animeRepository, AnimeStatRepository animeStatRepository) {
        this.animeRepository = animeRepository;
        this.animeStatRepository = animeStatRepository;
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
        } else {
            return null;
        }
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
            return Anime.from(animeDao);
        } else {
            return null;
        }
    }
}
