package com.hogimn.myanimechart.database.anime.dao;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.anime.dto.AnimeDto;
import dev.katsute.mal4j.anime.Anime;
import dev.katsute.mal4j.property.Genre;
import dev.katsute.mal4j.property.IDN;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AnimeDao from(AnimeDto animeDto) {
        AnimeDao anime = new AnimeDao();
        anime.setId(animeDto.getId());
        anime.setTitle(animeDto.getTitle());
        anime.setSeason(animeDto.getSeason());
        anime.setImage(animeDto.getImage());
        anime.setLink(animeDto.getLink());
        anime.setScore(animeDto.getScore());
        anime.setMembers(animeDto.getMembers());
        anime.setYear(animeDto.getYear());
        anime.setSource(animeDto.getSource());
        anime.setGenre(String.join(", ", animeDto.getGenre()));
        anime.setStudios(String.join(", ", animeDto.getStudios()));
        anime.setRank(animeDto.getRank());
        anime.setPopularity(animeDto.getPopularity());
        anime.setScoringCount(animeDto.getScoringCount());
        anime.setEpisodes(animeDto.getEpisodes());
        anime.setAirStatus(animeDto.getAirStatus());
        anime.setFinishedAt(animeDto.getFinishedAt());
        anime.setTitle(animeDto.getTitle());
        anime.setType(animeDto.getType());
        anime.setStartDate(animeDto.getStartDate());
        anime.setEndDate(animeDto.getEndDate());
        anime.setEnglishTitle(animeDto.getEnglishTitle());
        anime.setJapaneseTitle(animeDto.getJapaneseTitle());
        anime.setSynopsis(animeDto.getSynopsis());
        return anime;
    }

    public void setFrom(AnimeDto animeDto) {
        this.setId(animeDto.getId());
        this.setTitle(animeDto.getTitle());
        this.setSeason(animeDto.getSeason());
        this.setImage(animeDto.getImage());
        this.setLink(animeDto.getLink());
        this.setScore(animeDto.getScore());
        this.setMembers(animeDto.getMembers());
        this.setYear(animeDto.getYear());
        this.setSource(animeDto.getSource());
        this.setGenre(String.join(", ", animeDto.getGenre()));
        this.setStudios(String.join(", ", animeDto.getStudios()));
        this.setRank(animeDto.getRank());
        this.setPopularity(animeDto.getPopularity());
        this.setScoringCount(animeDto.getScoringCount());
        this.setEpisodes(animeDto.getEpisodes());
        this.setAirStatus(animeDto.getAirStatus());
        this.setFinishedAt(animeDto.getFinishedAt());
        this.setTitle(animeDto.getTitle());
        this.setType(animeDto.getType());
        this.setStartDate(animeDto.getStartDate());
        this.setEndDate(animeDto.getEndDate());
        this.setEnglishTitle(animeDto.getEnglishTitle());
        this.setJapaneseTitle(animeDto.getJapaneseTitle());
        this.setSynopsis(animeDto.getSynopsis());
    }
}
