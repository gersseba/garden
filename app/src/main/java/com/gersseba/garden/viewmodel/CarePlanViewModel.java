package com.gersseba.garden.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gersseba.garden.R;
import com.gersseba.garden.model.CurrentCareTask;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for the Care Plan screen.
 * Provides an in-memory mocked list of this month's care tasks.
 *
 * Forward-compatible: replace buildMockedTasks() with a repository call when
 * real CareTaskEntity data is available.
 */
public class CarePlanViewModel extends ViewModel {

    private final MutableLiveData<List<CurrentCareTask>> tasksLiveData = new MutableLiveData<>();

    /**
     * Returns the LiveData-wrapped task list for observation from the Fragment.
     * Loads mocked data on first access.
     */
    public LiveData<List<CurrentCareTask>> getTasks() {
        if (tasksLiveData.getValue() == null) {
            tasksLiveData.setValue(buildMockedTasks());
        }
        return tasksLiveData;
    }

    /**
     * Builds the mocked list of care tasks for this month.
     * Public to allow direct testing without LiveData/Looper setup.
     */
    public List<CurrentCareTask> buildMockedTasks() {
        List<CurrentCareTask> tasks = new ArrayList<>();

        tasks.add(new CurrentCareTask(
                R.string.task_name_water,
                R.string.plant_name_monstera,
                R.string.task_desc_water_monstera));

        tasks.add(new CurrentCareTask(
                R.string.task_name_fertilize,
                R.string.plant_name_cherry_tomato,
                R.string.task_desc_fertilize_tomato));

        tasks.add(new CurrentCareTask(
                R.string.task_name_inspect_pests,
                R.string.plant_name_snake_plant,
                R.string.task_desc_inspect_snake_plant));

        tasks.add(new CurrentCareTask(
                R.string.task_name_prune,
                R.string.plant_name_monstera,
                R.string.task_desc_prune_monstera));

        tasks.add(new CurrentCareTask(
                R.string.task_name_rotate,
                R.string.plant_name_snake_plant,
                R.string.task_desc_rotate_snake_plant));

        tasks.add(new CurrentCareTask(
                R.string.task_name_refresh_compost,
                R.string.plant_name_cherry_tomato,
                R.string.task_desc_refresh_compost_tomato));

        return tasks;
    }
}


