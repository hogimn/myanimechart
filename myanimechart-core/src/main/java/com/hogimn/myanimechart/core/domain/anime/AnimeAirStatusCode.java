package com.hogimn.myanimechart.core.domain.anime;

/**
 * MAL API / stored {@code air_status} values used in queries and persistence.
 */
public enum AnimeAirStatusCode {
    CURRENTLY_AIRING("currently_airing"),
    FINISHED_AIRING("finished_airing");

    private final String code;

    AnimeAirStatusCode(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
