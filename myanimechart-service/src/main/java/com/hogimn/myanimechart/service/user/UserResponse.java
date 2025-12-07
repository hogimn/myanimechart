package com.hogimn.myanimechart.service.user;

import dev.katsute.mal4j.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String name;
    private String gender;
    private String pictureUrl;
    private Date birthday;
    private String location;
    private Date joinedAt;
    private Long joinedAtEpochMillis;
    private String timeZone;
    private boolean isSupporter;
    private UserAnimeStatisticsResponse userAnimeStatisticsResponse;

    public static UserResponse from(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setName(user.getName());
        userResponse.setGender(user.getGender());
        userResponse.setPictureUrl(user.getPictureURL());
        userResponse.setBirthday(user.getBirthday());
        userResponse.setLocation(user.getLocation());
        userResponse.setJoinedAt(user.getJoinedAt());
        userResponse.setJoinedAtEpochMillis(user.getJoinedAtEpochMillis());
        userResponse.setTimeZone(user.getTimeZone());
        userResponse.setSupporter(user.isSupporter());
        userResponse.setUserAnimeStatisticsResponse(UserAnimeStatisticsResponse.from(user.getAnimeStatistics()));
        return userResponse;
    }
}
