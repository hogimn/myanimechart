package com.hogimn.myanimechart.service.anime;

import com.hogimn.myanimechart.core.domain.anime.AnimeEntity;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

public record AnimeCreateRequest(
        @NotNull(message = "id is required.")
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
        String nsfw
) {
    public AnimeEntity toEntity() {
        AnimeEntity anime = new AnimeEntity();
        anime.setId(id());
        anime.setTitle(title());
        anime.setSeason(season());
        anime.setImage(image());
        anime.setLargeImage(largeImage());
        anime.setLink(link());
        anime.setScore(score());
        anime.setMembers(members());
        anime.setYear(year());
        anime.setSource(source());
        anime.setGenre(genre() != null ? String.join(", ", genre()) : "");
        anime.setStudios(studios() != null ? String.join(", ", studios()) : "");
        anime.setRank(rank());
        anime.setPopularity(popularity());
        anime.setScoringCount(scoringCount());
        anime.setEpisodes(episodes());
        anime.setAirStatus(airStatus());
        anime.setType(type());
        anime.setStartDate(startDate());
        anime.setEndDate(endDate());
        anime.setEnglishTitle(englishTitle());
        anime.setJapaneseTitle(japaneseTitle());
        anime.setSynopsis(synopsis());
        anime.setRating(rating());
        anime.setNsfw(nsfw());
        return anime;
    }
}