package com.hogimn.myanimechart.database.batch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchHistoryId implements Serializable {
    private String batch;
    private LocalDateTime recordedAt;
}
