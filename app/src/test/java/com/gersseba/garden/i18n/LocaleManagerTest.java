package com.gersseba.garden.i18n;

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.*;

public class LocaleManagerTest {

    static class InMemorySettings implements SettingsDataStore {
        String saved;

        @Override
        public String getSavedLocale() { return saved; }

        @Override
        public void saveLocale(String languageTag) { this.saved = languageTag; }
    }

    @Test
    public void initialLocale_readsFromSettings() throws Exception {
        InMemorySettings settings = new InMemorySettings();
        settings.saved = Locale.GERMAN.toLanguageTag();
        LocaleManager manager = new LocaleManager(settings);

        // wait briefly for background init
        Thread.sleep(100);
        Locale cur = manager.getCurrentLocale();
        assertNotNull(cur);
        assertEquals(Locale.GERMAN.getLanguage(), cur.getLanguage());
    }

    @Test
    public void setLocale_persistsAndUpdatesLiveData() throws Exception {
        InMemorySettings settings = new InMemorySettings();
        LocaleManager manager = new LocaleManager(settings);
        Thread.sleep(50);

        manager.setLocale(Locale.GERMAN);
        Thread.sleep(20);
        assertEquals(Locale.GERMAN.getLanguage(), manager.getCurrentLocale().getLanguage());
        assertEquals(Locale.GERMAN.toLanguageTag(), settings.saved);
    }

    @Test
    public void isLocaleSupported_checksList() {
        InMemorySettings settings = new InMemorySettings();
        LocaleManager manager = new LocaleManager(settings);
        assertTrue(manager.isLocaleSupported(Locale.ENGLISH));
        assertTrue(manager.isLocaleSupported(Locale.GERMAN));
        assertFalse(manager.isLocaleSupported(Locale.FRENCH));
    }
}

