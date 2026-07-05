package com.gersseba.garden.i18n;

import java.util.Locale;

/**
 * Abstraction for resource string lookup. Implementations may delegate to Android Context or be fakes for tests.
 */
public interface ResourceProvider {
    /**
     * Returns the string for the given resource ID for the requested locale, or null if not found.
     */
    String getString(int resId, Locale locale);

    /**
     * Returns the string for the given resource ID using default locale, or null.
     */
    default String getString(int resId) {
        return getString(resId, null);
    }
}

