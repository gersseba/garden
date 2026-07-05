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
        Map<Integer, String> map = new HashMap<>();

        @Override
        public String getString(int resId, Locale locale) {
            // ignore locale in this fake; allow locale-specific map keys if desired
            return map.get(resId);
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
        resourceProvider.map.put(com.gersseba.garden.R.string.task_name_water, "Water (res)");
        resourceProvider.map.put(com.gersseba.garden.R.string.task_name_fertilize, "Fertilize (res)");
        resourceProvider.map.put(com.gersseba.garden.R.string.plant_name_monstera, "Monstera (res)");

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
        String out = repository.getLocalizedText(ResourceKeyMapper.KEY_TASK_FERTILIZE, Locale.GERMAN);
        assertEquals("Fertilize (res)", out);
    }

    @Test
    public void fallsBackToEnglishWhenResourceMissingForLocale() {
        // remove resource mapping for the key to simulate missing locale-specific string
        resourceProvider.map.remove(com.gersseba.garden.R.string.plant_name_monstera);
        // but provide english fallback via same resId
        resourceProvider.map.put(com.gersseba.garden.R.string.plant_name_monstera, "Monstera (en)");

        String out = repository.getLocalizedText(ResourceKeyMapper.KEY_PLANT_MONSTERA, Locale.FRENCH);
        assertEquals("Monstera (en)", out);
    }

    @Test
    public void returnsEmptyStringWhenNothingFound() {
        String out = repository.getLocalizedText("unknown.key", Locale.ENGLISH);
        assertEquals("", out);
    }
}

