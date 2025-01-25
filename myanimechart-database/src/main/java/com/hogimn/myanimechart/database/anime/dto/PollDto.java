package com.hogimn.myanimechart.database.anime.dto;

import com.hogimn.myanimechart.database.anime.dao.PollDao;
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

    public static PollDto from(PollDao pollDao) {
        PollDto pollDto = new PollDto();
        pollDto.setAnimeId(pollDao.getAnime().getId());
        pollDto.setPollOptionId(pollDao.getPollOption().getId());
        pollDto.setTopicId(pollDao.getTopicId());
        pollDto.setEpisode(pollDao.getEpisode());
        pollDto.setTitle(pollDao.getTitle());
        pollDto.setVotes(pollDao.getVotes());
        pollDto.setCreatedAt(pollDao.getCreatedAt());
        pollDto.setUpdatedAt(pollDao.getUpdatedAt());
        return pollDto;
    }
}
