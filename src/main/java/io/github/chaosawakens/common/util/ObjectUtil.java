package io.github.chaosawakens.common.util;

import java.util.Locale;
import java.util.Objects;

public final class ObjectUtil {
	
	private ObjectUtil() {
		throw new IllegalAccessError("Attempted to instantiate a Utility Class!");
	}
	
	/**
	 * Performs nullity checks, to ascertain that all object parameters passed in aren't null
	 * @param throwsException Whether or not a {@link NullPointerException} will be thrown if any of the objects passed in are null
	 * @param objects The objects to perform nullity checks on
	 * @return True if all objects are not null, else returns False (or throws NPM if <code>throwsException</code> is true.
	 */
	public static boolean performNullityChecks(boolean throwsException, Object... objects) {
		for (Object obj : objects) {
			if (throwsException) Objects.requireNonNull(obj, "An object parameter passed in was null. Check stacktrace for more info.");
			else {
				if (obj == null) return false;
			}
		}
		return true;
	}
	
	/**
	 * Capitalizes the very first character in a specified {@link String}
	 * @param targetString String to capitalize the first letter of
	 * @return The specified String with the first character in it capitalized
	 */
	public static String capitalizeFirstLetter(String targetString) {
		return targetString.replaceAll(targetString.substring(0, 1), targetString.substring(0, 1).toUpperCase(Locale.ROOT));
	}

}
