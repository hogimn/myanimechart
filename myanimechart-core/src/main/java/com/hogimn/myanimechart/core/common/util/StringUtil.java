package com.hogimn.myanimechart.core.common.util;

import java.util.Arrays;
import java.util.List;

public class StringUtil {
    public static List<String> safeSplit(String str) {
        if (str == null || str.isBlank()) return List.of();
        return Arrays.stream(str.split(","))
                .map(String::trim)
                .toList();
    }
}
