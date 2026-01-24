package com.hogimn.myanimechart.service.anime;

import com.hogimn.myanimechart.core.domain.anime.AnimeEntity;
import com.hogimn.myanimechart.service.poll.PollResponse;
import dev.katsute.mal4j.anime.Anime;
import dev.katsute.mal4j.property.Genre;
import dev.katsute.mal4j.property.IDN;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
        return new AnimeResponse(
                anime.getID(),
                anime.getTitle(),
                "https://myanimelist.net/anime/" + anime.getID(),
                anime.getMainPicture().getMediumURL(),
                anime.getMainPicture().getLargeURL(),
                anime.getMeanRating() != null ? anime.getMeanRating().doubleValue() : 0.0,
                anime.getUserListingCount(),
                Arrays.stream(anime.getGenres()).map(Genre::getName).toList(),
                Arrays.stream(anime.getStudios()).map(IDN::getName).toList(),
                anime.getSource() != null ? anime.getSource().field() : anime.getRawSource(),
                anime.getStartSeason().getYear(),
                anime.getStartSeason().getSeason().field(),
                anime.getRank(),
                anime.getPopularity(),
                anime.getUserScoringCount(),
                anime.getEpisodes(),
                anime.getStatus() != null ? anime.getStatus().field() : anime.getRawStatus(),
                anime.getType() != null ? anime.getType().field() : anime.getRawType(),
                anime.getStartDate() != null ? anime.getStartDate().getDate() : null,
                anime.getEndDate() != null ? anime.getEndDate().getDate() : null,
                anime.getAlternativeTitles().getEnglish(),
                anime.getAlternativeTitles().getJapanese(),
                anime.getSynopsis(),
                anime.getRating() != null ? anime.getRating().field() : anime.getRawRating(),
                anime.getNSFW() != null ? anime.getNSFW().field() : anime.getRawNSFW(),
                List.of()
        );
    }
}