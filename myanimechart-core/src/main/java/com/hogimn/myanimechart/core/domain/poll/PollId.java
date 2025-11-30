package com.hogimn.myanimechart.core.domain.poll;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollId implements Serializable {
    private Long animeId;
    private Integer pollOptionId;
    private Long topicId;
}
