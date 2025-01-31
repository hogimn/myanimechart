package com.hogimn.myanimechart.database.anime.dto;

import com.hogimn.myanimechart.database.anime.entity.AnimeEntity;
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

    private List<AnimeStatDto> animeStats;
    private List<PollDto> polls;

    public static AnimeDto from(AnimeEntity animeEntity) {
        AnimeDto anime = new AnimeDto();
        anime.setId(animeEntity.getId());
        anime.setTitle(animeEntity.getTitle());
        anime.setSeason(animeEntity.getSeason());
        anime.setImage(animeEntity.getImage());
        anime.setLink(animeEntity.getLink());
        anime.setScore(animeEntity.getScore());
        anime.setMembers(animeEntity.getMembers());
        anime.setYear(animeEntity.getYear());
        anime.setSource(animeEntity.getSource());
        anime.setGenre(Arrays.asList(animeEntity.getGenre().split(",")));
        anime.setStudios(Arrays.asList(animeEntity.getStudios().split(",")));
        anime.setRank(animeEntity.getRank());
        anime.setPopularity(animeEntity.getPopularity());
        anime.setScoringCount(animeEntity.getScoringCount());
        anime.setEpisodes(animeEntity.getEpisodes());
        anime.setAirStatus(animeEntity.getAirStatus());
        anime.setTitle(animeEntity.getTitle());
        anime.setType(animeEntity.getType());
        anime.setFinishedAt(animeEntity.getFinishedAt());
        anime.setStartDate(animeEntity.getStartDate());
        anime.setEndDate(animeEntity.getEndDate());
        anime.setEnglishTitle(animeEntity.getEnglishTitle());
        anime.setJapaneseTitle(animeEntity.getJapaneseTitle());
        anime.setSynopsis(animeEntity.getSynopsis());
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

        animeDto.setStartDate(anime.getStartDate() != null ? anime.getStartDate().getDate() : null);
        animeDto.setEndDate(anime.getEndDate() != null ? anime.getEndDate().getDate() : null);
        animeDto.setEnglishTitle(anime.getAlternativeTitles().getEnglish());
        animeDto.setJapaneseTitle(anime.getAlternativeTitles().getJapanese());
        animeDto.setSynopsis(anime.getSynopsis());

        return animeDto;
    }
}
