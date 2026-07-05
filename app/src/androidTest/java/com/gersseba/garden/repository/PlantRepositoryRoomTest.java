package com.gersseba.garden.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.gersseba.garden.database.AppDatabase;
import com.gersseba.garden.model.Plant;
import com.gersseba.garden.model.PlantPhoto;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class PlantRepositoryRoomTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AppDatabase database;
    private PlantRepository repository;

    @Before
    public void setUp() {
        database = Room.inMemoryDatabaseBuilder(
                        ApplicationProvider.getApplicationContext(),
                        AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        repository = new PlantRepository(database, Runnable::run);
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void addPlant_persistsPlantAndLinkedPhotos() throws InterruptedException {
        repository.addPlant(
                "Monstera deliciosa",
                "Monstera deliciosa",
                "Araceae",
                "Bright indirect light",
                "Every 7-10 days",
                "Well-draining mix",
                true,
                "Generic notes",
                new int[] {11, 22},
                new String[] {"Healthy", "Stable roots"});

        List<Plant> plants = awaitValue(repository.observePlants());
        assertEquals(1, plants.size());

        Plant savedPlant = plants.get(0);
        assertNotNull(savedPlant);
        assertEquals("Monstera deliciosa", savedPlant.name);
        assertEquals("Generic notes", savedPlant.notes);

        List<PlantPhoto> photos = awaitValue(repository.observePhotosForPlant(savedPlant.id));
        assertEquals(2, photos.size());
        assertEquals("Stable roots", photos.get(0).aiSummary);
        assertEquals("Healthy", photos.get(1).aiSummary);
    }

    @Test
    public void deletePhoto_removesRecordFromPersistence() throws InterruptedException {
        repository.addPlant("Test", "Test", "Test", "Test", "Test", "Test", true, "", new int[]{1}, new String[]{""});
        Plant plant = awaitValue(repository.observePlants()).get(0);
        List<PlantPhoto> photosBefore = awaitValue(repository.observePhotosForPlant(plant.id));
        long photoIdToDelete = photosBefore.get(0).id;

        repository.deletePhoto(photoIdToDelete);

        List<PlantPhoto> photosAfter = awaitValue(repository.observePhotosForPlant(plant.id));
        assertEquals(0, photosAfter.size());
    }

    @Test
    public void deletePlant_removesPlantAndCascadesToPhotos() throws InterruptedException {
        repository.addPlant("DeleteMe", "Del", "Del", "Del", "Del", "Del", true, "", new int[]{1, 2}, new String[]{"P1", "P2"});
        List<Plant> plantsBefore = awaitValue(repository.observePlants());
        Plant plantToDelete = plantsBefore.get(0);

        repository.deletePlant(plantToDelete.id);

        List<Plant> plantsAfter = awaitValue(repository.observePlants());
        assertEquals(0, plantsAfter.size());

        List<PlantPhoto> photosAfter = awaitValue(repository.observePhotosForPlant(plantToDelete.id));
        assertEquals(0, photosAfter.size());
    }

    private static <T> T awaitValue(LiveData<T> liveData) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        List<T> values = new ArrayList<>();

        liveData.observeForever(value -> {
            values.add(value);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
        return values.get(values.size() - 1);
    }
}
