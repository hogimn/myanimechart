package com.hogimn.myanimechart.common.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class DateUtil {
    public static LocalDateTime now() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    public static int currentYear() {
        return now().getYear();
    }

    public static int nextYear() {
        return currentYear() + 1;
    }

    public static int nextMonthYear() {
        if (now().getMonthValue() == 12) {
            return nextYear();
        }

        return now().getYear();
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
}
