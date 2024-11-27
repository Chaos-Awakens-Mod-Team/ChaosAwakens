package io.github.chaosawakens.api.asm;

import io.github.chaosawakens.CAConstants;

public final class ClassFinder {

    /**
     * A utility method that wraps {@link Class#forName(String)} in a {@code try-catch} block.
     *
     * @param targetClassName The name of the class to load.
     *
     * @return The loaded class, or {@code null} if no such class exists.
     */
    public static Class<?> forName(String targetClassName) {
        try {
            return Class.forName(targetClassName);
        } catch (ClassNotFoundException | ExceptionInInitializerError e) {
            CAConstants.LOGGER.error("Failed to load: {}, no such class was found.", targetClassName, e);
            return null;
        }
    }
}
