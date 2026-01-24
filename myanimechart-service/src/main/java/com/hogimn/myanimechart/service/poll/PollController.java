package com.hogimn.myanimechart.service.poll;

import com.hogimn.myanimechart.core.common.apicalllog.ApiLoggable;
import com.hogimn.myanimechart.core.common.result.SaveResult;
import com.hogimn.myanimechart.service.poll.status.PollCollectionStatusRequest;
import com.hogimn.myanimechart.service.poll.status.PollCollectionStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/poll")
@Slf4j
@RequiredArgsConstructor
public class PollController {
    private final PollService pollService;
    private final PollCollectionStatusService pollCollectionStatusService;

    @ApiLoggable
    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid PollCreateRequest request) {
        SaveResult result = pollService.save(request);
        if (result == SaveResult.CREATED) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @ApiLoggable
    @PostMapping("/collection-status")
    public ResponseEntity<Void> savePollCollectionStatus(@RequestBody @Valid PollCollectionStatusRequest request) {
        SaveResult result = pollCollectionStatusService.save(request);
        if (result == SaveResult.CREATED) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
