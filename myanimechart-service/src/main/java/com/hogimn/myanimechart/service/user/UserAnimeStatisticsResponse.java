package com.hogimn.myanimechart.service.user;

import dev.katsute.mal4j.user.UserAnimeStatistics;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAnimeStatisticsResponse {
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

    public static UserAnimeStatisticsResponse from(UserAnimeStatistics userAnimeStatistics) {
        UserAnimeStatisticsResponse userAnimeStatisticsResponse = new UserAnimeStatisticsResponse();
        userAnimeStatisticsResponse.setItemsWatching(userAnimeStatistics.getItemsWatching());
        userAnimeStatisticsResponse.setItemsCompleted(userAnimeStatisticsResponse.getItemsCompleted());
        userAnimeStatisticsResponse.setItemsOnHold(userAnimeStatistics.getItemsOnHold());
        userAnimeStatisticsResponse.setItemsDropped(userAnimeStatistics.getItemsDropped());
        userAnimeStatisticsResponse.setItemsPlanToWatch(userAnimeStatistics.getItemsPlanToWatch());
        userAnimeStatisticsResponse.setItems(userAnimeStatistics.getItems());
        userAnimeStatisticsResponse.setDaysWatched(userAnimeStatistics.getDaysWatched());
        userAnimeStatisticsResponse.setDaysWatching(userAnimeStatistics.getDaysWatching());
        userAnimeStatisticsResponse.setDaysCompleted(userAnimeStatistics.getDaysCompleted());
        userAnimeStatisticsResponse.setDaysOnHold(userAnimeStatistics.getDaysOnHold());
        userAnimeStatisticsResponse.setDaysDropped(userAnimeStatistics.getDaysDropped());
        userAnimeStatisticsResponse.setDays(userAnimeStatistics.getDays());
        userAnimeStatisticsResponse.setEpisodes(userAnimeStatistics.getEpisodes());
        userAnimeStatisticsResponse.setTimesRewatched(userAnimeStatistics.getTimesRewatched());
        userAnimeStatisticsResponse.setMeanScore(userAnimeStatistics.getMeanScore());
        return userAnimeStatisticsResponse;
    }
}