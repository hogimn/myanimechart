package com.hogimn.myanimechart.core.domain.poll;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class PollId implements Serializable {
    private Long animeId;
    private Integer pollOptionId;
    private Long topicId;
}
