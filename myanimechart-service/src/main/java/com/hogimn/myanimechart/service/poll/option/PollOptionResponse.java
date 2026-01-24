package com.hogimn.myanimechart.service.poll.option;

import com.hogimn.myanimechart.core.domain.poll.option.PollOptionEntity;

public record PollOptionResponse(Integer id, String text) {
    public static PollOptionResponse from(PollOptionEntity entity) {
        return new PollOptionResponse(entity.getId(), entity.getText());
    }
}
