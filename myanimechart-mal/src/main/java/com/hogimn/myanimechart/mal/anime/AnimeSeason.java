package com.hogimn.myanimechart.mal.anime;

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