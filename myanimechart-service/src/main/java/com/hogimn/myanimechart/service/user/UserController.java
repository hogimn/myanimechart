package com.hogimn.myanimechart.service.user;

import com.hogimn.myanimechart.core.common.apicalllog.ApiLoggable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping
    public UserDto getCurrentUser() {
        return userService.getCurrentUser();
    }

    @ApiLoggable
    @GetMapping("/anime-statuses")
    public List<AnimeListStatusDto> getAnimeStatuses() {
        return userService.getAnimeStatuses();
    }

    @ApiLoggable
    @GetMapping("/anime-status")
    public AnimeListStatusDto getAnimeStatusById(@RequestParam("id") int id) {
        return userService.getAnimeStatusById(id);
    }

    @ApiLoggable
    @PostMapping("/anime-status/update")
    public void updateAnimeStatus(@RequestBody AnimeListStatusDto animeListStatusDto) {
        userService.updateAnimeStatus(animeListStatusDto);
    }

    @ApiLoggable
    @PostMapping("/anime-status/delete")
    public void deleteAnimeStatus(@RequestBody AnimeListStatusDto animeListStatusDto) {
        userService.deleteAnimeStatus(animeListStatusDto);
    }
}
