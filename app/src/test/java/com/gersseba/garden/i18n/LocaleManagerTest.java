// Java unit tests for LocaleManager
package com.gersseba.garden.i18n;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LocaleManagerTest {

    static class FakeSettings implements SettingsDataStore {
        volatile String saved;
        final CountDownLatch saveLatch;

        FakeSettings(String initial, CountDownLatch saveLatch) {
            this.saved = initial;
            this.saveLatch = saveLatch;
        }

        @Override
        public String getSavedLocale() {
            return saved;
        }

        @Override
        public void saveLocale(String languageTag) {
            this.saved = languageTag;
            if (saveLatch != null) saveLatch.countDown();
        }
    }

    private ExecutorService executor;

    @Before
    public void setUp() {
        executor = Executors.newSingleThreadExecutor();
    }

    @After
    public void tearDown() {
        executor.shutdownNow();
    }

    @Test
    public void loadsInitialLocaleFromDataStore() throws Exception {
        CountDownLatch initLatch = new CountDownLatch(1);
        FakeSettings settings = new FakeSettings(Locale.GERMAN.toLanguageTag(), null);

        LocaleManager manager = new LocaleManager(settings, initLatch, executor);
        boolean ok = initLatch.await(1, TimeUnit.SECONDS);
        if (!ok) throw new AssertionError("init did not complete in time");

        assertEquals(Locale.forLanguageTag(Locale.GERMAN.toLanguageTag()), manager.getCurrentLocale());
    }

    @Test
    public void setLocalePersistsAndUpdatesCurrent() throws Exception {
        CountDownLatch initLatch = new CountDownLatch(1);
        CountDownLatch saveLatch = new CountDownLatch(1);
        FakeSettings settings = new FakeSettings(null, saveLatch);

        LocaleManager manager = new LocaleManager(settings, initLatch, executor);
        // wait initial load
        initLatch.await(1, TimeUnit.SECONDS);

        manager.setLocale(Locale.FRENCH);

        boolean saved = saveLatch.await(1, TimeUnit.SECONDS);
        if (!saved) throw new AssertionError("save did not complete in time");

        assertEquals(Locale.FRENCH, manager.getCurrentLocale());
        assertEquals(Locale.FRENCH.toLanguageTag(), settings.saved);
    }

    @Test
    public void reportsSupportedLocales() {
        // use synchronous executor to avoid background thread; init latch not needed for this check
        FakeSettings settings = new FakeSettings(null, null);
        LocaleManager manager = new LocaleManager(settings, null, executor);

        assertTrue(manager.isLocaleSupported(Locale.ENGLISH));
        assertTrue(manager.isLocaleSupported(Locale.GERMAN));
        assertFalse(manager.isLocaleSupported(Locale.FRENCH));
    }
}
