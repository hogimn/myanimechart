package com.hogimn.myanimechart.common.user;

import dev.katsute.mal4j.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String name;
    private String gender;
    private String pictureUrl;
    private Date birthday;
    private String location;
    private Date joinedAt;
    private Long joinedAtEpochMillis;
    private String timeZone;
    private boolean isSupporter;
    private UserAnimeStatisticsDto userAnimeStatisticsDto;

    public static UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setGender(user.getGender());
        userDto.setPictureUrl(user.getPictureURL());
        userDto.setBirthday(user.getBirthday());
        userDto.setLocation(user.getLocation());
        userDto.setJoinedAt(user.getJoinedAt());
        userDto.setJoinedAtEpochMillis(user.getJoinedAtEpochMillis());
        userDto.setTimeZone(user.getTimeZone());
        userDto.setSupporter(user.isSupporter());
        userDto.setUserAnimeStatisticsDto(UserAnimeStatisticsDto.from(user.getAnimeStatistics()));
        return userDto;
    }
}
