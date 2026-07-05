package com.gersseba.garden.i18n

import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.CountDownLatch

/**
 * Service that manages the current locale for the app.
 *
 * Public API is intentionally small: observe [currentLocale] and use
 * [setLocale] to change. Initial value is read from the provided
 * [SettingsDataStore]; if absent, falls back to [Locale.getDefault()].
 */
class LocaleManager @JvmOverloads constructor(
    private val settings: SettingsDataStore,
    private val initLatch: CountDownLatch? = null,
    scope: CoroutineScope? = null
) {
    private val currentLocale = MutableLiveData<Locale?>()
    // keep a synchronous copy for tests and callers without Android Looper
    @Volatile
    private var currentLocaleValue: Locale? = null

    // dispatcher/scope for IO operations; allow injection for tests
    private val scope: CoroutineScope = scope ?: CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val supportedLocales: Set<Locale> = setOf(Locale.ENGLISH, Locale.GERMAN)

    init {
        // load initial value asynchronously to avoid blocking callers
        this.scope.launch {
            try {
                val saved = settings.getSavedLocale()
                val locale = if (saved.isNullOrEmpty()) Locale.getDefault() else Locale.forLanguageTag(saved)
                currentLocaleValue = locale
                try {
                    currentLocale.postValue(locale)
                } catch (e: RuntimeException) {
                    // In plain JVM unit tests there may be no Android Looper available; avoid touching LiveData
                }
            } finally {
                initLatch?.countDown()
            }
        }
    }

    /** LiveData exposing the current locale. Observe on main thread. */
    fun currentLocale(): LiveData<Locale?> = currentLocale

    /** Sets a new locale and persists it without blocking the caller. */
    fun setLocale(locale: Locale?) {
        if (locale == null) return
        scope.launch {
            settings.saveLocale(locale.toLanguageTag())
            currentLocaleValue = locale
            try {
                currentLocale.postValue(locale)
            } catch (e: RuntimeException) {
                // skip LiveData update when no Android Looper is present
            }
        }
    }

    /** Returns the current locale synchronously (may be null until loaded). */
    fun getCurrentLocale(): Locale? = currentLocaleValue ?: currentLocale.value

    /** Returns whether the provided locale is supported by the app. */
    fun isLocaleSupported(locale: Locale?): Boolean = locale != null && supportedLocales.contains(locale)
}






