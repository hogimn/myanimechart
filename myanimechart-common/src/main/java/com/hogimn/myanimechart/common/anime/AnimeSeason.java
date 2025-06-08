package com.hogimn.myanimechart.common.anime;

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