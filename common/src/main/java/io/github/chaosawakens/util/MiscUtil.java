package io.github.chaosawakens.util;

import java.util.Locale;

public final class MiscUtil {

    private MiscUtil() {
        throw new IllegalAccessError("Attempted to construct Utility Class!");
    }

    public static String capitalizeFirstLetter(String targetString) {
        return targetString.replaceFirst(targetString.substring(0, 1), targetString.substring(0, 1).toUpperCase(Locale.ROOT));
    }

    public static String reformatFromSnakeCase(String targetString) {
        String[] parts = targetString.split("_");
        StringBuilder result = new StringBuilder();

        for (int index = 0; index < parts.length; index++) {
            String part = parts[index];

            if (index != parts.length - 1) part += " ";

            result.append(capitalizeFirstLetter(part));
        }

        return result.toString();
    }

    public static String wrapSuffixInBrackets(String targetString) {
        int lastSpaceIndex = targetString.lastIndexOf(" ");
        return lastSpaceIndex == -1 ? targetString : targetString.substring(0, lastSpaceIndex) + " (" + targetString.substring(lastSpaceIndex + 1) + ")";
    }
}
