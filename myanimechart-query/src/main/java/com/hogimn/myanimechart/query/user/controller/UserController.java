package com.hogimn.myanimechart.query.user.controller;

import com.hogimn.myanimechart.database.user.dto.UserDto;
import com.hogimn.myanimechart.query.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getUser")
    public UserDto getAnimeByTitle() {
        return userService.getUserDto();
    }
}
