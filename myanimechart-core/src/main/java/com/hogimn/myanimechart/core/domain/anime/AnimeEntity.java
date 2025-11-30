package com.hogimn.myanimechart.core.domain.anime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "anime")
@Data
public class AnimeEntity {
    @Id
    private Long id;

    private String title;
    private String link;
    private String image;
    private String largeImage;
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
    private Date startDate;
    private Date endDate;
    private String englishTitle;
    private String japaneseTitle;
    private String synopsis;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String rating;
    private String nsfw;
    private String forceCollect;

    public void setFrom(AnimeEntity entity) {
        this.setId(entity.getId());
        this.setTitle(entity.getTitle());
        this.setSeason(entity.getSeason());
        this.setImage(entity.getImage());
        this.setLargeImage(entity.getLargeImage());
        this.setLink(entity.getLink());
        this.setScore(entity.getScore());
        this.setMembers(entity.getMembers());
        this.setYear(entity.getYear());
        this.setSource(entity.getSource());
        this.setGenre(entity.getGenre());
        this.setStudios(entity.getStudios());
        this.setRank(entity.getRank());
        this.setPopularity(entity.getPopularity());
        this.setScoringCount(entity.getScoringCount());
        this.setEpisodes(entity.getEpisodes());
        this.setAirStatus(entity.getAirStatus());
        this.setTitle(entity.getTitle());
        this.setType(entity.getType());
        this.setStartDate(entity.getStartDate());
        this.setEndDate(entity.getEndDate());
        this.setEnglishTitle(entity.getEnglishTitle());
        this.setJapaneseTitle(entity.getJapaneseTitle());
        this.setSynopsis(entity.getSynopsis());
        this.setRating(entity.getRating());
        this.setNsfw(entity.getNsfw());
    }
}
