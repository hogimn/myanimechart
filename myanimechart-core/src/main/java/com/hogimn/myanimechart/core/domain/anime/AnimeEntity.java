package com.hogimn.myanimechart.core.domain.anime;

import com.hogimn.myanimechart.core.common.util.DateUtil;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "anime")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    public void update(AnimeEntity other) {
        this.title = other.title;
        this.link = other.link;
        this.image = other.image;
        this.largeImage = other.largeImage;
        this.score = other.score;
        this.members = other.members;
        this.genre = other.genre;
        this.studios = other.studios;
        this.source = other.source;
        this.year = other.year;
        this.season = other.season;
        this.rank = other.rank;
        this.popularity = other.popularity;
        this.scoringCount = other.scoringCount;
        this.episodes = other.episodes;
        this.airStatus = other.airStatus;
        this.type = other.type;
        this.startDate = other.startDate;
        this.endDate = other.endDate;
        this.englishTitle = other.englishTitle;
        this.japaneseTitle = other.japaneseTitle;
        this.synopsis = other.synopsis;
        this.rating = other.rating;
        this.nsfw = other.nsfw;
        this.updatedAt = DateUtil.now();
    }
}
