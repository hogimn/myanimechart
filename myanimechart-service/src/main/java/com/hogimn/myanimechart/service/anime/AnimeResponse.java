package com.hogimn.myanimechart.service.anime;

import com.hogimn.myanimechart.core.domain.anime.AnimeEntity;
import com.hogimn.myanimechart.service.poll.PollResponse;
import dev.katsute.mal4j.anime.Anime;
import dev.katsute.mal4j.property.Genre;
import dev.katsute.mal4j.property.IDN;
import lombok.Data;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
public class AnimeResponse {
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

    private List<PollResponse> polls;

    public static AnimeResponse from(AnimeEntity animeEntity) {
        AnimeResponse anime = new AnimeResponse();
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
        return anime;
    }

    public static AnimeResponse from(Anime anime) {
        AnimeResponse animeResponse = new AnimeResponse();
        animeResponse.setId(anime.getID());
        animeResponse.setTitle(anime.getTitle());
        animeResponse.setSeason(anime.getStartSeason().getSeason().field());
        animeResponse.setImage(anime.getMainPicture().getMediumURL());
        animeResponse.setLargeImage(anime.getMainPicture().getLargeURL());
        animeResponse.setLink("https://myanimelist.net/anime/" + anime.getID());
        animeResponse.setScore(anime.getMeanRating() != null ? anime.getMeanRating().doubleValue() : 0.0);
        animeResponse.setMembers(anime.getUserListingCount());
        animeResponse.setYear(anime.getStartSeason().getYear());
        animeResponse.setSource(anime.getSource() != null ? anime.getSource().field() : anime.getRawSource());
        animeResponse.setGenre(Arrays.stream(anime.getGenres()).map(Genre::getName).toList());
        animeResponse.setStudios(Arrays.stream(anime.getStudios()).map(IDN::getName).toList());
        animeResponse.setRank(anime.getRank());
        animeResponse.setPopularity(anime.getPopularity());
        animeResponse.setScoringCount(anime.getUserScoringCount());
        animeResponse.setEpisodes(anime.getEpisodes());
        animeResponse.setAirStatus(anime.getStatus() != null ? anime.getStatus().field() : anime.getRawStatus());
        animeResponse.setTitle(anime.getTitle());
        animeResponse.setType(anime.getType() != null ? anime.getType().field() : anime.getRawType());
        animeResponse.setStartDate(anime.getStartDate() != null ? anime.getStartDate().getDate() : null);
        animeResponse.setEndDate(anime.getEndDate() != null ? anime.getEndDate().getDate() : null);
        animeResponse.setEnglishTitle(anime.getAlternativeTitles().getEnglish());
        animeResponse.setJapaneseTitle(anime.getAlternativeTitles().getJapanese());
        animeResponse.setSynopsis(anime.getSynopsis());
        animeResponse.setRating(anime.getRating() != null ? anime.getRating().field() : anime.getRawRating());
        animeResponse.setNsfw(anime.getNSFW() != null ? anime.getNSFW().field() : anime.getRawNSFW());
        return animeResponse;
    }
}
