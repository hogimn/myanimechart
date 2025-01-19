package com.hogimn.myanimechart.database.anime.dao;

import com.hogimn.myanimechart.common.util.DateUtil;
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

    public static AnimeDao from(Anime katsuteAnime) {
        AnimeDao anime = new AnimeDao();
        anime.setId(katsuteAnime.getID());
        anime.setTitle(katsuteAnime.getTitle());
        anime.setSeason(katsuteAnime.getStartSeason().getSeason().field());
        anime.setImage(katsuteAnime.getMainPicture().getMediumURL());
        anime.setLink("https://myanimelist.net/anime/" + katsuteAnime.getID());
        anime.setScore(katsuteAnime.getMeanRating() != null ? katsuteAnime.getMeanRating().doubleValue() : 0.0);
        anime.setMembers(katsuteAnime.getUserListingCount());
        anime.setYear(katsuteAnime.getStartSeason().getYear());
        anime.setSource(katsuteAnime.getSource().field());
        anime.setGenre(String.join(", ", Arrays.stream(katsuteAnime.getGenres()).map(Genre::getName).toList()));
        anime.setStudios(String.join(", ", Arrays.stream(katsuteAnime.getStudios()).map(IDN::getName).toList()));
        anime.setRank(katsuteAnime.getRank());
        anime.setPopularity(katsuteAnime.getPopularity());
        anime.setScoringCount(katsuteAnime.getUserScoringCount());
        anime.setEpisodes(katsuteAnime.getEpisodes());
        anime.setAirStatus(!Objects.equals(katsuteAnime.getStatus().field(), "") ?
                katsuteAnime.getStatus().field() : katsuteAnime.getRawStatus());
        anime.setTitle(katsuteAnime.getTitle());
        anime.setType(!Objects.equals(katsuteAnime.getType().field(), "") ?
                katsuteAnime.getType().field() : katsuteAnime.getRawType());

        if (anime.getAirStatus().equals("finished_airing")) {
            anime.setFinishedAt(DateUtil.now());
        }

        anime.setStartDate(katsuteAnime.getStartDate() != null ? katsuteAnime.getStartDate().getDate() : null);
        anime.setEndDate(katsuteAnime.getEndDate() != null ? katsuteAnime.getEndDate().getDate() : null);
        anime.setEnglishTitle(katsuteAnime.getAlternativeTitles().getEnglish());
        anime.setJapaneseTitle(katsuteAnime.getAlternativeTitles().getJapanese());
        anime.setSynopsis(katsuteAnime.getSynopsis());

        return anime;
    }
}
