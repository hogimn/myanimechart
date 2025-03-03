package com.hogimn.myanimechart.query.user.service;

import com.hogimn.myanimechart.common.myanimelist.MyAnimeListProvider;
import com.hogimn.myanimechart.database.user.dto.AnimeListStatusDto;
import com.hogimn.myanimechart.database.user.dto.UserDto;
import dev.katsute.mal4j.MyAnimeList;
import dev.katsute.mal4j.PaginatedIterator;
import dev.katsute.mal4j.anime.AnimeListStatus;
import dev.katsute.mal4j.anime.property.StartSeason;
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
        PaginatedIterator<AnimeListStatus> iterator = myAnimeList
                .getUserAnimeListing(userDto.getName())
                .withLimit(1000)
                .searchAll();

        List<AnimeListStatusDto> results = new ArrayList<>();
        while (iterator.hasNext()) {
            AnimeListStatus animeListStatus = iterator.next();
            StartSeason startSeason = animeListStatus.getAnime().getStartSeason();
            if (startSeason.getYear() == null
                    || startSeason.getYear() != year
                    || startSeason.getSeason() == null
                    || !startSeason.getSeason().field().equals(season)) {
                continue;
            }
            results.add(AnimeListStatusDto.from(animeListStatus));
        }

        return results;
    }

    public AnimeListStatusDto getAnimeListStatusDtoById(int id) {
        MyAnimeList myAnimeList = myAnimeListProvider.getMyAnimeListWithToken();
        UserDto userDto = UserDto.from(myAnimeList.getAuthenticatedUser());
        List<AnimeListStatus> animeListStatuses = myAnimeList
                .getUserAnimeListing(userDto.getName())
                .withLimit(1000)
                .search();

        Optional<AnimeListStatusDto> animeListStatusDto = animeListStatuses.parallelStream()
                .filter(animeListStatus -> animeListStatus.getAnime().getID() == id)
                .findFirst()
                .map(AnimeListStatusDto::from);

        if (animeListStatusDto.isPresent()) {
            return animeListStatusDto.get();
        }

        PaginatedIterator<AnimeListStatus> iterator = myAnimeList
                .getUserAnimeListing(userDto.getName())
                .withOffset(animeListStatuses.size())
                .searchAll();

        while (iterator.hasNext()) {
            AnimeListStatus animeListStatus = iterator.next();
            if (animeListStatus.getAnime().getID() == id) {
                return AnimeListStatusDto.from(animeListStatus);
            }
        }

        return null;
    }

    public void updateUserAnimeStatus(AnimeListStatusDto animeListStatusDto) {
        MyAnimeList myAnimeList = myAnimeListProvider.getMyAnimeListWithToken();
        myAnimeList.updateAnimeListing(animeListStatusDto.getAnimeId())
                .status(animeListStatusDto.getStatus())
                .score(animeListStatusDto.getScore())
                .episodesWatched(animeListStatusDto.getWatchedEpisodes())
                .update();
    }
}
