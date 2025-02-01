package com.hogimn.myanimechart.database.anime.entity;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.anime.entity.key.AnimeStatId;
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
AnimeStatEntity {
    @Id
    @ManyToOne
    @JoinColumn(name = "anime_id", referencedColumnName = "id")
    private AnimeEntity anime;

    private int members;
    private double score;
    private Integer popularity;
    private Integer scoringCount;
    private Integer rank;

    @Id
    private LocalDateTime recordedAt;

    public static AnimeStatEntity from(AnimeEntity animeEntity) {
        AnimeStatEntity animeStatEntity = new AnimeStatEntity();
        animeStatEntity.anime = animeEntity;
        animeStatEntity.members = animeEntity.getMembers();
        animeStatEntity.score = animeEntity.getScore();
        animeStatEntity.recordedAt = DateUtil.now();
        animeStatEntity.popularity = animeEntity.getPopularity();
        animeStatEntity.scoringCount = animeEntity.getScoringCount();
        animeStatEntity.rank = animeEntity.getRank();
        return animeStatEntity;
    }
}
