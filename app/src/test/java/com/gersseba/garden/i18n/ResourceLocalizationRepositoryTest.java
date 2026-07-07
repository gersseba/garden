package com.gersseba.garden.i18n;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ResourceLocalizationRepositoryTest {

    static class FakeDataSource implements LocalizedTextDataSource {
        Map<String, String> map = new HashMap<>();

        @Override
        public String getTextForKey(String key, Locale locale) {
            return map.get(key + "|" + (locale == null ? "" : locale.toLanguageTag()));
        }
    }

    static class FakeResourceProvider implements ResourceProvider {
        Map<String, String> map = new HashMap<>();

        @Override
        public String getString(int resId, Locale locale) {
            return map.get(resId + "|" + (locale == null ? "" : locale.toLanguageTag()));
        }

        void put(int resId, Locale locale, String value) {
            map.put(resId + "|" + (locale == null ? "" : locale.toLanguageTag()), value);
        }
    }

    private FakeDataSource dataSource;
    private FakeResourceProvider resourceProvider;
    private ResourceLocalizationRepository repository;

    @Before
    public void setup() {
        dataSource = new FakeDataSource();
        resourceProvider = new FakeResourceProvider();
        // populate fake resources for mapping keys defined in ResourceKeyMapper
        resourceProvider.put(com.gersseba.garden.R.string.task_name_water, Locale.ENGLISH, "Water (res)");
        resourceProvider.put(com.gersseba.garden.R.string.task_name_fertilize, Locale.ENGLISH, "Fertilize (res)");
        resourceProvider.put(com.gersseba.garden.R.string.plant_name_monstera, Locale.ENGLISH, "Monstera (res)");

        repository = new ResourceLocalizationRepository(dataSource, resourceProvider);
    }

    @Test
    public void returnsDbTextWhenPresent() {
        dataSource.map.put(ResourceKeyMapper.KEY_TASK_WATER + "|" + Locale.GERMAN.toLanguageTag(), "Wasser (db)");
        String out = repository.getLocalizedText(ResourceKeyMapper.KEY_TASK_WATER, Locale.GERMAN);
        assertEquals("Wasser (db)", out);
    }

    @Test
    public void fallsBackToResourceWhenDbMissing() {
        // use Locale.ENGLISH to match what we put in setup
        String out = repository.getLocalizedText(ResourceKeyMapper.KEY_TASK_FERTILIZE, Locale.ENGLISH);
        assertEquals("Fertilize (res)", out);
    }

    @Test
    public void fallsBackToEnglishWhenResourceMissingForLocale() {
        // provide only english fallback via same resId
        resourceProvider.put(com.gersseba.garden.R.string.plant_name_monstera, Locale.ENGLISH, "Monstera (en)");
        // ask for a different locale where we DON'T have a value
        String out = repository.getLocalizedText(ResourceKeyMapper.KEY_PLANT_MONSTERA, Locale.FRENCH);
        assertEquals("Monstera (en)", out);
    }

    @Test
    public void returnsEmptyStringWhenNothingFound() {
        String out = repository.getLocalizedText("unknown.key", Locale.ENGLISH);
        assertEquals("", out);
    }
}
