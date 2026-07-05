package com.gersseba.garden.model;

import androidx.annotation.NonNull;

import java.time.LocalDate;

/**
 * Presentation model for a persisted plant entry shown in My Plants and Plant Detail.
 */
public class Plant {

    public final long id;
    @NonNull public final String name;
    @NonNull public final String scientificName;
    @NonNull public final String plantFamily;
    @NonNull public final LocalDate dateAdded;
    @NonNull public final String sunExposure;
    @NonNull public final String wateringFrequency;
    @NonNull public final String soilType;
    public final boolean isIndoor;
    @NonNull public final String notes;

    public Plant(
            long id,
            @NonNull String name,
            @NonNull String scientificName,
            @NonNull String plantFamily,
            @NonNull LocalDate dateAdded,
            @NonNull String sunExposure,
            @NonNull String wateringFrequency,
            @NonNull String soilType,
            boolean isIndoor,
            @NonNull String notes) {
        this.id = id;
        this.name = name;
        this.scientificName = scientificName;
        this.plantFamily = plantFamily;
        this.dateAdded = dateAdded;
        this.sunExposure = sunExposure;
        this.wateringFrequency = wateringFrequency;
        this.soilType = soilType;
        this.isIndoor = isIndoor;
        this.notes = notes;
    }
}
