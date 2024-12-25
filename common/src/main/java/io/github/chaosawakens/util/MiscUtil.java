package io.github.chaosawakens.util;

import java.util.Locale;

public final class MiscUtil {

    public static String capitalizeFirstLetter(String targetString) {
        return targetString.replaceAll(targetString.substring(0, 1), targetString.substring(0, 1).toUpperCase(Locale.ROOT));
    }
}
