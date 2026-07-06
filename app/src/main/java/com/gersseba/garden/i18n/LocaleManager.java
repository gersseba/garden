package com.gersseba.garden.i18n;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Java implementation of a small locale management service.
 * Reads initial value from SettingsDataStore on a background thread and exposes it via LiveData.
 */
public class LocaleManager {
	private final MutableLiveData<Locale> currentLocale = new MutableLiveData<>();
	private volatile Locale currentLocaleValue;
	private final ExecutorService executor;
	private final SettingsDataStore settings;
	private final CountDownLatch initLatch;
	private final Set<Locale> supportedLocales = new HashSet<>(Arrays.asList(Locale.ENGLISH, Locale.GERMAN));

	// Shared application-scoped executor to avoid creating per-instance threads that may leak
	private static final ExecutorService SHARED_EXECUTOR = Executors.newSingleThreadExecutor(r -> {
		Thread t = new Thread(r);
		t.setName("LocaleManager-SHARED");
		t.setDaemon(true);
		return t;
	});

	public LocaleManager(SettingsDataStore settings) {
		this(settings, null, SHARED_EXECUTOR);
	}

	public LocaleManager(SettingsDataStore settings, CountDownLatch initLatch, ExecutorService executor) {
		this.settings = settings;
		this.initLatch = initLatch;
		this.executor = executor != null ? executor : Executors.newSingleThreadExecutor();

		// load initial value asynchronously
		this.executor.submit(new Runnable() {
			@Override
			public void run() {
				try {
					String saved = settings.getSavedLocale();
					Locale locale = (saved == null || saved.isEmpty()) ? Locale.getDefault() : Locale.forLanguageTag(saved);
					currentLocaleValue = locale;
					try {
						currentLocale.postValue(locale);
					} catch (RuntimeException e) {
						// ignore (no looper available in unit tests)
					}
				} finally {
					if (initLatch != null) initLatch.countDown();
				}
			}
		});
	}

	/** LiveData exposing the current locale. Observe on main thread. */
	public LiveData<Locale> currentLocale() {
		return currentLocale;
	}

	/** Sets a new locale and persists it without blocking the caller. */
	public void setLocale(final Locale locale) {
		if (locale == null) return;
		executor.submit(new Runnable() {
			@Override
			public void run() {
				settings.saveLocale(locale.toLanguageTag());
				currentLocaleValue = locale;
				try {
					currentLocale.postValue(locale);
				} catch (RuntimeException e) {
					// ignore when no main looper
				}
			}
		});
	}

	/** Returns the current locale synchronously (may be null until loaded). */
	public Locale getCurrentLocale() {
		return currentLocaleValue != null ? currentLocaleValue : currentLocale.getValue();
	}

	/** Returns whether the provided locale is supported by the app. */
	public boolean isLocaleSupported(Locale locale) {
		return locale != null && supportedLocales.contains(locale);
	}
}

