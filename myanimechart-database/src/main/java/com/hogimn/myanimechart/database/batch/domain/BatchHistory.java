package com.hogimn.myanimechart.database.batch.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchHistory {
    private String name;
    private LocalDateTime recordedAt;
}
