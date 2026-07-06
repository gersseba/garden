package com.gersseba.garden.viewmodel;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gersseba.garden.database.dao.LocalizedTextDao;
import com.gersseba.garden.database.entity.LocalizedTextEntity;
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
    private FakeLocalizedTextRepository localizedTextRepository;

    @Before
    public void setUp() {
        repository = new FakePlantRepository();
        localizedTextRepository = new FakeLocalizedTextRepository();
        viewModel = new PlantDetailViewModel(new Application(), repository, localizedTextRepository, null);
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
    public void deletePlant_callsRepositoryAndSetsDeletedState() {
        viewModel.init(42L);
        viewModel.getPlantDeleted().observeForever(deleted -> {});

        viewModel.deletePlant();

        assertEquals(42L, repository.lastDeletedPlantId);
        assertEquals(true, viewModel.getPlantDeleted().getValue());
    }

    @Test
    public void getCareTasks_returnsMockedDefaultTasks() {
        List<PlantCareTask> tasks = viewModel.getCareTasks().getValue();

        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());
        assertEquals(4, tasks.size());
    }

    @Test
    public void getGeneralInfoText_returnsNullWhenNoDb() {
        // Use a viewmodel without localized repo to test fallback
        PlantDetailViewModel noDbViewModel = new PlantDetailViewModel(new Application(), repository, null, null);
        repository.setPlant(new Plant(
                20L,
                "Test",
                "Test",
                "Family",
                LocalDate.now(),
                "Light",
                "Water",
                "Soil",
                true,
                ""));
        noDbViewModel.getGeneralInfoText().observeForever(s -> {});
        noDbViewModel.init(20L);

        assertEquals(null, noDbViewModel.getGeneralInfoText().getValue());
    }

    @Test
    public void photoSummary_prefersLocalizedDbValue() {
        long photoId = 200L;
        PlantPhoto p = new PlantPhoto(photoId, 111, "", LocalDateTime.now(), "Original AI Summary");
        repository.setPhotos(Collections.singletonList(p));

        // Setup localized text in DB
        localizedTextRepository.setLocalizedText("photo", photoId, "ai_summary", "Localized DB Summary", null);

        viewModel.getPhotos().observeForever(list -> {});
        viewModel.getCurrentPhotoSummary().observeForever(s -> {});
        viewModel.init(5L);
        viewModel.setSelectedPhotoId(photoId);

        // Should return the DB summary
        assertEquals("Localized DB Summary", viewModel.getCurrentPhotoSummary().getValue());
    }

    @Test
    public void photoSummary_fallsBackToNullWhenNoDbValue() {
        long photoId = 201L;
        PlantPhoto p = new PlantPhoto(photoId, 111, "", LocalDateTime.now(), "Original AI Summary");
        repository.setPhotos(Collections.singletonList(p));

        viewModel.getPhotos().observeForever(list -> {});
        viewModel.getCurrentPhotoSummary().observeForever(s -> {});
        viewModel.init(6L);
        viewModel.setSelectedPhotoId(photoId);

        // Should return null from DB (fragment handles fallback to photo.aiSummary)
        assertEquals(null, viewModel.getCurrentPhotoSummary().getValue());
    }

    private static final class FakeLocalizedTextRepository extends com.gersseba.garden.repository.LocalizedTextRepository {
        private final MutableLiveData<String> textLiveData = new MutableLiveData<>();

        FakeLocalizedTextRepository() {
            super(new LocalizedTextDao() {
                @Override
                public long insertOrUpdate(LocalizedTextEntity entity) { return 0; }
                @Override
                public LiveData<LocalizedTextEntity> getByEntityAndKeyLive(String entityType, long entityId, String key) { return null; }
                @Override
                public LocalizedTextEntity getByEntityAndKeySync(String entityType, long entityId, String key) { return null; }
                @Override
                public void deleteByEntity(String entityType, long entityId) {}
                @Override
                public void deleteById(long id) {}
            }, Runnable::run);
        }

        void setLocalizedText(String entityType, long entityId, String key, String textEn, String textDe) {
            textLiveData.setValue(textEn); // Simplification for test: return En
        }

        @Override
        public LiveData<String> getLocalizedTextLive(String entityType, long entityId, String key, java.util.Locale locale) {
            return textLiveData;
        }
    }

    private static final class FakePlantRepository implements PlantRepositoryContract {
        private final MutableLiveData<List<Plant>> plantsLiveData = new MutableLiveData<>(Collections.emptyList());
        private final MutableLiveData<Plant> plantLiveData = new MutableLiveData<>();
        private final MutableLiveData<List<PlantPhoto>> photosLiveData = new MutableLiveData<>(Collections.emptyList());
        long lastDeletedPhotoId = -1L;
        long lastDeletedPlantId = -1L;

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

        @Override
        public void deletePlant(long plantId) {
            lastDeletedPlantId = plantId;
        }
    }
}
