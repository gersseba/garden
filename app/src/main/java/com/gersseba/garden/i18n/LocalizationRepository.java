package com.gersseba.garden.i18n;

import java.util.Locale;

/**
 * API for fetching localized text for a given key and locale.
 */
public interface LocalizationRepository {
    /**
     * Returns a localized string for the provided key and locale.
     * The implementation should follow fallback rules: DB -> resources -> english -> empty.
     */
    String getLocalizedText(String key, Locale locale);
}
