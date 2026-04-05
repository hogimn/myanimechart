package com.hogimn.myanimechart.core.domain.anime;

public enum AnimeSeason {
    SPRING,
    SUMMER,
    FALL,
    WINTER;

    /**
     * @return lowercase season string stored in the database and used in JPQL ({@code spring}, {@code summer}, …)
     */
    public String apiValue() {
        return name().toLowerCase();
    }

    /**
     * Parses a path or query segment into a season (case-insensitive).
     *
     * @throws IllegalArgumentException if null, blank, or not a known season
     */
    public static AnimeSeason parse(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("Season is required.");
        }
        String trimmed = raw.trim();
        for (AnimeSeason s : values()) {
            if (s.name().equalsIgnoreCase(trimmed)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Season must be one of: spring, summer, fall, winter.");
    }

    @Override
    public String toString() {
        return apiValue();
    }
}