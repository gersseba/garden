package com.gersseba.garden.i18n

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.CountDownLatch

/**
 * Locale management service.
 * Reads initial value from SettingsDataStore and exposes it via LiveData.
 */
class LocaleManager @JvmOverloads constructor(
    private val settings: SettingsDataStore,
    private val initLatch: CountDownLatch? = null,
    private val scope: CoroutineScope = applicationScope
) {
    companion object {
        @Volatile
        private var INSTANCE: LocaleManager? = null

        private val applicationScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

        @JvmStatic
        fun getInstance(settings: SettingsDataStore): LocaleManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: LocaleManager(settings, null, applicationScope).also { INSTANCE = it }
            }
        }
    }

    private val _currentLocale = MutableLiveData<Locale>()

    @Volatile
    private var currentLocaleValue: Locale? = null

    private val supportedLocales = setOf(Locale.ENGLISH, Locale.GERMAN)

    init {
        scope.launch(Dispatchers.IO) {
            try {
                val saved = settings.savedLocale
                val locale = if (saved.isNullOrEmpty()) {
                    Locale.getDefault()
                } else {
                    Locale.forLanguageTag(saved)
                }
                currentLocaleValue = locale
                _currentLocale.postValue(locale)
            } finally {
                initLatch?.countDown()
            }
        }
    }

    /** LiveData exposing the current locale. Observe on main thread. */
    fun currentLocale(): LiveData<Locale> = _currentLocale

    /** Sets a new locale and persists it without blocking the caller. */
    fun setLocale(locale: Locale?) {
        if (locale == null) return
        scope.launch(Dispatchers.IO) {
            settings.saveLocale(locale.toLanguageTag())
            currentLocaleValue = locale
            _currentLocale.postValue(locale)
        }
    }

    /** Returns the current locale synchronously (may be null until loaded). */
    fun getCurrentLocale(): Locale? = currentLocaleValue ?: _currentLocale.value

    /** Returns whether the provided locale is supported by the app. */
    fun isLocaleSupported(locale: Locale?): Boolean = locale != null && supportedLocales.contains(locale)
}
