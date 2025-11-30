package com.hogimn.myanimechart.mal.poll;

import com.hogimn.myanimechart.mal.anime.AnimeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollCollectionStatusDto {
    private Long animeId;
    private CollectionStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private LocalDateTime updatedAt;
    private AnimeDto animeDto;

    public static PollCollectionStatusDto from(PollCollectionStatusEntity entity) {
       PollCollectionStatusDto pollCollectionStatusDto = new PollCollectionStatusDto();
       pollCollectionStatusDto.setAnimeId(entity.getAnimeId());
       pollCollectionStatusDto.setStatus(entity.getStatus());
       pollCollectionStatusDto.setStartedAt(entity.getStartedAt());
       pollCollectionStatusDto.setFinishedAt(entity.getFinishedAt());
       pollCollectionStatusDto.setUpdatedAt(entity.getUpdatedAt());
       return pollCollectionStatusDto;
    }
}
