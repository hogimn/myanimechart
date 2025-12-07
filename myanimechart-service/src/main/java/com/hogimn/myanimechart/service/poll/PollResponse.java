package com.hogimn.myanimechart.service.poll;

import com.hogimn.myanimechart.core.domain.poll.PollEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class PollResponse {
    private Long animeId;
    private Integer pollOptionId;
    private Long topicId;
    private Integer episode;
    private String title;
    private Integer votes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PollResponse from(PollEntity pollEntity) {
        PollResponse pollResponse = new PollResponse();
        pollResponse.setAnimeId(pollEntity.getAnimeId());
        pollResponse.setPollOptionId(pollEntity.getPollOptionId());
        pollResponse.setTopicId(pollEntity.getTopicId());
        pollResponse.setEpisode(pollEntity.getEpisode());
        pollResponse.setTitle(pollEntity.getTitle());
        pollResponse.setVotes(pollEntity.getVotes());
        pollResponse.setCreatedAt(pollEntity.getCreatedAt());
        pollResponse.setUpdatedAt(pollEntity.getUpdatedAt());
        return pollResponse;
    }
}
