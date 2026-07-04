package com.gersseba.garden.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.gersseba.garden.model.MockPlant;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for {@link MyPlantsViewModel}.
 * Tests cover add-item and find-by-id behaviour.
 */
public class MyPlantsViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MyPlantsViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new MyPlantsViewModel();
    }

    @Test
    public void initialList_isEmpty() {
        List<MockPlant> plants = viewModel.getPlants().getValue();
        assertNotNull(plants);
        assertEquals(0, plants.size());
    }

    @Test
    public void addRandomPlant_incrementsListSize() {
        viewModel.addRandomPlant();
        List<MockPlant> plants = viewModel.getPlants().getValue();
        assertNotNull(plants);
        assertEquals(1, plants.size());
    }

    @Test
    public void addRandomPlant_thricE_listHasThreeEntries() {
        viewModel.addRandomPlant();
        viewModel.addRandomPlant();
        viewModel.addRandomPlant();
        List<MockPlant> plants = viewModel.getPlants().getValue();
        assertNotNull(plants);
        assertEquals(3, plants.size());
    }

    @Test
    public void addedPlant_hasNonEmptyName() {
        viewModel.addRandomPlant();
        MockPlant plant = viewModel.getPlants().getValue().get(0);
        assertNotNull(plant.name);
        assertEquals(false, plant.name.isEmpty());
    }

    @Test
    public void addedPlant_hasUniqueIds() {
        viewModel.addRandomPlant();
        viewModel.addRandomPlant();
        List<MockPlant> plants = viewModel.getPlants().getValue();
        assertNotNull(plants);
        long id0 = plants.get(0).id;
        long id1 = plants.get(1).id;
        assertEquals(false, id0 == id1);
    }

    @Test
    public void findPlantById_returnsCorrectPlant() {
        viewModel.addRandomPlant();
        viewModel.addRandomPlant();
        List<MockPlant> plants = viewModel.getPlants().getValue();
        assertNotNull(plants);
        MockPlant expected = plants.get(1);
        MockPlant found = viewModel.findPlantById(expected.id);
        assertNotNull(found);
        assertEquals(expected.id, found.id);
        assertEquals(expected.name, found.name);
    }

    @Test
    public void findPlantById_unknownId_returnsNull() {
        viewModel.addRandomPlant();
        assertNull(viewModel.findPlantById(9999L));
    }
}
