package com.gersseba.garden.model;

import androidx.annotation.StringRes;

/**
 * Presentation model for a monthly care task.
 * Uses string resource IDs so the model stays context-free while all
 * user-facing text lives in strings.xml.
 *
 * Forward-compatible: fields align with the future CareTaskEntity schema
 * (taskType → taskNameRes, plantId → plantNameRes, reason → descriptionRes).
 */
public class CurrentCareTask {

    @StringRes
    public final int taskNameRes;

    @StringRes
    public final int plantNameRes;

    @StringRes
    public final int descriptionRes;

    public CurrentCareTask(
            @StringRes int taskNameRes,
            @StringRes int plantNameRes,
            @StringRes int descriptionRes) {
        this.taskNameRes = taskNameRes;
        this.plantNameRes = plantNameRes;
        this.descriptionRes = descriptionRes;
    }
}

