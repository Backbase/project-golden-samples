package com.backbase.openbanking.mockserver.common;

import java.util.Arrays;

public class StringUtil {

    /**
     * Returns true if any of the given strings is empty.
     * @param values Strings to check
     * @return boolean
     */
    public static boolean isAnyEmpty(String ... values) {
        return Arrays.stream(values).anyMatch(s -> s == null || s.isEmpty());
    }
}
