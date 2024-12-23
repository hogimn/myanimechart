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

    public static Anime from(dev.katsute.mal4j.anime.Anime katsuteAnime) {
        Anime anime = new Anime();
        anime.setId(katsuteAnime.getID());
        anime.setTitle(katsuteAnime.getTitle());
        anime.setSeason(katsuteAnime.getStartSeason().getSeason().field());
        anime.setImage(katsuteAnime.getMainPicture().getMediumURL());
        anime.setLink("https://myanimelist.net/anime/" + katsuteAnime.getID());
        anime.setScore(katsuteAnime.getMeanRating() != null ? katsuteAnime.getMeanRating().doubleValue() : 0.0);
        anime.setMembers(katsuteAnime.getUserListingCount());
        anime.setYear(katsuteAnime.getStartSeason().getYear());
        anime.setSource(katsuteAnime.getSource().field());
        anime.setGenre(Arrays.stream(katsuteAnime.getGenres()).map(Genre::getName).toList());
        anime.setStudios(Arrays.stream(katsuteAnime.getStudios()).map(IDN::getName).toList());
        return anime;
    }

}
