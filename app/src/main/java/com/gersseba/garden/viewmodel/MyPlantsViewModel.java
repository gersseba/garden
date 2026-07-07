package com.gersseba.garden.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.AndroidViewModel;

import com.gersseba.garden.R;
import com.gersseba.garden.model.Plant;
import com.gersseba.garden.repository.PlantRepository;
import com.gersseba.garden.repository.PlantRepositoryContract;

import java.util.List;
import java.util.Random;

/**
 * Holds persisted My Plants list state and survives configuration changes.
 */
public class MyPlantsViewModel extends AndroidViewModel {

    private final PlantRepositoryContract repository;
    private final List<String> plantCatalog;
    private final Random random;
    private final String defaultPlantFamily;
    private final String defaultSunExposure;
    private final String defaultWateringFrequency;
    private final String defaultSoilType;
    private final boolean defaultIsIndoor;
    private final String defaultNotes;
    private final String[] defaultPhotoSummaries;
    private final int[] defaultPhotoDrawables;

    /**
     * Constructor accepting Application parameter.
     *
     * DEPRECATED: This constructor performs manual instantiation of dependencies.
     * For new code, use {@link com.gersseba.garden.di.ViewModelFactory} to ensure
     * proper dependency injection via {@link com.gersseba.garden.di.ServiceLocator}.
     *
     * Kept for backward compatibility with existing code that may instantiate this
     * ViewModel directly (e.g., before ServiceLocator migration was complete).
     * This should not be used in new fragments or activities; migrate to ViewModelFactory instead.
     */
    @Deprecated
    public MyPlantsViewModel(@NonNull Application application) {
        this(
                application,
                new PlantRepository(application),
                List.of(application.getResources().getStringArray(R.array.my_plants_catalog_entries)),
                new Random(),
                application.getString(R.string.default_plant_family),
                application.getString(R.string.default_sun_exposure),
                application.getString(R.string.default_watering_frequency),
                application.getString(R.string.default_soil_type),
                true,
                application.getString(R.string.default_plant_notes),
                new int[] {
                        R.drawable.plant_placeholder,
                        R.drawable.plant_placeholder_b,
                        R.drawable.plant_placeholder_c
                },
                new String[] {
                        application.getString(R.string.mock_photo_summary_1),
                        application.getString(R.string.mock_photo_summary_2),
                        application.getString(R.string.mock_photo_summary_3)
                });
    }

    /**
     * Constructor for dependency injection via ViewModelFactory.
     * Visible for testing and DI.
     */
    public MyPlantsViewModel(
            @NonNull Application application,
            @NonNull PlantRepositoryContract repository,
            @NonNull List<String> plantCatalog,
            @NonNull Random random,
            @NonNull String defaultPlantFamily,
            @NonNull String defaultSunExposure,
            @NonNull String defaultWateringFrequency,
            @NonNull String defaultSoilType,
            boolean defaultIsIndoor,
            @NonNull String defaultNotes,
            @NonNull int[] defaultPhotoDrawables,
            @NonNull String[] defaultPhotoSummaries) {
        super(application);
        this.repository = repository;
        this.plantCatalog = plantCatalog;
        this.random = random;
        this.defaultPlantFamily = defaultPlantFamily;
        this.defaultSunExposure = defaultSunExposure;
        this.defaultWateringFrequency = defaultWateringFrequency;
        this.defaultSoilType = defaultSoilType;
        this.defaultIsIndoor = defaultIsIndoor;
        this.defaultNotes = defaultNotes;
        this.defaultPhotoSummaries = defaultPhotoSummaries;
        this.defaultPhotoDrawables = defaultPhotoDrawables;
    }

    /** Observable list of plants shown in the My Plants screen. */
    public LiveData<List<Plant>> getPlants() {
        return repository.observePlants();
    }

    /**
     * Appends a randomly selected plant from the catalog to persistence.
     */
    public void addRandomPlant() {
        if (plantCatalog.isEmpty()) {
            return;
        }
        String name = plantCatalog.get(random.nextInt(plantCatalog.size()));
        repository.addPlant(
                name,
                name,
                defaultPlantFamily,
                defaultSunExposure,
                defaultWateringFrequency,
                defaultSoilType,
                defaultIsIndoor,
                defaultNotes,
                defaultPhotoDrawables,
                defaultPhotoSummaries);
    }
}
