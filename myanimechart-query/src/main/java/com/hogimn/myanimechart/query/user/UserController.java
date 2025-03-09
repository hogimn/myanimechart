package com.hogimn.myanimechart.query.user;

import com.hogimn.myanimechart.common.apicalllog.ApiLoggable;
import com.hogimn.myanimechart.common.user.AnimeListStatusDto;
import com.hogimn.myanimechart.common.user.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiLoggable
    @GetMapping("/getUser")
    public UserDto getUser() {
        return userService.getUserDto();
    }

    @ApiLoggable
    @GetMapping("/getUserAnimeStatusListByYearAndSeason/{year}/{season}")
    public List<AnimeListStatusDto> getUserAnimeStatusListByYearAndSeason(
            @PathVariable int year, @PathVariable String season
    ) throws InterruptedException {
        return userService.getUserAnimeListStatusDtosByYearAndSeason(year, season);
    }

    @ApiLoggable
    @GetMapping("/getUserAnimeStatusById")
    public AnimeListStatusDto getUserAnimeStatusById(@RequestParam("id") int id)
            throws InterruptedException {
        return userService.getAnimeListStatusDtoById(id);
    }

    @ApiLoggable
    @PostMapping("/updateUserAnimeStatus")
    public void updateUserAnimeStatus(@RequestBody AnimeListStatusDto animeListStatusDto) {
        userService.updateUserAnimeStatus(animeListStatusDto);
    }

    @ApiLoggable
    @PostMapping("/deleteUserAnimeStatus")
    public void deleteUserAnimeStatus(@RequestBody AnimeListStatusDto animeListStatusDto) {
        userService.deleteUserAnimeStatus(animeListStatusDto);
    }
}
