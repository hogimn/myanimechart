package com.hogimn.myanimechart.core.domain.poll.option;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "poll_option")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PollOptionEntity {
    @Id
    private Integer id;

    private String text;
}
