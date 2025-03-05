package com.hogimn.myanimechart.database.poll;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollId implements Serializable {
    private Long anime;
    private Integer pollOption;
    private Long topicId;
}
