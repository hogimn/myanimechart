package com.hogimn.myanimechart.database.anime.dao.key;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollId implements Serializable {
    private Long anime;
    private Integer pollOption;
    private Long topicId;
}
