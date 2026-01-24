package com.hogimn.myanimechart.service.user;

import com.hogimn.myanimechart.core.common.myanimelist.MyAnimeListProvider;
import com.hogimn.myanimechart.core.common.util.SleepUtil;
import com.hogimn.myanimechart.service.exception.AnimeStatusNotFoundException;
import dev.katsute.mal4j.MyAnimeList;
import dev.katsute.mal4j.anime.AnimeListStatus;
import dev.katsute.mal4j.query.AnimeListUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final MyAnimeListProvider myAnimeListProvider;

    private static final int MAL_LIST_LIMIT = 1000;

    public UserResponse getCurrentUser() {
        return UserResponse.from(myAnimeListProvider
                .getMyAnimeListWithToken()
                .getAuthenticatedUser());
    }

    private List<AnimeListStatus> fetchAllAnimeStatuses(MyAnimeList myAnimeList, String userName) {
        int offset = 0;
        List<AnimeListStatus> allStatuses = new ArrayList<>();

        while (true) {
            List<AnimeListStatus> tempStatuses = myAnimeList
                    .getUserAnimeListing(userName)
                    .withLimit(MAL_LIST_LIMIT)
                    .withOffset(offset)
                    .search();

            allStatuses.addAll(tempStatuses);

            log.info("Fetching MAL list: offset={}, totalSize={}", offset, allStatuses.size());

            if (tempStatuses.size() < MAL_LIST_LIMIT) {
                break;
            }

            offset += MAL_LIST_LIMIT;
            SleepUtil.sleepForMAL();
        }
        return allStatuses;
    }

    public List<AnimeListStatusResponse> getAnimeStatuses() {
        MyAnimeList myAnimeList = myAnimeListProvider.getMyAnimeListWithToken();
        UserResponse userResponse = UserResponse.from(myAnimeList.getAuthenticatedUser());
        List<AnimeListStatus> animeListStatuses =
                fetchAllAnimeStatuses(myAnimeList, userResponse.name());

        return animeListStatuses
                .stream()
                .map(AnimeListStatusResponse::from)
                .toList();
    }

    public AnimeListStatusResponse getAnimeStatusById(int id) {
        MyAnimeList myAnimeList = myAnimeListProvider.getMyAnimeListWithToken();
        UserResponse userResponse = UserResponse.from(myAnimeList.getAuthenticatedUser());
        List<AnimeListStatus> allStatuses
                = fetchAllAnimeStatuses(myAnimeList, userResponse.name());

        return allStatuses.stream()
                .filter(status -> status.getAnime().getID() == id)
                .findFirst()
                .map(AnimeListStatusResponse::from)
                .orElseThrow(() -> new AnimeStatusNotFoundException(id));
    }

    public void updateAnimeStatus(AnimeListStatusRequest request) {
        MyAnimeList myAnimeList = myAnimeListProvider.getMyAnimeListWithToken();
        AnimeListUpdate animeListUpdate = myAnimeList.updateAnimeListing(request.animeId());
        if (request.status() != null) {
            animeListUpdate.status(request.status());
        }
        if (request.score() != null) {
            animeListUpdate.score(request.score());
        }
        animeListUpdate.episodesWatched(request.watchedEpisodes());
        animeListUpdate.update();
    }

    public void deleteAnimeStatus(AnimeListStatusRequest request) {
        MyAnimeList myAnimeList = myAnimeListProvider.getMyAnimeListWithToken();
        myAnimeList.deleteAnimeListing(request.animeId());
    }
}
