package com.hogimn.myanimechart.database.anime.service;

import com.hogimn.myanimechart.database.anime.dao.AnimeDao;
import com.hogimn.myanimechart.database.anime.dao.AnimeStatDao;
import com.hogimn.myanimechart.database.anime.domain.Anime;
import com.hogimn.myanimechart.database.anime.domain.AnimeStat;
import com.hogimn.myanimechart.database.anime.repository.AnimeStatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AnimeStatService {
    private final AnimeStatRepository animeStatRepository;
    private final AnimeService animeService;

    public AnimeStatService(AnimeStatRepository animeStatRepository, AnimeService animeService) {
        this.animeStatRepository = animeStatRepository;
        this.animeService = animeService;
    }

    public void saveAnimeStat(AnimeDao anime) {
        if (anime == null) {
            return;
        }

        AnimeStatDao animeStatDao = AnimeStatDao.from(anime);
        animeStatRepository.save(animeStatDao);
    }

    public List<Anime> getAnimeByYearAndSeason(Integer year, String season) {
        return animeService.getAnimeByYearAndSeason(year, season);
    }

    public List<AnimeStat> getAnimeStatByAnime(Anime anime) {
        Anime animeFound = animeService.getAnimeById(anime.getId());
        List<AnimeStatDao> animeStatDaos = animeStatRepository.findByAnime(AnimeDao.from(animeFound));
        return animeStatDaos.stream().map(AnimeStat::from).toList();
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
        Anime anime = animeService.getAnimeByTitle(title);
        List<AnimeStat> animeStat = getAnimeStatByAnime(anime);
        anime.setAnimeStats(animeStat);
        return anime;
    }

    public Anime getAnimeStatsById(Long id) {
        Anime anime = animeService.getAnimeById(id);
        List<AnimeStat> animeStat = getAnimeStatByAnime(anime);
        anime.setAnimeStats(animeStat);
        return anime;
    }

    public List<Anime> getAnimeStatsByKeyword(String keyword) {
        List<Anime> animeList = animeService.getAnimeByKeyword(keyword);
        animeList.forEach(anime -> {
            List<AnimeStat> animeStat = getAnimeStatByAnime(anime);
            anime.setAnimeStats(animeStat);
        });
        return animeList;
    }
}