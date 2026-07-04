package com.gersseba.garden.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gersseba.garden.model.MockPlant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Holds the mocked My Plants list state and survives configuration changes.
 * All data is in-memory; Room persistence will replace this in a later ticket.
 */
public class MyPlantsViewModel extends ViewModel {

    /** Full catalog of mock plants that can be added to the list. */
    private static final List<String> MOCK_CATALOG = Arrays.asList(
            "Monstera deliciosa",
            "Snake plant",
            "Cherry tomato",
            "Pothos",
            "Peace lily",
            "Fiddle-leaf fig",
            "Aloe vera",
            "Cactus",
            "Lavender",
            "Basil"
    );

    private final MutableLiveData<List<MockPlant>> plants = new MutableLiveData<>(new ArrayList<>());
    private long nextId = 1;
    private final Random random = new Random();

    /** Observable list of plants shown in the My Plants screen. */
    public LiveData<List<MockPlant>> getPlants() {
        return plants;
    }

    /**
     * Appends a randomly selected plant from the mock catalog to the list.
     * Duplicate entries are allowed to keep the implementation simple.
     */
    public void addRandomPlant() {
        String name = MOCK_CATALOG.get(random.nextInt(MOCK_CATALOG.size()));
        List<MockPlant> current = new ArrayList<>(currentList());
        current.add(new MockPlant(nextId++, name));
        plants.setValue(current);
    }

    /** Returns the plant with the given id, or {@code null} if not found. */
    public MockPlant findPlantById(long id) {
        for (MockPlant plant : currentList()) {
            if (plant.id == id) {
                return plant;
            }
        }
        return null;
    }

    private List<MockPlant> currentList() {
        List<MockPlant> list = plants.getValue();
        return list != null ? list : new ArrayList<>();
    }
}

