package com.hogimn.myanimechart.service.user;

import dev.katsute.mal4j.user.UserAnimeStatistics;

public record UserAnimeStatisticsResponse(
        Integer itemsWatching,
        Integer itemsCompleted,
        Integer itemsOnHold,
        Integer itemsDropped,
        Integer itemsPlanToWatch,
        Integer items,
        Float daysWatched,
        Float daysWatching,
        Float daysCompleted,
        Float daysOnHold,
        Float daysDropped,
        Float days,
        Integer episodes,
        Integer timesRewatched,
        Float meanScore
) {
    public static UserAnimeStatisticsResponse from(UserAnimeStatistics stats) {
        return new UserAnimeStatisticsResponse(
                stats.getItemsWatching(),
                stats.getItemsCompleted(),
                stats.getItemsOnHold(),
                stats.getItemsDropped(),
                stats.getItemsPlanToWatch(),
                stats.getItems(),
                stats.getDaysWatched(),
                stats.getDaysWatching(),
                stats.getDaysCompleted(),
                stats.getDaysOnHold(),
                stats.getDaysDropped(),
                stats.getDays(),
                stats.getEpisodes(),
                stats.getTimesRewatched(),
                stats.getMeanScore()
        );
    }
}
