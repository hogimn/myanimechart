package com.hogimn.myanimechart.database.service;

import com.hogimn.myanimechart.database.dao.AnimeDao;
import com.hogimn.myanimechart.database.domain.Anime;
import com.hogimn.myanimechart.database.repository.AnimeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AnimeService {
    private final AnimeRepository animeRepository;

    public AnimeService(AnimeRepository animeRepository) {
        this.animeRepository = animeRepository;
    }

    public AnimeDao upsertAnime(Anime anime) {
        if (anime == null) {
            return null;
        }

        AnimeDao animeDao = AnimeDao.from(anime);
        Optional<AnimeDao> optional = animeRepository.findByTitle(anime.getTitle());

        try {
            if (optional.isPresent()) {
                AnimeDao foundAnime = optional.get();
                foundAnime.setFrom(anime);
                animeRepository.save(foundAnime);
                return foundAnime;
            } else {
                animeRepository.save(animeDao);
                return animeDao;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error(anime.toString());
            return null;
        }
    }
}
