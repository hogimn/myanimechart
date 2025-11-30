package com.hogimn.myanimechart.core.domain.anime;

public enum AnimeSeason {
    SPRING,
    SUMMER,
    FALL,
    WINTER;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}