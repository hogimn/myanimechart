package com.hogimn.myanimechart.query.user.service;

import com.hogimn.myanimechart.common.myanimelist.MyAnimeListProvider;
import com.hogimn.myanimechart.database.user.dto.AnimeListStatusDto;
import com.hogimn.myanimechart.database.user.dto.UserDto;
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

    public UserDto getUserDto() {
        return UserDto.from(myAnimeListProvider.getMyAnimeListWithToken().getAuthenticatedUser());
    }

    public List<AnimeListStatusDto> getUserAnimeListStatusDtosByYearAndSeason(int year, String season) {
        MyAnimeList myAnimeList = myAnimeListProvider.getMyAnimeListWithToken();
        UserDto userDto = UserDto.from(myAnimeList.getAuthenticatedUser());

        int offset = 0;
        int limit = 1000;

        List<AnimeListStatus> animeListStatuses = new ArrayList<>();
        while (true) {
            animeListStatuses.addAll(myAnimeList
                    .getUserAnimeListing(userDto.getName())
                    .withLimit(limit)
                    .withOffset(offset)
                    .search());

            if (animeListStatuses.size() >= limit) {
                offset += limit;
            } else {
                break;
            }
        }

        return animeListStatuses
                .parallelStream()
                .filter(animeListStatus ->
                        animeListStatus.getAnime().getStartSeason() != null
                                && animeListStatus.getAnime().getStartSeason().getYear() != null
                                && animeListStatus.getAnime().getStartSeason().getYear() == year
                                && animeListStatus.getAnime().getStartSeason().getSeason() != null
                                && animeListStatus.getAnime().getStartSeason().getSeason().field().equals(season))
                .map(AnimeListStatusDto::from)
                .toList();
    }

    public AnimeListStatusDto getAnimeListStatusDtoById(int id) {
        MyAnimeList myAnimeList = myAnimeListProvider.getMyAnimeListWithToken();
        UserDto userDto = UserDto.from(myAnimeList.getAuthenticatedUser());

        int offset = 0;
        int limit = 1000;

        List<AnimeListStatus> animeListStatuses = new ArrayList<>();
        while (true) {
            animeListStatuses.addAll(myAnimeList
                    .getUserAnimeListing(userDto.getName())
                    .withLimit(limit)
                    .withOffset(offset)
                    .search());

            if (animeListStatuses.size() >= limit) {
                offset += limit;
            } else {
                break;
            }
        }

        Optional<AnimeListStatusDto> animeListStatusDto = animeListStatuses
                .parallelStream()
                .filter(animeListStatus -> animeListStatus.getAnime().getID() == id)
                .findFirst()
                .map(AnimeListStatusDto::from);

        return animeListStatusDto.orElse(null);
    }

    public void updateUserAnimeStatus(AnimeListStatusDto animeListStatusDto) {
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
}
