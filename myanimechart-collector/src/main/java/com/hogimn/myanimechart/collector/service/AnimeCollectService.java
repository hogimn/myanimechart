package com.hogimn.myanimechart.collector.service;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.dao.AnimeDao;
import com.hogimn.myanimechart.database.dao.AnimeStatDao;
import com.hogimn.myanimechart.database.dao.BatchHistoryDao;
import com.hogimn.myanimechart.database.domain.Anime;
import com.hogimn.myanimechart.database.domain.BatchHistory;
import com.hogimn.myanimechart.database.repository.AnimeRepository;
import com.hogimn.myanimechart.database.repository.AnimeStatRepository;
import com.hogimn.myanimechart.database.repository.BatchHistoryRepository;
import dev.katsute.mal4j.MyAnimeList;
import dev.katsute.mal4j.PaginatedIterator;
import dev.katsute.mal4j.anime.property.time.Season;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AnimeCollectService {
    private final AnimeRepository animeRepository;
    private final AnimeStatRepository animeStatRepository;
    private final BatchHistoryRepository batchHistoryRepository;
    private final MyAnimeList myAnimeList;

    public AnimeCollectService(
            AnimeRepository animeRepository,
            AnimeStatRepository animeStatRepository, BatchHistoryRepository batchHistoryRepository, MyAnimeList myAnimeList) {
        this.animeRepository = animeRepository;
        this.animeStatRepository = animeStatRepository;
        this.batchHistoryRepository = batchHistoryRepository;
        this.myAnimeList = myAnimeList;
    }

    public List<Anime> getAnime(int year, String season) {
        PaginatedIterator<dev.katsute.mal4j.anime.Anime> animePaginatedIterator =
                myAnimeList.getAnimeSeason(year, getSeason(season)).searchAll();

        List<Anime> animeList = new ArrayList<>();
        while (animePaginatedIterator.hasNext()) {
            try {
                dev.katsute.mal4j.anime.Anime katsuteAnime = animePaginatedIterator.next();
                Anime anime = Anime.from(katsuteAnime);
                animeList.add(anime);
                log.info("{}", anime);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        return animeList;
    }

    private Season getSeason(String season) {
        return switch (season) {
            case "spring" -> Season.Spring;
            case "summer" -> Season.Summer;
            case "fall" -> Season.Fall;
            case "winter" -> Season.Winter;
            default -> null;
        };
    }

    @Transactional
    public void collectAnimeStatistics() {
        List<Anime> animeList = getAnime(DateUtil.currentYear(), DateUtil.currentSeason());
        animeList.forEach(this::saveAnimeStatistics);

        if (DateUtil.changingSeasonMonth()) {
            animeList = getAnime(DateUtil.nextMonthYear(), DateUtil.nextMonthSeason());
            animeList.forEach(this::saveAnimeStatistics);
        }
    }

    public void saveAnimeStatistics(Anime anime) {
        AnimeDao animeDao = upsertAnime(anime);
        saveAnimeStat(animeDao);
    }

    private void saveAnimeStat(AnimeDao anime) {
        if (anime == null) {
            return;
        }

        AnimeStatDao animeStatDao = AnimeStatDao.from(anime);
        animeStatRepository.save(animeStatDao);
    }

    private AnimeDao upsertAnime(Anime anime) {
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

    public void saveBatchHistory(String name) {
        batchHistoryRepository.save(
                BatchHistoryDao.from(new BatchHistory(name, DateUtil.now())));
    }
}
