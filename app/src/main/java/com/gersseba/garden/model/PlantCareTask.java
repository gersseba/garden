package com.gersseba.garden.model;

import androidx.annotation.StringRes;

/**
 * Presentation model for a single plant-specific care task shown on the detail screen.
 *
 * Forward-compatible: field names align with {@code CarePlanEntity}:
 *   - {@code taskTypeRes}    → {@code CarePlanEntity.taskType}
 *   - {@code descriptionRes} → {@code CarePlanEntity.description}
 */
public class PlantCareTask {

    @StringRes public final int taskTypeRes;
    @StringRes public final int descriptionRes;

    public PlantCareTask(@StringRes int taskTypeRes, @StringRes int descriptionRes) {
        this.taskTypeRes = taskTypeRes;
        this.descriptionRes = descriptionRes;
    }
}

