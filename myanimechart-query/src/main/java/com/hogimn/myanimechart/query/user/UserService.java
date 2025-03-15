package com.hogimn.myanimechart.query.user;

import com.hogimn.myanimechart.common.myanimelist.MyAnimeListProvider;
import com.hogimn.myanimechart.common.user.AnimeListStatusDto;
import com.hogimn.myanimechart.common.user.UserDto;
import com.hogimn.myanimechart.common.util.SleepUtil;
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

    public UserDto findUserDtoByToken() {
        return UserDto.from(myAnimeListProvider.getMyAnimeListWithToken().getAuthenticatedUser());
    }

    public List<AnimeListStatusDto> findUserAnimeListStatusDtosByYearAndSeason(int year, String season) throws InterruptedException {
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

            if (tempAnimeListStatuses.size() >= limit) {
                offset += limit;
            } else {
                break;
            }

            SleepUtil.sleep(30 * 1000);
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

    public AnimeListStatusDto findAnimeListStatusDtoById(int id) throws InterruptedException {
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

            if (tempAnimeListStatuses.size() >= limit) {
                offset += limit;
            } else {
                break;
            }
            
            SleepUtil.sleep(30 * 1000);
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

    public void deleteUserAnimeStatus(AnimeListStatusDto animeListStatusDto) {
        MyAnimeList myAnimeList = myAnimeListProvider.getMyAnimeListWithToken();
        myAnimeList.deleteAnimeListing(animeListStatusDto.getAnimeId());
    }
}
