package com.gersseba.garden.di;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.gersseba.garden.database.AppDatabase;
import com.gersseba.garden.i18n.LocaleManager;
import com.gersseba.garden.i18n.SettingsDataStoreImpl;
import com.gersseba.garden.repository.LocalizedTextRepository;
import com.gersseba.garden.repository.PlantRepository;
import com.gersseba.garden.repository.PlantRepositoryContract;

/**
 * Service locator for providing singleton instances of app dependencies.
 *
 * Provides:
 * - AppDatabase (Room singleton)
 * - PlantRepository (PlantRepositoryContract)
 * - LocalizedTextRepository
 * - LocaleManager
 *
 * This replaces manual factory logic and try-catch blocks in ViewModels.
 */
public class ServiceLocator {

    private static ServiceLocator instance;
    private AppDatabase appDatabase;
    private PlantRepositoryContract plantRepository;
    private LocalizedTextRepository localizedTextRepository;
    private LocaleManager localeManager;
    private final Context context;

    private ServiceLocator(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * Gets or creates the singleton ServiceLocator instance.
     */
    @NonNull
    public static synchronized ServiceLocator getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = new ServiceLocator(context);
        }
        return instance;
    }

    /**
     * Returns the singleton AppDatabase instance.
     * Thread-safe lazy initialization.
     */
    @NonNull
    public synchronized AppDatabase getAppDatabase() {
        if (appDatabase == null) {
            appDatabase = AppDatabase.getInstance(context);
        }
        return appDatabase;
    }

    /**
     * Returns the singleton PlantRepository instance.
     * Thread-safe lazy initialization.
     */
    @NonNull
    public synchronized PlantRepositoryContract getPlantRepository() {
        if (plantRepository == null) {
            plantRepository = new PlantRepository(getAppDatabase());
        }
        return plantRepository;
    }

    /**
     * Returns the singleton LocalizedTextRepository instance.
     * Thread-safe lazy initialization.
     */
    @NonNull
    public synchronized LocalizedTextRepository getLocalizedTextRepository() {
        if (localizedTextRepository == null) {
            localizedTextRepository = new LocalizedTextRepository(getAppDatabase());
        }
        return localizedTextRepository;
    }

    /**
     * Returns the singleton LocaleManager instance.
     * Thread-safe lazy initialization.
     */
    @NonNull
    public synchronized LocaleManager getLocaleManager() {
        if (localeManager == null) {
            SettingsDataStoreImpl settingsDataStore = SettingsDataStoreImpl.getInstance(context);
            localeManager = new LocaleManager(settingsDataStore);
        }
        return localeManager;
    }

    /**
     * Resets the singleton instances. Visible for testing only.
     */
    @VisibleForTesting
    public static synchronized void reset() {
        instance = null;
    }

    /**
     * Sets custom instances for testing. Visible for testing only.
     */
    @VisibleForTesting
    public void setPlantRepository(@NonNull PlantRepositoryContract repository) {
        this.plantRepository = repository;
    }

    /**
     * Sets custom instances for testing. Visible for testing only.
     */
    @VisibleForTesting
    public void setLocalizedTextRepository(@NonNull LocalizedTextRepository repository) {
        this.localizedTextRepository = repository;
    }

    /**
     * Sets custom instances for testing. Visible for testing only.
     */
    @VisibleForTesting
    public void setLocaleManager(@NonNull LocaleManager manager) {
        this.localeManager = manager;
    }
}



