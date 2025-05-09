package com.hogimn.myanimechart.common.poll;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "anime_keyword_mapping")
@Data
public class AnimeKeywordMappingEntity {
    @Id
    private Long animeId;

    private String searchKeyword;
}
