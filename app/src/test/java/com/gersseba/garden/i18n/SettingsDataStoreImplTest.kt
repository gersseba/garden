package com.gersseba.garden.i18n

import org.junit.Test
import org.junit.Assert.*
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.Locale

class SettingsDataStoreImplTest {

    @Test
    fun saveAndReadLocale() {
        val tmp = File.createTempFile("settings_test", ".preferences_pb")
        tmp.deleteOnExit()

        val store = SettingsDataStoreImpl.create(tmp)
        // initially empty
        runBlocking {
            assertNull(store.getSavedLocale())
            store.saveLocale("de")
            assertEquals("de", store.getSavedLocale())
        }
    }

    @Test
    fun localeManager_readsFromDataStore() {
        val tmp = File.createTempFile("settings_test2", ".preferences_pb")
        tmp.deleteOnExit()
        val store = SettingsDataStoreImpl.create(tmp)
        runBlocking { store.saveLocale(Locale.GERMAN.toLanguageTag()) }

        val initLatch = java.util.concurrent.CountDownLatch(1)
        val manager = LocaleManager(store, initLatch)
        // wait for async init deterministically
        assertTrue(initLatch.await(1, java.util.concurrent.TimeUnit.SECONDS))
        val cur = manager.getCurrentLocale()
        assertEquals(Locale.GERMAN.language, cur!!.getLanguage())
    }
}





