package com.hogimn.myanimechart.service.batch.history;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class BatchHistoryId implements Serializable {
    private String name;
    private LocalDateTime recordedAt;
}
