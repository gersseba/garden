package com.gersseba.garden.i18n;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Rule;
import org.junit.Test;

import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LocaleManagerTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    static class FakeSettings implements SettingsDataStore {
        private String saved;
        private final CountDownLatch saveLatch;

        FakeSettings(String saved, CountDownLatch saveLatch) {
            this.saved = saved;
            this.saveLatch = saveLatch;
        }

        @Override
        public String getSavedLocale() {
            return saved;
        }

        @Override
        public void saveLocale(String languageTag) {
            saved = languageTag;
            if (saveLatch != null) {
                saveLatch.countDown();
            }
        }

        public String getSaved() {
            return saved;
        }
    }

    @Test
    public void loadsInitialLocaleFromDataStore() throws InterruptedException {
        CountDownLatch initLatch = new CountDownLatch(1);
        FakeSettings settings = new FakeSettings(Locale.GERMAN.toLanguageTag(), null);
        ExecutorService executor = Executors.newSingleThreadExecutor();

        LocaleManager manager = new LocaleManager(settings, initLatch, executor);

        boolean ok = initLatch.await(1, TimeUnit.SECONDS);
        assertTrue("init did not complete in time", ok);
        assertEquals(Locale.GERMAN, manager.getCurrentLocale());

        executor.shutdown();
    }

    @Test
    public void setLocalePersistsAndUpdatesCurrent() throws InterruptedException {
        CountDownLatch initLatch = new CountDownLatch(1);
        CountDownLatch saveLatch = new CountDownLatch(1);
        FakeSettings settings = new FakeSettings(null, saveLatch);
        ExecutorService executor = Executors.newSingleThreadExecutor();

        LocaleManager manager = new LocaleManager(settings, initLatch, executor);
        initLatch.await(1, TimeUnit.SECONDS);

        manager.setLocale(Locale.FRENCH);
        boolean saved = saveLatch.await(1, TimeUnit.SECONDS);
        assertTrue("save did not complete in time", saved);
        assertEquals(Locale.FRENCH, manager.getCurrentLocale());
        assertEquals(Locale.FRENCH.toLanguageTag(), settings.getSaved());

        executor.shutdown();
    }

    @Test
    public void reportsSupportedLocales() {
        FakeSettings settings = new FakeSettings(null, null);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        LocaleManager manager = new LocaleManager(settings, null, executor);

        assertTrue(manager.isLocaleSupported(Locale.ENGLISH));
        assertTrue(manager.isLocaleSupported(Locale.GERMAN));
        assertFalse(manager.isLocaleSupported(Locale.FRENCH));

        executor.shutdown();
    }
}

