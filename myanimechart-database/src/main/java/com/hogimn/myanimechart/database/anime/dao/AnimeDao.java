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

    public static AnimeDao from(Anime anime) {
        AnimeDao animeDao = new AnimeDao();
        animeDao.setId(anime.getID());
        animeDao.setTitle(anime.getTitle());
        animeDao.setSeason(anime.getStartSeason().getSeason().field());
        animeDao.setImage(anime.getMainPicture().getMediumURL());
        animeDao.setLink("https://myanimelist.net/anime/" + anime.getID());
        animeDao.setScore(anime.getMeanRating() != null ? anime.getMeanRating().doubleValue() : 0.0);
        animeDao.setMembers(anime.getUserListingCount());
        animeDao.setYear(anime.getStartSeason().getYear());
        animeDao.setSource(anime.getSource().field());
        animeDao.setGenre(String.join(", ", Arrays.stream(anime.getGenres()).map(Genre::getName).toList()));
        animeDao.setStudios(String.join(", ", Arrays.stream(anime.getStudios()).map(IDN::getName).toList()));
        animeDao.setRank(anime.getRank());
        animeDao.setPopularity(anime.getPopularity());
        animeDao.setScoringCount(anime.getUserScoringCount());
        animeDao.setEpisodes(anime.getEpisodes());
        animeDao.setAirStatus(!Objects.equals(anime.getStatus().field(), "") ?
                anime.getStatus().field() : anime.getRawStatus());

        if (animeDao.getAirStatus() != null &&
                animeDao.getAirStatus().equals("finished_airing")) {
            animeDao.setFinishedAt(DateUtil.now());
        }

        animeDao.setTitle(anime.getTitle());
        animeDao.setType(!Objects.equals(anime.getType().field(), "") ?
                anime.getType().field() : anime.getRawType());
        animeDao.setStartDate(anime.getStartDate() != null ? anime.getStartDate().getDate() : null);
        animeDao.setEndDate(anime.getEndDate() != null ? anime.getEndDate().getDate() : null);
        animeDao.setEnglishTitle(anime.getAlternativeTitles().getEnglish());
        animeDao.setJapaneseTitle(anime.getAlternativeTitles().getJapanese());
        animeDao.setSynopsis(anime.getSynopsis());

        return animeDao;
    }

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
        anime.setAirStatus(anime.getAirStatus());
        anime.setTitle(animeDto.getTitle());
        anime.setType(animeDto.getType());

        if (anime.getAirStatus().equals("finished_airing")) {
            anime.setFinishedAt(DateUtil.now());
        }

        anime.setStartDate(animeDto.getStartDate());
        anime.setEndDate(animeDto.getEndDate());
        anime.setEnglishTitle(animeDto.getEnglishTitle());
        anime.setJapaneseTitle(animeDto.getJapaneseTitle());
        anime.setSynopsis(animeDto.getSynopsis());

        return anime;
    }
}
