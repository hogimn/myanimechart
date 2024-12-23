package com.hogimn.myanimechart.database.dao;

import com.hogimn.myanimechart.database.domain.Anime;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "anime")
@Data
public class AnimeDao {
    @Id
    private Long id;

    private String title;
    private String link;
    private String image;
    private Double score;
    private Integer members;
    private String genre;
    private String studios;
    private String source;
    private Integer year;
    private String season;

    public static AnimeDao from(Anime anime) {
        AnimeDao animeDao = new AnimeDao();
        animeDao.setId(anime.getId());
        animeDao.title = anime.getTitle();
        animeDao.link = anime.getLink();
        animeDao.image = anime.getImage();
        animeDao.members = anime.getMembers();
        animeDao.score = anime.getScore();
        animeDao.source = anime.getSource();
        animeDao.genre = String.join(", ", anime.getGenre());
        animeDao.studios = String.join(", ", anime.getStudios());
        animeDao.year = anime.getYear();
        animeDao.season = anime.getSeason();
        return animeDao;
    }

    public void setFrom(Anime anime) {
        this.id = anime.getId();
        this.title = anime.getTitle();
        this.link = anime.getLink();
        this.image = anime.getImage();
        this.members = anime.getMembers();
        this.score = anime.getScore();
        this.source = anime.getSource();
        this.year = anime.getYear();
        this.season = anime.getSeason();
        this.genre = String.join(", ", anime.getGenre());
        this.studios = String.join(", ", anime.getStudios());
    }
}
