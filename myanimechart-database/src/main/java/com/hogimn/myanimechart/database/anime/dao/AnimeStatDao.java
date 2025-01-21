package com.hogimn.myanimechart.database.anime.dao;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.anime.dao.key.AnimeStatId;
import com.hogimn.myanimechart.database.anime.dto.AnimeDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "anime_stat")
@Data
@IdClass(AnimeStatId.class)
public class
AnimeStatDao {
    @Id
    @ManyToOne
    @JoinColumn(name = "anime_id", referencedColumnName = "id")
    private AnimeDao anime;

    private int members;
    private double score;
    private Integer popularity;
    private Integer scoringCount;
    private Integer rank;

    @Id
    private LocalDateTime recordedAt;

    public static AnimeStatDao from(AnimeDto animeDto) {
        AnimeStatDao animeDao = new AnimeStatDao();
        animeDao.anime = AnimeDao.from(animeDto);
        animeDao.members = animeDto.getMembers();
        animeDao.score = animeDto.getScore();
        animeDao.recordedAt = DateUtil.now();
        animeDao.popularity = animeDto.getPopularity();
        animeDao.scoringCount = animeDto.getScoringCount();
        animeDao.rank = animeDto.getRank();
        return animeDao;
    }
}
