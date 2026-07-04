package com.gersseba.garden;

import com.gersseba.garden.model.CurrentCareTask;
import com.gersseba.garden.viewmodel.CarePlanViewModel;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for {@link CarePlanViewModel} mocked task data.
 *
 * Tests call {@code buildMockedTasks()} directly to avoid LiveData/Looper
 * setup that would require a device or Robolectric.
 */
public class CarePlanViewModelTest {

    @Test
    public void buildMockedTasks_returnsNonEmptyList() {
        CarePlanViewModel viewModel = new CarePlanViewModel();
        List<CurrentCareTask> tasks = viewModel.buildMockedTasks();

        assertNotNull(tasks);
        assertFalse("Expected at least one mocked task", tasks.isEmpty());
    }

    @Test
    public void buildMockedTasks_returnsExpectedCount() {
        CarePlanViewModel viewModel = new CarePlanViewModel();
        List<CurrentCareTask> tasks = viewModel.buildMockedTasks();

        assertEquals("Expected 6 mocked care tasks", 6, tasks.size());
    }

    @Test
    public void buildMockedTasks_allTasksHaveNonZeroResourceIds() {
        CarePlanViewModel viewModel = new CarePlanViewModel();
        List<CurrentCareTask> tasks = viewModel.buildMockedTasks();

        for (CurrentCareTask task : tasks) {
            assertFalse("taskNameRes must be a valid resource ID", task.taskNameRes == 0);
            assertFalse("plantNameRes must be a valid resource ID", task.plantNameRes == 0);
            assertFalse("descriptionRes must be a valid resource ID", task.descriptionRes == 0);
        }
    }

    @Test
    public void currentCareTask_storesAllFields() {
        CurrentCareTask task = new CurrentCareTask(1, 2, 3);

        assertEquals(1, task.taskNameRes);
        assertEquals(2, task.plantNameRes);
        assertEquals(3, task.descriptionRes);
    }
}

