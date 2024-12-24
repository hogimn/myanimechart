package com.hogimn.myanimechart.database.dao.key;

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
