package com.hogimn.myanimechart.database.anime.dao;

import com.hogimn.myanimechart.database.anime.domain.Anime;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

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
    private Integer rank;
    private Integer popularity;
    private Integer scoringCount;
    private Integer episodes;
    private String airStatus;
    private String type;
    private LocalDateTime finishedAt;
    private Date startDate;
    private Date endDate;
    private String englishTitle;
    private String japaneseTitle;
    private String synopsis;

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
        animeDao.rank = anime.getRank();
        animeDao.popularity = anime.getPopularity();
        animeDao.scoringCount = anime.getScoringCount();
        animeDao.episodes = anime.getEpisodes();
        animeDao.airStatus = anime.getAirStatus();
        animeDao.type = anime.getType();
        animeDao.finishedAt = anime.getFinishedAt();
        animeDao.startDate = anime.getStartDate();
        animeDao.endDate = anime.getEndDate();
        animeDao.englishTitle = anime.getEnglishTitle();
        animeDao.japaneseTitle = anime.getJapaneseTitle();
        animeDao.synopsis = anime.getSynopsis();
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
        this.rank = anime.getRank();
        this.popularity = anime.getPopularity();
        this.scoringCount = anime.getScoringCount();
        this.episodes = anime.getEpisodes();
        this.airStatus = anime.getAirStatus();
        this.type = anime.getType();
        this.finishedAt = anime.getFinishedAt();
        this.startDate = anime.getStartDate();
        this.endDate = anime.getEndDate();
        this.englishTitle = anime.getEnglishTitle();
        this.japaneseTitle = anime.getJapaneseTitle();
        this.synopsis = anime.getSynopsis();
    }
}
