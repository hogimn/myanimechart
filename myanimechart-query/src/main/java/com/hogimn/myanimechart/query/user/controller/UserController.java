package com.hogimn.myanimechart.query.user.controller;

import com.hogimn.myanimechart.database.user.dto.AnimeListStatusDto;
import com.hogimn.myanimechart.database.user.dto.UserDto;
import com.hogimn.myanimechart.query.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getUser")
    public UserDto getUser() {
        return userService.getUserDto();
    }

    @GetMapping("/getUserAnimeStatusListByYearAndSeason/{year}/{season}")
    public List<AnimeListStatusDto> getUserAnimeStatusListByYearAndSeason(@PathVariable int year, @PathVariable String season) {
        return userService.getUserAnimeListStatusDtosByYearAndSeason(year, season);
    }

    @GetMapping("/getUserAnimeStatusById")
    public AnimeListStatusDto getUserAnimeStatusById(@RequestParam("id") int id) {
        return userService.getAnimeListStatusDtoById(id);
    }

    @PostMapping("/updateUserAnimeStatus")
    public void updateUserAnimeStatus(@RequestBody AnimeListStatusDto animeListStatusDto) {
        userService.updateUserAnimeStatus(animeListStatusDto);
    }

    @PostMapping("/deleteUserAnimeStatus")
    public void deleteUserAnimeStatus(@RequestBody AnimeListStatusDto animeListStatusDto) {
        userService.deleteUserAnimeStatus(animeListStatusDto);
    }
}
