package com.gersseba.garden.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gersseba.garden.R;
import com.gersseba.garden.model.PlantCareTask;
import com.gersseba.garden.model.PlantDetailInfo;
import com.gersseba.garden.model.PlantPhoto;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for the Plant Detail screen.
 *
 * Holds all mocked data for a selected plant and survives configuration changes.
 * Replace the {@code buildMocked*()} methods with repository calls when Room and
 * Gemini AI are wired up — the LiveData contracts and field names remain the same.
 */
public class PlantDetailViewModel extends ViewModel {

    private final MutableLiveData<String> plantNameLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<PlantPhoto>> photosLiveData = new MutableLiveData<>();
    private final MutableLiveData<PlantDetailInfo> generalInfoLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<PlantCareTask>> careTasksLiveData = new MutableLiveData<>();

    /**
     * Initialises the ViewModel with data for the given plant.
     * Safe to call on every {@code onViewCreated} — data is only populated once.
     *
     * @param plantId   future Room primary key; unused by mocks but kept for forward-compatibility
     * @param plantName display name passed via navigation argument
     */
    public void init(long plantId, String plantName) {
        if (plantNameLiveData.getValue() != null) {
            return; // already initialised; skip on re-attach after rotation
        }
        plantNameLiveData.setValue(plantName);
        photosLiveData.setValue(buildMockedPhotos());
        generalInfoLiveData.setValue(buildMockedGeneralInfo());
        careTasksLiveData.setValue(buildMockedCareTasks());
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

    public LiveData<List<PlantCareTask>> getCareTasks() {
        return careTasksLiveData;
    }

    // -----------------------------------------------------------------------
    // Mocked data builders — public for direct unit testing without LiveData
    // -----------------------------------------------------------------------

    /**
     * Returns three mocked plant photos with AI-generated summaries.
     * Replace with a repository query (e.g. {@code photoRepository.getPhotosForPlant(plantId)}).
     */
    public List<PlantPhoto> buildMockedPhotos() {
        List<PlantPhoto> photos = new ArrayList<>();
        photos.add(new PlantPhoto(R.drawable.plant_placeholder,   R.string.mock_photo_summary_1));
        photos.add(new PlantPhoto(R.drawable.plant_placeholder_b, R.string.mock_photo_summary_2));
        photos.add(new PlantPhoto(R.drawable.plant_placeholder_c, R.string.mock_photo_summary_3));
        return photos;
    }

    /**
     * Returns mocked general plant info.
     * Replace with {@code PlantEntity} fields from the Room database.
     */
    public PlantDetailInfo buildMockedGeneralInfo() {
        return new PlantDetailInfo(
                R.string.mock_scientific_name,
                R.string.mock_plant_family,
                R.string.mock_sun_exposure,
                R.string.mock_watering_frequency,
                R.string.mock_soil_type);
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

