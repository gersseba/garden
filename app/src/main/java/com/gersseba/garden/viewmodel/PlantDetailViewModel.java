package com.gersseba.garden.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Transformations;

import com.gersseba.garden.R;
import com.gersseba.garden.model.Plant;
import com.gersseba.garden.model.PlantCareTask;
import com.gersseba.garden.model.PlantDetailInfo;
import com.gersseba.garden.model.PlantPhoto;
import com.gersseba.garden.repository.PlantRepository;
import com.gersseba.garden.repository.PlantRepositoryContract;
import com.gersseba.garden.repository.LocalizedTextRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for the Plant Detail screen.
 *
 * Holds detail screen state for a selected plant loaded from persistence.
 */
public class PlantDetailViewModel extends AndroidViewModel {

    private final PlantRepositoryContract repository;
    private final MutableLiveData<Long> selectedPlantId = new MutableLiveData<>();
    private final LiveData<Plant> selectedPlantLiveData;
    private final LiveData<String> plantNameLiveData;
    private final LiveData<List<PlantPhoto>> photosLiveData;
    private final LiveData<PlantDetailInfo> generalInfoLiveData;
    private final LiveData<String> generalInfoTextLiveData;
    private final LocalizedTextRepository localizedTextRepository;
    private final MutableLiveData<Long> selectedPhotoId = new MutableLiveData<>();
    private final LiveData<String> photoSummaryLiveData;
    // Locale manager is created lazily; visible for tests via constructor injection
    private final com.gersseba.garden.i18n.LocaleManager localeManager;
    private final MutableLiveData<List<PlantCareTask>> careTasksLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> plantDeletedLiveData = new MutableLiveData<>(false);

    public PlantDetailViewModel(@NonNull Application application) {
        this(application, new PlantRepository(application), null, null);
    }

    PlantDetailViewModel(@NonNull Application application,
            @NonNull PlantRepositoryContract repository) {
        this(application, repository, null, null);
    }

    /**
     * Constructor for dependency injection via ViewModelFactory.
     * Visible for testing and DI: accept injected LocalizedTextRepository and LocaleManager.
     */
    public PlantDetailViewModel(@NonNull Application application,
            @NonNull PlantRepositoryContract repository,
            LocalizedTextRepository localizedTextRepository,
            com.gersseba.garden.i18n.LocaleManager localeManager) {
        super(application);
        this.repository = repository;

        LocalizedTextRepository tmp = localizedTextRepository;
        if (tmp == null) {
            try {
                tmp = new LocalizedTextRepository(application);
            } catch (Exception ignored) {
                // In unit tests the Room database may not be available; fall back to null repository.
                tmp = null;
            }
        }
        this.localizedTextRepository = tmp;

        if (localeManager == null) {
            com.gersseba.garden.i18n.SettingsDataStoreImpl store = null;
            try {
                store = com.gersseba.garden.i18n.SettingsDataStoreImpl.getInstance(application);
            } catch (Exception ignored) {
                // tests may not provide DataStore; localeManager will be null and fallback to Locale.getDefault()
            }
            this.localeManager = store == null ? null : new com.gersseba.garden.i18n.LocaleManager(store);
        } else {
            this.localeManager = localeManager;
        }

        this.photoSummaryLiveData = Transformations.switchMap(selectedPhotoId, photoId -> {
            if (photoId == null || this.localizedTextRepository == null) {
                return new MutableLiveData<>(null);
            }
            return this.localizedTextRepository.getLocalizedTextLive("photo", photoId, "ai_summary", determineLocale());
        });
        this.selectedPlantLiveData = Transformations.switchMap(
                selectedPlantId,
                repository::observePlant);
        this.photosLiveData = Transformations.switchMap(
                selectedPlantId,
                repository::observePhotosForPlant);
        this.plantNameLiveData = Transformations.map(selectedPlantLiveData,
                plant -> plant != null ? plant.name : "");
        this.generalInfoLiveData = Transformations.map(selectedPlantLiveData,
                this::mapDetailInfo);

        this.generalInfoTextLiveData = Transformations.switchMap(selectedPlantLiveData, plant -> {
            if (plant == null) return new MutableLiveData<>(null);
            if (this.localizedTextRepository == null) return new MutableLiveData<>(null);
            java.util.Locale locale = determineLocale();
            return this.localizedTextRepository.getLocalizedTextLive("plant", plant.id, "general_info", locale);
        });

        careTasksLiveData.setValue(buildMockedCareTasks());
    }

