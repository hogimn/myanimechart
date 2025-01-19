package com.hogimn.myanimechart.database.anime.service;

import com.hogimn.myanimechart.database.anime.dao.PollOptionDao;
import com.hogimn.myanimechart.database.anime.domain.PollOption;
import com.hogimn.myanimechart.database.anime.repository.PollOptionRepository;
import com.hogimn.myanimechart.database.anime.repository.PollRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class PollOptionService {
    private final PollOptionRepository pollOptionRepository;

    public PollOptionService(PollOptionRepository pollOptionRepository) {
        this.pollOptionRepository = pollOptionRepository;
    }

    public PollOptionDao getPollOptionDao(String text) {
        Optional<PollOptionDao> option = pollOptionRepository.findByText(text);
        if (option.isPresent()) {
            return option.get();
        } else {
            throw new IllegalArgumentException("PollOption not found (" + text + ")");
        }
    }
}
