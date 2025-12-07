package com.hogimn.myanimechart.service.poll.option;

import com.hogimn.myanimechart.core.domain.poll.option.PollOptionEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollOptionResponse {
    private Integer id;
    private String text;

    public static PollOptionResponse from(PollOptionEntity entity) {
        PollOptionResponse response = new PollOptionResponse();
        response.setId(entity.getId());
        response.setText(entity.getText());
        return response;
    }
}
