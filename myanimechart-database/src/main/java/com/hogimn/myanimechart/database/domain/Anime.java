package com.hogimn.myanimechart.database.domain;

import com.hogimn.myanimechart.database.dao.AnimeDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Anime {
    private Long id;
    private String title;
    private String link;
    private String image;
    private String synopsis;
    private String startDate;
    private Double score;
    private Integer members;
    private List<String> genre;
    private List<String> studios;
    private List<String> sources;
    private Integer year;
    private String season;
    private List<AnimeStat> animeStats;

    public static Anime from(AnimeDao animeDao) {
        Anime anime = new Anime();
        anime.setId(animeDao.getId());
        anime.setTitle(animeDao.getTitle());
        anime.setSeason(animeDao.getSeason());
        anime.setSynopsis(animeDao.getSynopsis());
        anime.setImage(animeDao.getImage());
        anime.setLink(animeDao.getLink());
        anime.setScore(animeDao.getScore());
        anime.setMembers(animeDao.getMembers());
        anime.setYear(animeDao.getYear());
        anime.setStartDate(animeDao.getStartDate());
        anime.setSources(Arrays.asList(animeDao.getSources().split(",")));
        anime.setGenre(Arrays.asList(animeDao.getGenre().split(",")));
        anime.setStudios(Arrays.asList(animeDao.getStudios().split(",")));
        return anime;
    }
}
