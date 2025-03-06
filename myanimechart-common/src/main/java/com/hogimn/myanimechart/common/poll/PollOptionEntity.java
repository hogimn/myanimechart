package com.hogimn.myanimechart.common.poll;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "poll_option")
@Data
public class PollOptionEntity {
    @Id
    private Integer id;
    private String text;
}
