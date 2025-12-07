package com.hogimn.myanimechart.service.user;

import com.hogimn.myanimechart.core.common.apicalllog.ApiLoggable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @ApiLoggable
    @GetMapping
    public ResponseEntity<UserResponse> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @ApiLoggable
    @GetMapping("/anime-statuses")
    public ResponseEntity<List<AnimeListStatusResponse>> getAnimeStatuses() {
        return ResponseEntity.ok(userService.getAnimeStatuses());
    }

    @ApiLoggable
    @GetMapping("/anime-statuses/{id}")
    public ResponseEntity<AnimeListStatusResponse> getAnimeStatusById(@PathVariable int id) {
        return ResponseEntity.ok(userService.getAnimeStatusById(id));
    }

    @ApiLoggable
    @PostMapping("/anime-status/update")
    public ResponseEntity<Void> updateAnimeStatus(@RequestBody @Valid AnimeListStatusRequest request) {
        userService.updateAnimeStatus(request);
        return ResponseEntity.noContent().build();
    }

    @ApiLoggable
    @PostMapping("/anime-status/delete")
    public ResponseEntity<Void> deleteAnimeStatus(@RequestBody @Valid AnimeListStatusRequest request) {
        userService.deleteAnimeStatus(request);
        return ResponseEntity.noContent().build();
    }
}
