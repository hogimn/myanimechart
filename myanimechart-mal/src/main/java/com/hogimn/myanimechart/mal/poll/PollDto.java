package com.hogimn.myanimechart.mal.poll;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollDto {
    private Long animeId;
    private Integer pollOptionId;
    private Long topicId;
    private Integer episode;
    private String title;
    private Integer votes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PollDto from(PollEntity pollEntity) {
        PollDto pollDto = new PollDto();
        pollDto.setAnimeId(pollEntity.getAnimeId());
        pollDto.setPollOptionId(pollEntity.getPollOptionId());
        pollDto.setTopicId(pollEntity.getTopicId());
        pollDto.setEpisode(pollEntity.getEpisode());
        pollDto.setTitle(pollEntity.getTitle());
        pollDto.setVotes(pollEntity.getVotes());
        pollDto.setCreatedAt(pollEntity.getCreatedAt());
        pollDto.setUpdatedAt(pollEntity.getUpdatedAt());
        return pollDto;
    }
}
