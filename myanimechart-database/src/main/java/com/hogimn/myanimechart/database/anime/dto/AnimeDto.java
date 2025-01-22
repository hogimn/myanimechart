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

    public static AnimeDto from(Anime katsuteAnime) {
        AnimeDto animeDto = new AnimeDto();
        animeDto.setId(katsuteAnime.getID());
        animeDto.setTitle(katsuteAnime.getTitle());
        animeDto.setSeason(katsuteAnime.getStartSeason().getSeason().field());
        animeDto.setImage(katsuteAnime.getMainPicture().getMediumURL());
        animeDto.setLink("https://myanimelist.net/anime/" + katsuteAnime.getID());
        animeDto.setScore(katsuteAnime.getMeanRating() != null ? katsuteAnime.getMeanRating().doubleValue() : 0.0);
        animeDto.setMembers(katsuteAnime.getUserListingCount());
        animeDto.setYear(katsuteAnime.getStartSeason().getYear());
        animeDto.setSource(katsuteAnime.getSource().field());
        animeDto.setGenre(Arrays.stream(katsuteAnime.getGenres()).map(Genre::getName).toList());
        animeDto.setStudios(Arrays.stream(katsuteAnime.getStudios()).map(IDN::getName).toList());
        animeDto.setRank(katsuteAnime.getRank());
        animeDto.setPopularity(katsuteAnime.getPopularity());
        animeDto.setScoringCount(katsuteAnime.getUserScoringCount());
        animeDto.setEpisodes(katsuteAnime.getEpisodes());
        animeDto.setAirStatus(!Objects.equals(katsuteAnime.getStatus().field(), "") ?
                katsuteAnime.getStatus().field() : katsuteAnime.getRawStatus());
        animeDto.setTitle(katsuteAnime.getTitle());
        animeDto.setType(!Objects.equals(katsuteAnime.getType().field(), "") ?
                katsuteAnime.getType().field() : katsuteAnime.getRawType());

        if (Objects.equals(animeDto.getAirStatus(), "finished_airing")) {
            animeDto.setFinishedAt(DateUtil.now());
        }

        animeDto.setStartDate(katsuteAnime.getStartDate() != null ? katsuteAnime.getStartDate().getDate() : null);
        animeDto.setEndDate(katsuteAnime.getEndDate() != null ? katsuteAnime.getEndDate().getDate() : null);
        animeDto.setEnglishTitle(katsuteAnime.getAlternativeTitles().getEnglish());
        animeDto.setJapaneseTitle(katsuteAnime.getAlternativeTitles().getJapanese());
        animeDto.setSynopsis(katsuteAnime.getSynopsis());

        return animeDto;
    }
}
