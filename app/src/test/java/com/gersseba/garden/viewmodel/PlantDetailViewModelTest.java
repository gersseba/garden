package com.gersseba.garden.viewmodel;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gersseba.garden.model.Plant;
import com.gersseba.garden.model.PlantCareTask;
import com.gersseba.garden.model.PlantDetailInfo;
import com.gersseba.garden.model.PlantPhoto;
import com.gersseba.garden.repository.PlantRepositoryContract;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for {@link PlantDetailViewModel} repository-backed detail state.
 */
public class PlantDetailViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private PlantDetailViewModel viewModel;
    private FakePlantRepository repository;

    @Before
    public void setUp() {
        repository = new FakePlantRepository();
        viewModel = new PlantDetailViewModel(new Application(), repository);
    }

    @Test
    public void init_loadsPlantNameFromRepository() {
        repository.setPlant(new Plant(
                7L,
                "Monstera deliciosa",
                "Monstera deliciosa",
                "Araceae",
                LocalDate.now(),
                "Bright indirect light",
                "Every 7-10 days",
                "Well-draining mix",
                true,
                ""));
        viewModel.getPlantName().observeForever(name -> {}); // Force LiveData transformation
        viewModel.init(7L);

        assertEquals("Monstera deliciosa", viewModel.getPlantName().getValue());
    }

    @Test
    public void init_loadsGeneralInfoFromRepository() {
        repository.setPlant(new Plant(
                10L,
                "Snake plant",
                "Dracaena trifasciata",
                "Asparagaceae",
                LocalDate.of(2024, 1, 1),
                "Medium light",
                "Every 14 days",
                "Cactus blend",
                true,
                "Slow grower"));
        viewModel.getGeneralInfo().observeForever(info -> {}); // Force LiveData transformation
        viewModel.init(10L);

        PlantDetailInfo info = viewModel.getGeneralInfo().getValue();
        assertNotNull(info);
        assertEquals("Dracaena trifasciata", info.scientificName);
        assertEquals("Asparagaceae", info.plantFamily);
        assertEquals("2024-01-01", info.dateAdded);
        assertEquals("Medium light", info.sunExposure);
        assertEquals("Every 14 days", info.wateringFrequency);
        assertEquals("Cactus blend", info.soilType);
        assertEquals("Placeholder Text", info.description);
        assertEquals("Placeholder Text", info.healthHumansClassification);
        assertEquals("Placeholder Text", info.carePlacement);
    }

    @Test
    public void init_loadsPhotosFromRepository() {
        repository.setPhotos(Collections.singletonList(new PlantPhoto(1L, 123, "", LocalDateTime.now(), "Healthy growth")));
        viewModel.getPhotos().observeForever(photos -> {}); // Force LiveData transformation
        viewModel.init(4L);

        List<PlantPhoto> photos = viewModel.getPhotos().getValue();
        assertNotNull(photos);
        assertEquals(1, photos.size());
        assertEquals(123, photos.get(0).imageResId);
        assertEquals("Healthy growth", photos.get(0).aiSummary);
    }

    @Test
    public void deletePhotoAt_callsRepository() {
        PlantPhoto photo = new PlantPhoto(100L, 123, "", LocalDateTime.now(), "");
        repository.setPhotos(Collections.singletonList(photo));
        viewModel.getPhotos().observeForever(photos -> {});
        viewModel.init(1L);

        viewModel.deletePhotoAt(0);

        assertEquals(100L, repository.lastDeletedPhotoId);
    }

    @Test
    public void getCareTasks_returnsMockedDefaultTasks() {
        List<PlantCareTask> tasks = viewModel.getCareTasks().getValue();

        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());
        assertEquals(4, tasks.size());
    }

    private static final class FakePlantRepository implements PlantRepositoryContract {
        private final MutableLiveData<List<Plant>> plantsLiveData = new MutableLiveData<>(Collections.emptyList());
        private final MutableLiveData<Plant> plantLiveData = new MutableLiveData<>();
        private final MutableLiveData<List<PlantPhoto>> photosLiveData = new MutableLiveData<>(Collections.emptyList());
        long lastDeletedPhotoId = -1L;

        void setPlant(Plant plant) {
            plantLiveData.setValue(plant);
        }

        void setPhotos(List<PlantPhoto> photos) {
            photosLiveData.setValue(photos);
        }

        @Override
        public LiveData<List<Plant>> observePlants() {
            return plantsLiveData;
        }

        @Override
        public LiveData<Plant> observePlant(long plantId) {
            return plantLiveData;
        }

        @Override
        public LiveData<List<PlantPhoto>> observePhotosForPlant(long plantId) {
            return photosLiveData;
        }

        @Override
        public void addPlant(String commonName,
                String scientificName,
                String plantFamily,
                String sunExposure,
                String wateringFrequency,
                String soilType,
                boolean isIndoor,
                String notes,
                int[] photoDrawableIds,
                String[] photoSummaries) {
            // Not used in PlantDetailViewModel tests.
        }

        @Override
        public void deletePhoto(long photoId) {
            lastDeletedPhotoId = photoId;
        }
    }
}
