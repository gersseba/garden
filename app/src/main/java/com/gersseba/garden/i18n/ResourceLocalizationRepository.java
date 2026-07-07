package com.gersseba.garden.i18n;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of {@link LocalizationRepository} that tries a data source first
 * and falls back to resources and english default.
 */
public class ResourceLocalizationRepository implements LocalizationRepository {
    private final LocalizedTextDataSource dataSource;
    private final ResourceProvider resources;
    private final Map<String, String> cache = new ConcurrentHashMap<>();

    public ResourceLocalizationRepository(LocalizedTextDataSource dataSource, ResourceProvider resources) {
        if (resources == null) {
            throw new IllegalArgumentException("ResourceProvider cannot be null");
        }
        this.dataSource = dataSource;
        this.resources = resources;
    }

    @Override
    public String getLocalizedText(String key, Locale locale) {
        if (key == null) return "";
        String cacheKey = key + "|" + (locale == null ? "" : locale.toLanguageTag());
        String cached = cache.get(cacheKey);
        if (cached != null) return cached;

        // 1) DB / data source
        String db = null;
        if (dataSource != null) db = dataSource.getTextForKey(key, locale);
        if (db != null) {
            cache.put(cacheKey, db);
            return db;
        }

        // 2) Resource mapping
        int resId = ResourceKeyMapper.mapKeyToResId(key);
        String fromRes = resId != 0 ? resources.getString(resId, locale) : null;
        if (fromRes != null) {
            cache.put(cacheKey, fromRes);
            return fromRes;
        }

        // 3) fallback to English resource
        String english = resId != 0 ? resources.getString(resId, Locale.ENGLISH) : null;
        if (english != null) {
            cache.put(cacheKey, english);
            return english;
        }

        // 4) last resort
        cache.put(cacheKey, "");
        return "";
    }
}
