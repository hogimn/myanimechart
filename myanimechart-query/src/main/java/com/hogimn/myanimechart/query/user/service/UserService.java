package com.hogimn.myanimechart.query.user.service;

import com.hogimn.myanimechart.common.myanimelist.MyAnimeListProvider;
import com.hogimn.myanimechart.database.user.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    private final MyAnimeListProvider myAnimeListProvider;

    public UserService(MyAnimeListProvider myAnimeListProvider) {
        this.myAnimeListProvider = myAnimeListProvider;
    }

    public UserDto getUserDto() {
        return UserDto.from(myAnimeListProvider.getMyAnimeListWithToken().getAuthenticatedUser());
    }
}
