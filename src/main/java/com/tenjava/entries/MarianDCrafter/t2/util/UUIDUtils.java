package com.tenjava.entries.MarianDCrafter.t2.util;

import java.util.UUID;

/**
 * Utils for Java's class UUID.
 */
public final class UUIDUtils {

    private UUIDUtils() {
    }

    /**
     * Returns a simple String of the given uuid.
     * Removes all '-' characters of the uuid.
     * @param uuid the uuid.
     * @return the simple String of the uuid.
     */
    public static String simpleString(UUID uuid) {
        return uuid.toString().replace("-", "");
    }

}
