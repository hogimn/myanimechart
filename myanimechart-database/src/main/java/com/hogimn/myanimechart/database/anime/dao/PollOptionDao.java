package com.hogimn.myanimechart.database.anime.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "poll_option")
@Data
public class PollOptionDao {
    @Id
    private Integer id;
    private String text;
}
