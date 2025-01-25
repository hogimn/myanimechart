package com.hogimn.myanimechart.database.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchHistoryDto {
    private String name;
    private LocalDateTime recordedAt;
}
