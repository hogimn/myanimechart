package com.hogimn.myanimechart.service.user;

import com.hogimn.myanimechart.core.common.myanimelist.MyAnimeListProvider;
import com.hogimn.myanimechart.core.common.util.SleepUtil;
import dev.katsute.mal4j.MyAnimeList;
import dev.katsute.mal4j.anime.AnimeListStatus;
import dev.katsute.mal4j.query.AnimeListUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final MyAnimeListProvider myAnimeListProvider;

    public UserService(MyAnimeListProvider myAnimeListProvider) {
        this.myAnimeListProvider = myAnimeListProvider;
    }

    public UserDto getCurrentUser() {
        return UserDto.from(myAnimeListProvider.getMyAnimeListWithToken().getAuthenticatedUser());
    }

    public List<AnimeListStatusDto> getAnimeStatuses() {
        MyAnimeList myAnimeList = myAnimeListProvider.getMyAnimeListWithToken();
        UserDto userDto = UserDto.from(myAnimeList.getAuthenticatedUser());

        int offset = 0;
        int limit = 1000;

        List<AnimeListStatus> animeListStatuses = new ArrayList<>();
        while (true) {
            List<AnimeListStatus> tempAnimeListStatuses = myAnimeList
                    .getUserAnimeListing(userDto.getName())
                    .withLimit(limit)
                    .withOffset(offset)
                    .search();

            animeListStatuses.addAll(tempAnimeListStatuses);

            log.info("offset: {}, limit: {}, size of list: {}", offset, limit, animeListStatuses.size());

            if (tempAnimeListStatuses.size() >= limit) {
                offset += limit;
            } else {
                break;
            }

            SleepUtil.sleepForMAL();
        }

        return animeListStatuses
                .stream()
                .map(AnimeListStatusDto::from)
                .toList();
    }

    public AnimeListStatusDto getAnimeStatusById(int id) {
        MyAnimeList myAnimeList = myAnimeListProvider.getMyAnimeListWithToken();
        UserDto userDto = UserDto.from(myAnimeList.getAuthenticatedUser());

        int offset = 0;
        int limit = 1000;

        List<AnimeListStatus> animeListStatuses = new ArrayList<>();
        while (true) {
            List<AnimeListStatus> tempAnimeListStatuses = myAnimeList
                    .getUserAnimeListing(userDto.getName())
                    .withLimit(limit)
                    .withOffset(offset)
                    .search();

            animeListStatuses.addAll(tempAnimeListStatuses);

            log.info("offset: {}, limit: {}, size of list: {}", offset, limit, animeListStatuses.size());

            if (tempAnimeListStatuses.size() >= limit) {
                offset += limit;
            } else {
                break;
            }

            SleepUtil.sleepForMAL();
        }

        Optional<AnimeListStatusDto> animeListStatusDto = animeListStatuses
                .parallelStream()
                .filter(animeListStatus -> animeListStatus.getAnime().getID() == id)
                .findFirst()
                .map(AnimeListStatusDto::from);

        return animeListStatusDto.orElse(null);
    }

    public void updateAnimeStatus(AnimeListStatusDto animeListStatusDto) {
        MyAnimeList myAnimeList = myAnimeListProvider.getMyAnimeListWithToken();
        AnimeListUpdate animeListUpdate = myAnimeList.updateAnimeListing(animeListStatusDto.getAnimeId());
        if (animeListStatusDto.getStatus() != null) {
            animeListUpdate.status(animeListStatusDto.getStatus());
        }
        if (animeListStatusDto.getScore() != null) {
            animeListUpdate.score(animeListStatusDto.getScore());
        }
        animeListUpdate.episodesWatched(animeListStatusDto.getWatchedEpisodes());

        animeListUpdate.update();
    }

    public void deleteAnimeStatus(AnimeListStatusDto animeListStatusDto) {
        MyAnimeList myAnimeList = myAnimeListProvider.getMyAnimeListWithToken();
        myAnimeList.deleteAnimeListing(animeListStatusDto.getAnimeId());
    }
}
