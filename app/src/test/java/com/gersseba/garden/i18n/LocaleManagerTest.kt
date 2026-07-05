package com.gersseba.garden.i18n

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import java.util.Locale
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class LocaleManagerTest {

    class InMemorySettings(var saved: String? = null, private val saveLatch: CountDownLatch? = null) : SettingsDataStore {
        override suspend fun getSavedLocale(): String? = saved
        override suspend fun saveLocale(languageTag: String) {
            saved = languageTag
            saveLatch?.countDown()
        }
    }

    @Test
    fun initialLocale_readsFromSettings() {
        val settings = InMemorySettings(saved = Locale.GERMAN.toLanguageTag())
        val latch = CountDownLatch(1)
        val manager = LocaleManager(settings, latch)

        // wait for async init to complete deterministically
        assertTrue(latch.await(1, TimeUnit.SECONDS))
        val cur = manager.getCurrentLocale()
        assertNotNull(cur)
        assertEquals(Locale.GERMAN.language, cur!!.language)
    }

    @Test
    fun setLocale_persistsAndUpdatesLiveData() {
        val saveLatch = CountDownLatch(1)
        val settings = InMemorySettings(saveLatch = saveLatch)
        val initLatch = CountDownLatch(1)
        val manager = LocaleManager(settings, initLatch)
        assertTrue(initLatch.await(1, TimeUnit.SECONDS))

        manager.setLocale(Locale.GERMAN)
        // wait for save to complete
        assertTrue(saveLatch.await(1, TimeUnit.SECONDS))
        assertEquals(Locale.GERMAN.language, manager.getCurrentLocale()!!.language)
        assertEquals(Locale.GERMAN.toLanguageTag(), settings.saved)
    }

    @Test
    fun isLocaleSupported_checksList() {
        val settings = InMemorySettings()
        val manager = LocaleManager(settings)
        assertTrue(manager.isLocaleSupported(Locale.ENGLISH))
        assertTrue(manager.isLocaleSupported(Locale.GERMAN))
        assertFalse(manager.isLocaleSupported(Locale.FRENCH))
    }
}


