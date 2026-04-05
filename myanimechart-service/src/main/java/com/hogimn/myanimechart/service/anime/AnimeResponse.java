package com.hogimn.myanimechart.service.anime;

import com.hogimn.myanimechart.core.domain.anime.AnimeEntity;
import com.hogimn.myanimechart.service.poll.PollResponse;
import dev.katsute.mal4j.anime.Anime;
import dev.katsute.mal4j.property.Genre;
import dev.katsute.mal4j.property.IDN;
import dev.katsute.mal4j.property.Picture;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.hogimn.myanimechart.core.common.util.StringUtil.safeSplit;

public record AnimeResponse(
        Long id,
        String title,
        String link,
        String image,
        String largeImage,
        Double score,
        Integer members,
        List<String> genre,
        List<String> studios,
        String source,
        Integer year,
        String season,
        Integer rank,
        Integer popularity,
        Integer scoringCount,
        Integer episodes,
        String airStatus,
        String type,
        Date startDate,
        Date endDate,
        String englishTitle,
        String japaneseTitle,
        String synopsis,
        String rating,
        String nsfw,
        List<PollResponse> polls
) {
    public static AnimeResponse from(AnimeEntity entity) {
        return from(entity, List.of());
    }

    public static AnimeResponse from(AnimeEntity entity, List<PollResponse> polls) {
        return new AnimeResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getLink(),
                entity.getImage(),
                entity.getLargeImage(),
                entity.getScore(),
                entity.getMembers(),
                safeSplit(entity.getGenre()),
                safeSplit(entity.getStudios()),
                entity.getSource(),
                entity.getYear(),
                entity.getSeason(),
                entity.getRank(),
                entity.getPopularity(),
                entity.getScoringCount(),
                entity.getEpisodes(),
                entity.getAirStatus(),
                entity.getType(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getEnglishTitle(),
                entity.getJapaneseTitle(),
                entity.getSynopsis(),
                entity.getRating(),
                entity.getNsfw(),
                polls
        );
    }

    public static AnimeResponse from(Anime anime) {
        Picture picture = anime.getMainPicture();
        String mediumUrl = picture != null ? picture.getMediumURL() : null;
        String largeUrl = picture != null ? picture.getLargeURL() : null;

        var startSeason = anime.getStartSeason();
        Integer year = null;
        String season = null;
        if (startSeason != null) {
            year = startSeason.getYear();
            var malSeason = startSeason.getSeason();
            season = malSeason != null ? malSeason.field() : null;
        }

        Genre[] genres = anime.getGenres();
        List<String> genreNames = genres == null
                ? List.of()
                : Arrays.stream(genres).map(Genre::getName).filter(Objects::nonNull).toList();

        var studios = anime.getStudios();
        List<String> studioNames = studios == null
                ? List.of()
                : Arrays.stream(studios).map(IDN::getName).filter(Objects::nonNull).toList();

        var altTitles = anime.getAlternativeTitles();

        return new AnimeResponse(
                anime.getID(),
                anime.getTitle(),
                "https://myanimelist.net/anime/" + anime.getID(),
                mediumUrl,
                largeUrl,
                anime.getMeanRating() != null ? anime.getMeanRating().doubleValue() : 0.0,
                anime.getUserListingCount(),
                genreNames,
                studioNames,
                anime.getSource() != null ? anime.getSource().field() : anime.getRawSource(),
                year,
                season,
                anime.getRank(),
                anime.getPopularity(),
                anime.getUserScoringCount(),
                anime.getEpisodes(),
                anime.getStatus() != null ? anime.getStatus().field() : anime.getRawStatus(),
                anime.getType() != null ? anime.getType().field() : anime.getRawType(),
                anime.getStartDate() != null ? anime.getStartDate().getDate() : null,
                anime.getEndDate() != null ? anime.getEndDate().getDate() : null,
                altTitles != null ? altTitles.getEnglish() : null,
                altTitles != null ? altTitles.getJapanese() : null,
                anime.getSynopsis(),
                anime.getRating() != null ? anime.getRating().field() : anime.getRawRating(),
                anime.getNSFW() != null ? anime.getNSFW().field() : anime.getRawNSFW(),
                List.of()
        );
    }
}