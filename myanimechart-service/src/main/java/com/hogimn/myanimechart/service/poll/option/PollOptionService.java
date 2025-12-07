package com.hogimn.myanimechart.service.poll.option;

import com.hogimn.myanimechart.core.domain.poll.option.PollOptionRepository;
import com.hogimn.myanimechart.service.exception.PollOptionNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PollOptionService {
    private final PollOptionRepository pollOptionRepository;

    public PollOptionResponse getPollOptionByText(String text) {
        return pollOptionRepository.findByText(text)
                .map(PollOptionResponse::from)
                .orElseThrow(() -> new PollOptionNotFoundException(text));
    }

    public PollOptionResponse getPollOptionById(int id) {
        return pollOptionRepository.findById(id)
                .map(PollOptionResponse::from)
                .orElseThrow(() -> new PollOptionNotFoundException(id));
    }
}
