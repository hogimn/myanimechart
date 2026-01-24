package com.hogimn.myanimechart.service.user;

import dev.katsute.mal4j.user.User;

import java.util.Date;

public record UserResponse(
        String name,
        String gender,
        String pictureUrl,
        Date birthday,
        String location,
        Date joinedAt,
        Long joinedAtEpochMillis,
        String timeZone,
        boolean isSupporter,
        UserAnimeStatisticsResponse animeStatistics
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getName(),
                user.getGender(),
                user.getPictureURL(),
                user.getBirthday(),
                user.getLocation(),
                user.getJoinedAt(),
                user.getJoinedAtEpochMillis(),
                user.getTimeZone(),
                user.isSupporter(),
                UserAnimeStatisticsResponse.from(user.getAnimeStatistics())
        );
    }
}
