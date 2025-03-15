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
    @GetMapping("/findUser")
    public UserDto findUser() {
        return userService.findUserDtoByToken();
    }

    @ApiLoggable
    @GetMapping("/findAllUserAnimeStatuses")
    public List<AnimeListStatusDto> findAllUserAnimeStatuses() {
        return userService.findAllUserAnimeStatuses();
    }

    @ApiLoggable
    @GetMapping("/findUserAnimeStatusById")
    public AnimeListStatusDto findUserAnimeStatusById(@RequestParam("id") int id) {
        return userService.findAnimeListStatusDtoById(id);
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
