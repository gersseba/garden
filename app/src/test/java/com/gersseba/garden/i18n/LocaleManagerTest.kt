package com.gersseba.garden.i18n
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
@OptIn(ExperimentalCoroutinesApi::class)
class LocaleManagerTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    class FakeSettings(private var saved: String?, private val saveLatch: CountDownLatch?) : SettingsDataStore {
        override fun getSavedLocale(): String? = saved
        override fun saveLocale(languageTag: String) {
            saved = languageTag
            saveLatch?.countDown()
        }
        fun getSaved(): String? = saved
    }
    @Test
    fun loadsInitialLocaleFromDataStore() = runTest {
        val initLatch = CountDownLatch(1)
        val settings = FakeSettings(Locale.GERMAN.toLanguageTag(), null)
        val manager = LocaleManager(settings, initLatch, this)
        val ok = initLatch.await(1, TimeUnit.SECONDS)
        assertTrue("init did not complete in time", ok)
        assertEquals(Locale.GERMAN, manager.getCurrentLocale())
    }
    @Test
    fun setLocalePersistsAndUpdatesCurrent() = runTest {
        val initLatch = CountDownLatch(1)
        val saveLatch = CountDownLatch(1)
        val settings = FakeSettings(null, saveLatch)
        val manager = LocaleManager(settings, initLatch, this)
        initLatch.await(1, TimeUnit.SECONDS)
        manager.setLocale(Locale.FRENCH)
        val saved = saveLatch.await(1, TimeUnit.SECONDS)
        assertTrue("save did not complete in time", saved)
        assertEquals(Locale.FRENCH, manager.getCurrentLocale())
        assertEquals(Locale.FRENCH.toLanguageTag(), settings.getSaved())
    }
    @Test
    fun reportsSupportedLocales() = runTest {
        val settings = FakeSettings(null, null)
        val manager = LocaleManager(settings, null, this)
        assertTrue(manager.isLocaleSupported(Locale.ENGLISH))
        assertTrue(manager.isLocaleSupported(Locale.GERMAN))
        assertFalse(manager.isLocaleSupported(Locale.FRENCH))
    }
}
