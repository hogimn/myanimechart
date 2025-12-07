package com.hogimn.myanimechart.service.anime;

import com.hogimn.myanimechart.core.domain.anime.AnimeEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimeCreateRequest {
    @NotBlank(message = "id is required.")
    private Long id;
    private String title;
    private String link;
    private String image;
    private String largeImage;
    private Double score;
    private Integer members;
    private List<String> genre;
    private List<String> studios;
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
    private String rating;
    private String nsfw;
    private String forceCollect;

    public AnimeEntity toEntity() {
        AnimeEntity anime = new AnimeEntity();
        anime.setId(getId());
        anime.setTitle(getTitle());
        anime.setSeason(getSeason());
        anime.setImage(getImage());
        anime.setLargeImage(getLargeImage());
        anime.setLink(getLink());
        anime.setScore(getScore());
        anime.setMembers(getMembers());
        anime.setYear(getYear());
        anime.setSource(getSource());
        anime.setGenre(String.join(", ", getGenre()));
        anime.setStudios(String.join(", ", getStudios()));
        anime.setRank(getRank());
        anime.setPopularity(getPopularity());
        anime.setScoringCount(getScoringCount());
        anime.setEpisodes(getEpisodes());
        anime.setAirStatus(getAirStatus());
        anime.setTitle(getTitle());
        anime.setType(getType());
        anime.setStartDate(getStartDate());
        anime.setEndDate(getEndDate());
        anime.setEnglishTitle(getEnglishTitle());
        anime.setJapaneseTitle(getJapaneseTitle());
        anime.setSynopsis(getSynopsis());
        anime.setRating(getRating());
        anime.setNsfw(getNsfw());
        return anime;
    }
}
