package com.hogimn.myanimechart.database.domain;

import com.hogimn.myanimechart.database.dao.AnimeDao;
import dev.katsute.mal4j.property.Genre;
import dev.katsute.mal4j.property.IDN;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Anime {
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
    private List<AnimeStat> animeStats;

    public static Anime from(AnimeDao animeDao) {
        Anime anime = new Anime();
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
        return anime;
    }

    public static Anime from(dev.katsute.mal4j.anime.Anime katsuneAnime) {
        Anime anime = new Anime();
        anime.setId(katsuneAnime.getID());
        anime.setTitle(katsuneAnime.getTitle());
        anime.setSeason(katsuneAnime.getStartSeason().getSeason().field());
        anime.setImage(katsuneAnime.getMainPicture().getMediumURL());
        anime.setLink("https://myanimelist.net/anime/" + katsuneAnime.getID());
        anime.setScore(katsuneAnime.getMeanRating() != null ? katsuneAnime.getMeanRating().doubleValue() : 0.0);
        anime.setMembers(katsuneAnime.getUserListingCount());
        anime.setYear(katsuneAnime.getStartSeason().getYear());
        anime.setSource(katsuneAnime.getSource().field());
        anime.setGenre(Arrays.stream(katsuneAnime.getGenres()).map(Genre::getName).toList());
        anime.setStudios(Arrays.stream(katsuneAnime.getStudios()).map(IDN::getName).toList());
        return anime;
    }

}
