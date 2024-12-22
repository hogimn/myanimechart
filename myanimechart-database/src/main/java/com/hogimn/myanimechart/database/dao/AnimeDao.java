package com.hogimn.myanimechart.database.dao;

import com.hogimn.myanimechart.database.domain.Anime;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "anime")
@Data
public class AnimeDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String link;
    private String image;
    private String synopsis;
    private String startDate;
    private Double score;
    private Integer members;
    private String genre;
    private String studios;
    private String sources;
    private Integer year;
    private String season;

    public static AnimeDao from(Anime anime) {
        AnimeDao animeDao = new AnimeDao();
        animeDao.title = anime.getTitle();
        animeDao.link = anime.getLink();
        animeDao.image = anime.getImage();
        animeDao.synopsis = anime.getSynopsis();
        animeDao.members = anime.getMembers();
        animeDao.score = anime.getScore();
        animeDao.genre = String.join(", ", anime.getGenre());
        animeDao.studios = String.join(", ", anime.getStudios());
        animeDao.sources = String.join(", ", anime.getSources());
        animeDao.year = anime.getYear();
        animeDao.season = anime.getSeason();
        animeDao.startDate = anime.getStartDate();
        return animeDao;
    }

    public void setFrom(Anime anime) {
        this.title = anime.getTitle();
        this.link = anime.getLink();
        this.image = anime.getImage();
        this.synopsis = anime.getSynopsis();
        this.members = anime.getMembers();
        this.score = anime.getScore();
        this.genre = String.join(", ", anime.getGenre());
        this.studios = String.join(", ", anime.getStudios());
        this.sources = String.join(", ", anime.getSources());
        this.year = anime.getYear();
        this.season = anime.getSeason();
        this.startDate = anime.getStartDate();
    }
}
