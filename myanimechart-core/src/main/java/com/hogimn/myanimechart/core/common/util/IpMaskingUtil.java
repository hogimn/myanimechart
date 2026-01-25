package com.hogimn.myanimechart.core.common.util;

public class IpMaskingUtil {
    private static final String IPV4_MASK = "***";
    private static final String IPV6_MASK = "****:****:****:****";

    public static String mask(String ip) {
        if (ip == null || ip.isBlank()) {
            return "Unknown";
        }

        if (ip.contains(".")) {
            int lastDotIndex = ip.lastIndexOf(".");
            return (lastDotIndex != -1)
                    ? ip.substring(0, lastDotIndex + 1) + IPV4_MASK
                    : ip;
        }

        if (ip.contains(":")) {
            String[] parts = ip.split(":");
            if (parts.length >= 4) {
                return String.format("%s:%s:%s:%s:%s",
                        parts[0], parts[1], parts[2], parts[3], IPV6_MASK);
            }
        }

        return ip;
    }
}