    /**
     * Initialises the ViewModel with data for the given plant.
     * Safe to call on every {@code onViewCreated} — data is only populated once.
     *
     * @param plantId selected plant primary key.
     */
    public void init(long plantId) {
        Long current = selectedPlantId.getValue();
        if (current != null && current == plantId) {
            return; // already initialised; skip on re-attach after rotation
        }
        selectedPlantId.setValue(plantId);
    }

    public LiveData<String> getPlantName() {
        return plantNameLiveData;
    }

    public LiveData<List<PlantPhoto>> getPhotos() {
        return photosLiveData;
    }

    public LiveData<PlantDetailInfo> getGeneralInfo() {
        return generalInfoLiveData;
    }

    public LiveData<String> getGeneralInfoText() {
        return generalInfoTextLiveData;
    }

    /**
     * Sets the currently viewed photo ID to update the localized summary.
     */
    public void setSelectedPhotoId(long photoId) {
        selectedPhotoId.setValue(photoId);
    }

    /**
     * Returns the localized AI summary for the currently selected photo.
     */
    public LiveData<String> getCurrentPhotoSummary() {
        return photoSummaryLiveData;
    }

    /**
     * Returns the localized AI summary for a photo if present in DB; null otherwise.
     */
    public LiveData<String> getPhotoSummaryLive(long photoId) {
        if (localizedTextRepository == null) return new MutableLiveData<>(null);
        return localizedTextRepository.getLocalizedTextLive("photo", photoId, "ai_summary", determineLocale());
    }

    private java.util.Locale determineLocale() {
        try {
            java.util.Locale cur = localeManager == null ? null : localeManager.getCurrentLocale();
            if (cur != null) return cur;
        } catch (Exception ignored) {
            // fall back
        }
        return java.util.Locale.getDefault();
    }

    public LiveData<List<PlantCareTask>> getCareTasks() {
        return careTasksLiveData;
    }

    public LiveData<Boolean> getPlantDeleted() {
        return plantDeletedLiveData;
    }

    /**
     * Deletes a photo at the given position in the current photo list.
     * @param position index in the {@link #getPhotos()} list.
     */
    public void deletePhotoAt(int position) {
        List<PlantPhoto> photos = photosLiveData.getValue();
        if (photos != null && position >= 0 && position < photos.size()) {
            repository.deletePhoto(photos.get(position).id);
        }
    }

    /**
     * Deletes the currently selected plant.
     */
    public void deletePlant() {
        Long plantId = selectedPlantId.getValue();
        if (plantId != null) {
            repository.deletePlant(plantId);
            plantDeletedLiveData.setValue(true);
        }
    }

    private PlantDetailInfo mapDetailInfo(Plant plant) {
        if (plant == null) {
            return new PlantDetailInfo("", "", "", "", "", "", false, "", "", "", "", "", "", "", "", "", "", "");
        }
        return new PlantDetailInfo(
                plant.scientificName,
                plant.plantFamily,
                plant.dateAdded.toString(),
                plant.sunExposure,
                plant.wateringFrequency,
                plant.soilType,
                plant.isIndoor,
                plant.notes,
                safeGetString(R.string.mock_description),
                safeGetString(R.string.health_classification_good),
                safeGetString(R.string.mock_health_humans),
                safeGetString(R.string.health_classification_bad),
                safeGetString(R.string.mock_health_cats),
                safeGetString(R.string.health_classification_ok),
                safeGetString(R.string.mock_health_tortoises),
                safeGetString(R.string.mock_care_placement),
                safeGetString(R.string.mock_care_cutting),
                safeGetString(R.string.mock_care_nutrients));
    }

    private String safeGetString(@androidx.annotation.StringRes int resId) {
        try {
            return getApplication().getString(resId);
        } catch (Exception e) {
            // Return a placeholder for unit tests where Application context may be limited
            return "Placeholder Text";
        }
    }

    /**
     * Returns a mocked plant-specific care plan with at least three tasks.
     * Replace with {@code CarePlanEntity} records from the Room database.
     */
    public List<PlantCareTask> buildMockedCareTasks() {
        List<PlantCareTask> tasks = new ArrayList<>();
        tasks.add(new PlantCareTask(
                R.string.detail_task_prune_spring,
                R.string.detail_task_prune_spring_desc));
        tasks.add(new PlantCareTask(
                R.string.detail_task_fertilize_monthly,
                R.string.detail_task_fertilize_monthly_desc));
        tasks.add(new PlantCareTask(
                R.string.detail_task_repot_biennial,
                R.string.detail_task_repot_biennial_desc));
        tasks.add(new PlantCareTask(
                R.string.detail_task_wipe_leaves,
                R.string.detail_task_wipe_leaves_desc));
        return tasks;
    }
}
