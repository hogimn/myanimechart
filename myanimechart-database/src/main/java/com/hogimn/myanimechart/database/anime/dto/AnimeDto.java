package com.hogimn.myanimechart.database.anime.dto;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.anime.dao.AnimeDao;
import dev.katsute.mal4j.anime.Anime;
import dev.katsute.mal4j.property.Genre;
import dev.katsute.mal4j.property.IDN;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimeDto {
    private Long id;
    private String title;
    private String link;
    private String image;
    private Double score;
    private Integer members;
    private List<String> genre;
    private List<String> studios;
    private String source;
    private Integer year;
    private String season;
    private List<AnimeStatDto> animeStats;
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

    public static AnimeDto from(AnimeDao animeDao) {
        AnimeDto anime = new AnimeDto();
        anime.setId(animeDao.getId());
        anime.setTitle(animeDao.getTitle());
        anime.setSeason(animeDao.getSeason());
        anime.setImage(animeDao.getImage());
        anime.setLink(animeDao.getLink());
        anime.setScore(animeDao.getScore());
        anime.setMembers(animeDao.getMembers());
        anime.setYear(animeDao.getYear());
        anime.setSource(animeDao.getSource());
        anime.setGenre(Arrays.asList(animeDao.getGenre().split(",")));
        anime.setStudios(Arrays.asList(animeDao.getStudios().split(",")));
        anime.setRank(animeDao.getRank());
        anime.setPopularity(animeDao.getPopularity());
        anime.setScoringCount(animeDao.getScoringCount());
        anime.setEpisodes(animeDao.getEpisodes());
        anime.setAirStatus(animeDao.getAirStatus());
        anime.setTitle(animeDao.getTitle());
        anime.setType(animeDao.getType());
        anime.setFinishedAt(animeDao.getFinishedAt());
        anime.setStartDate(animeDao.getStartDate());
        anime.setEndDate(animeDao.getEndDate());
        anime.setEnglishTitle(animeDao.getEnglishTitle());
        anime.setJapaneseTitle(animeDao.getJapaneseTitle());
        anime.setSynopsis(animeDao.getSynopsis());
        return anime;
    }

    public static AnimeDto from(Anime anime) {
        AnimeDto animeDto = new AnimeDto();
        animeDto.setId(anime.getID());
        animeDto.setTitle(anime.getTitle());
        animeDto.setSeason(anime.getStartSeason().getSeason().field());
        animeDto.setImage(anime.getMainPicture().getMediumURL());
        animeDto.setLink("https://myanimelist.net/anime/" + anime.getID());
        animeDto.setScore(anime.getMeanRating() != null ? anime.getMeanRating().doubleValue() : 0.0);
        animeDto.setMembers(anime.getUserListingCount());
        animeDto.setYear(anime.getStartSeason().getYear());
        animeDto.setSource(anime.getSource().field());
        animeDto.setGenre(Arrays.stream(anime.getGenres()).map(Genre::getName).toList());
        animeDto.setStudios(Arrays.stream(anime.getStudios()).map(IDN::getName).toList());
        animeDto.setRank(anime.getRank());
        animeDto.setPopularity(anime.getPopularity());
        animeDto.setScoringCount(anime.getUserScoringCount());
        animeDto.setEpisodes(anime.getEpisodes());
        animeDto.setAirStatus(!Objects.equals(anime.getStatus().field(), "") ?
                anime.getStatus().field() : anime.getRawStatus());
        animeDto.setTitle(anime.getTitle());
        animeDto.setType(!Objects.equals(anime.getType().field(), "") ?
                anime.getType().field() : anime.getRawType());

        if (Objects.equals(animeDto.getAirStatus(), "finished_airing")) {
            animeDto.setFinishedAt(DateUtil.now());
        }

        animeDto.setStartDate(anime.getStartDate() != null ? anime.getStartDate().getDate() : null);
        animeDto.setEndDate(anime.getEndDate() != null ? anime.getEndDate().getDate() : null);
        animeDto.setEnglishTitle(anime.getAlternativeTitles().getEnglish());
        animeDto.setJapaneseTitle(anime.getAlternativeTitles().getJapanese());
        animeDto.setSynopsis(anime.getSynopsis());

        return animeDto;
    }
}
