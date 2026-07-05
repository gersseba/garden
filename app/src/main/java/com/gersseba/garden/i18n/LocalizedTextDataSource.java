package com.gersseba.garden.i18n;

import java.util.Locale;

/**
 * Pluggable interface representing a data source (e.g. DB) holding long localized texts.
 * Implementations may return null when no entry exists for a key/locale.
 */
public interface LocalizedTextDataSource {
    String getTextForKey(String key, Locale locale);
}

