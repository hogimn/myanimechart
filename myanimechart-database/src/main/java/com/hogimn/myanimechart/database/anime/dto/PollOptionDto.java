package com.hogimn.myanimechart.database.anime.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollOptionDto {
    private Integer id;
    private String text;
}
