package com.hogimn.myanimechart.database.dao.key;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimeStatId implements Serializable {
    private Long anime;
    private LocalDateTime recordedAt;
}
