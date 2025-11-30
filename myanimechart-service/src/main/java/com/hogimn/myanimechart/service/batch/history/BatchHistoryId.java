package com.hogimn.myanimechart.service.batch.history;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchHistoryId implements Serializable {
    private String name;
    private LocalDateTime recordedAt;
}
