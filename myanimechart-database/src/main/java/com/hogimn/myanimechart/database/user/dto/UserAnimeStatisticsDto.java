package com.hogimn.myanimechart.database.user.dto;

import dev.katsute.mal4j.user.UserAnimeStatistics;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAnimeStatisticsDto {
    private Integer itemsWatching;
    private Integer itemsCompleted;
    private Integer itemsOnHold;
    private Integer itemsDropped;
    private Integer itemsPlanToWatch;
    private Integer items;
    private Float daysWatched;
    private Float daysWatching;
    private Float daysCompleted;
    private Float daysOnHold;
    private Float daysDropped;
    private Float days;
    private Integer episodes;
    private Integer timesRewatched;
    private Float meanScore;

    public static UserAnimeStatisticsDto from (UserAnimeStatistics userAnimeStatistics) {
        UserAnimeStatisticsDto userAnimeStatisticsDto = new UserAnimeStatisticsDto();
        userAnimeStatisticsDto.setItemsWatching(userAnimeStatistics.getItemsWatching());
        userAnimeStatisticsDto.setItemsCompleted(userAnimeStatisticsDto.getItemsCompleted());
        userAnimeStatisticsDto.setItemsOnHold(userAnimeStatistics.getItemsOnHold());
        userAnimeStatisticsDto.setItemsDropped(userAnimeStatistics.getItemsDropped());
        userAnimeStatisticsDto.setItemsPlanToWatch(userAnimeStatistics.getItemsPlanToWatch());
        userAnimeStatisticsDto.setItems(userAnimeStatistics.getItems());
        userAnimeStatisticsDto.setDaysWatched(userAnimeStatistics.getDaysWatched());
        userAnimeStatisticsDto.setDaysWatching(userAnimeStatistics.getDaysWatching());
        userAnimeStatisticsDto.setDaysCompleted(userAnimeStatistics.getDaysCompleted());
        userAnimeStatisticsDto.setDaysOnHold(userAnimeStatistics.getDaysOnHold());
        userAnimeStatisticsDto.setDaysDropped(userAnimeStatistics.getDaysDropped());
        userAnimeStatisticsDto.setDays(userAnimeStatistics.getDays());
        userAnimeStatisticsDto.setEpisodes(userAnimeStatistics.getEpisodes());
        userAnimeStatisticsDto.setTimesRewatched(userAnimeStatistics.getTimesRewatched());
        userAnimeStatisticsDto.setMeanScore(userAnimeStatistics.getMeanScore());
        return userAnimeStatisticsDto;
    }
}