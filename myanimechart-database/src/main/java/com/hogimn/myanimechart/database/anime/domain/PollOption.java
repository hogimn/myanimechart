package com.hogimn.myanimechart.database.anime.domain;

import com.hogimn.myanimechart.database.anime.dao.PollOptionDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollOption {
    private Integer id;
    private String text;

    public static PollOption from(PollOptionDao pollOptionDao) {
        PollOption pollOption = new PollOption();
        pollOption.setId(pollOptionDao.getId());
        pollOption.setText(pollOptionDao.getText());
        return pollOption;
    }
}
