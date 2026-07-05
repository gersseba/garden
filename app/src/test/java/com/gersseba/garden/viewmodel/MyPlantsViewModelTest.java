package com.gersseba.garden.viewmodel;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gersseba.garden.model.Plant;
import com.gersseba.garden.model.PlantPhoto;
import com.gersseba.garden.repository.PlantRepositoryContract;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for {@link MyPlantsViewModel}.
 * Tests cover repository-backed list state and add-plant behavior.
 */
public class MyPlantsViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MyPlantsViewModel viewModel;
    private FakePlantRepository repository;

    @Before
    public void setUp() {
        repository = new FakePlantRepository();
        viewModel = new MyPlantsViewModel(
                new Application(),
                repository,
                Collections.singletonList("Monstera deliciosa"),
                new FixedRandom(),
                "Araceae",
                "Bright indirect light",
                "Every 7-10 days",
                "Well-draining mix",
                true,
                "Generic notes",
                new int[] {1, 2, 3},
                new String[] {"Healthy", "Stable roots", "Strong growth"});
    }

    @Test
    public void getPlants_returnsRepositoryLiveData() {
        List<Plant> plants = viewModel.getPlants().getValue();
        assertNotNull(plants);
        assertEquals(0, plants.size());
    }

    @Test
    public void addRandomPlant_persistsPlantAndPhotosViaRepository() {
        viewModel.addRandomPlant();

        assertEquals(1, repository.addCalls);
        assertEquals("Monstera deliciosa", repository.lastCommonName);
        assertEquals(3, repository.lastPhotoDrawables.length);
        assertEquals(3, repository.lastPhotoSummaries.length);

        List<Plant> plants = viewModel.getPlants().getValue();
        assertNotNull(plants);
        assertEquals(1, plants.size());
        assertEquals("Monstera deliciosa", plants.get(0).name);
    }

    private static final class FixedRandom extends Random {
        @Override
        public int nextInt(int bound) {
            return 0;
        }
    }

    private static final class FakePlantRepository implements PlantRepositoryContract {
        private final MutableLiveData<List<Plant>> plantsLiveData = new MutableLiveData<>(new ArrayList<>());
        private final MutableLiveData<Plant> plantLiveData = new MutableLiveData<>();
        private final MutableLiveData<List<PlantPhoto>> photosLiveData = new MutableLiveData<>(new ArrayList<>());

        int addCalls;
        String lastCommonName;
        int[] lastPhotoDrawables = new int[0];
        String[] lastPhotoSummaries = new String[0];

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
        public void addPlant(String commonName, String scientificName, String plantFamily, String sunExposure, String wateringFrequency, String soilType, boolean isIndoor, String notes, int[] photoDrawableIds, String[] photoSummaries) {
            addCalls++;
            lastCommonName = commonName;
            lastPhotoDrawables = photoDrawableIds;
            lastPhotoSummaries = photoSummaries;

            List<Plant> current = plantsLiveData.getValue();
            if (current == null) {
                current = new ArrayList<>();
            }
            current.add(new Plant(0L, commonName, scientificName, plantFamily, LocalDate.now(), sunExposure, wateringFrequency, soilType, isIndoor, notes));
            plantsLiveData.setValue(current);
        }

        @Override
        public void deletePhoto(long photoId) {
            // Not used in MyPlantsViewModel tests.
        }
    }
}
