package com.hogimn.myanimechart.core.common.myanimelist;

import dev.katsute.mal4j.MyAnimeList;
import dev.katsute.mal4j.property.ExperimentalFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MyAnimeListProvider {
    private final String clientId;
    private final AccessTokenProvider accessTokenProvider;
    private MyAnimeList myAnimeList;

    public MyAnimeListProvider(@Value("${myanimelist.clientId}") String clientId,
                               AccessTokenProvider accessTokenProvider) {
        this.clientId = clientId;
        this.accessTokenProvider = accessTokenProvider;
    }

    public MyAnimeList getMyAnimeList() {
        if (myAnimeList != null) {
            return myAnimeList;
        }

        MyAnimeList newMyAnimeList = MyAnimeList.withClientID(clientId);
        newMyAnimeList.enableExperimentalFeature(ExperimentalFeature.ALL);
        myAnimeList = newMyAnimeList;
        return myAnimeList;
    }

    public MyAnimeList getMyAnimeListWithToken() {
        return accessTokenProvider.getAccessTokenFromCookie()
                .map(MyAnimeList::withToken)
                .orElse(getMyAnimeList());
    }
}
