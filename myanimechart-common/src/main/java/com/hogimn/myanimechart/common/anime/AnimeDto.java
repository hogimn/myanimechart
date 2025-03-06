package com.hogimn.myanimechart.common.anime;

import com.hogimn.myanimechart.common.poll.PollDto;
import dev.katsute.mal4j.anime.Anime;
import dev.katsute.mal4j.property.Genre;
import dev.katsute.mal4j.property.IDN;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimeDto {
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

    private List<PollDto> polls;

    public static AnimeDto from(AnimeEntity animeEntity) {
        AnimeDto anime = new AnimeDto();
        anime.setId(animeEntity.getId());
        anime.setTitle(animeEntity.getTitle());
        anime.setSeason(animeEntity.getSeason());
        anime.setImage(animeEntity.getImage());
        anime.setLargeImage(animeEntity.getLargeImage());
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
        anime.setStartDate(animeEntity.getStartDate());
        anime.setEndDate(animeEntity.getEndDate());
        anime.setEnglishTitle(animeEntity.getEnglishTitle());
        anime.setJapaneseTitle(animeEntity.getJapaneseTitle());
        anime.setSynopsis(animeEntity.getSynopsis());
        anime.setRating(animeEntity.getRating());
        anime.setNsfw(animeEntity.getNsfw());
        anime.setForceCollect(animeEntity.getForceCollect());
        return anime;
    }

    public static AnimeDto from(Anime anime) {
        AnimeDto animeDto = new AnimeDto();
        animeDto.setId(anime.getID());
        animeDto.setTitle(anime.getTitle());
        animeDto.setSeason(anime.getStartSeason().getSeason().field());
        animeDto.setImage(anime.getMainPicture().getMediumURL());
        animeDto.setLargeImage(anime.getMainPicture().getLargeURL());
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
        animeDto.setAirStatus(anime.getStatus().field());
        animeDto.setTitle(anime.getTitle());
        animeDto.setType(anime.getType().field());
        animeDto.setStartDate(anime.getStartDate() != null ? anime.getStartDate().getDate() : null);
        animeDto.setEndDate(anime.getEndDate() != null ? anime.getEndDate().getDate() : null);
        animeDto.setEnglishTitle(anime.getAlternativeTitles().getEnglish());
        animeDto.setJapaneseTitle(anime.getAlternativeTitles().getJapanese());
        animeDto.setSynopsis(anime.getSynopsis());
        animeDto.setRating(anime.getRating().field());
        animeDto.setNsfw(anime.getNSFW().field());
        return animeDto;
    }
}
