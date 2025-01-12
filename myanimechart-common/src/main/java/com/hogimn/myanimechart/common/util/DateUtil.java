package com.hogimn.myanimechart.common.util;

import java.time.LocalDateTime;
import java.util.Calendar;

public class DateUtil {
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    private static final Season[] seasons = {
            new Season("winter", new int[]{1, 2, 3}),
            new Season("spring", new int[]{4, 5, 6}),
            new Season("summer", new int[]{7, 8, 9}),
            new Season("fall", new int[]{10, 11, 12})
    };

    public static String getCurrentSeason() {
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        for (Season season : seasons) {
            for (int month : season.months()) {
                if (month == currentMonth) {
                    return season.season();
                }
            }
        }
        return null;
    }

    public static String getPreviousSeason() {
        String currentSeason = getCurrentSeason();
        int currentIndex = getSeasonIndex(currentSeason);
        int previousIndex = (currentIndex == 0) ? seasons.length - 1 : currentIndex - 1;
        return seasons[previousIndex].season();
    }

    public static String getNextSeason() {
        String currentSeason = getCurrentSeason();
        int currentIndex = getSeasonIndex(currentSeason);
        int nextIndex = (currentIndex == seasons.length - 1) ? 0 : currentIndex + 1;
        return seasons[nextIndex].season();
    }

    public static int getCurrentSeasonYear() {
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String currentSeason = getCurrentSeason();
        int firstMonthOfSeason = getSeasonFirstMonth(currentSeason);
        return firstMonthOfSeason <= currentMonth ? currentYear : currentYear - 1;
    }

    public static int getPreviousSeasonYear() {
        String previousSeason = getPreviousSeason();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int firstMonthOfSeason = getSeasonFirstMonth(previousSeason);
        return firstMonthOfSeason < 10 ? currentYear : currentYear - 1;
    }

    public static int getNextSeasonYear() {
        String nextSeason = getNextSeason();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int firstMonthOfSeason = getSeasonFirstMonth(nextSeason);
        return firstMonthOfSeason <= 3 ? currentYear + 1 : currentYear;
    }

    private static int getSeasonIndex(String seasonName) {
        for (int i = 0; i < seasons.length; i++) {
            if (seasons[i].season().equals(seasonName)) {
                return i;
            }
        }
        return -1;
    }

    private static int getSeasonFirstMonth(String seasonName) {
        for (Season season : seasons) {
            if (season.season().equals(seasonName)) {
                return season.months()[0];
            }
        }
        return -1;
    }

    public static String currentSeason() {
        return getSeasonByMonth(now().getMonthValue());
    }

    public static String nextMonthSeason() {
        int monthValue = now().getMonthValue();
        if (monthValue == 12) {
            monthValue = 1;
        } else {
            monthValue++;
        }
        return getSeasonByMonth(monthValue);
    }

    private static String getSeasonByMonth(int month) {
        if (month <= 3) {
            return "winter";
        } else if (month <= 6) {
            return "spring";
        } else if (month <= 9) {
            return "summer";
        } else {
            return "fall";
        }
    }

    public static boolean changingSeasonMonth() {
        return !currentSeason().equals(nextMonthSeason());
    }

    private record Season(String season, int[] months) {
    }
}
