package com.gersseba.garden.model;

import androidx.annotation.NonNull;

/**
 * Presentation model for the General Information section of the plant detail screen.
 *
 * Field names mirror {@code PlantEntity} so persistence data can be bound directly.
 */
public class PlantDetailInfo {

    @NonNull public final String scientificName;
    @NonNull public final String plantFamily;
    @NonNull public final String dateAdded;
    @NonNull public final String sunExposure;
    @NonNull public final String wateringFrequency;
    @NonNull public final String soilType;
    public final boolean isIndoor;
    @NonNull public final String notes;
    @NonNull public final String description;

    // Health and Toxicity
    @NonNull public final String healthHumansClassification;
    @NonNull public final String healthHumansText;
    @NonNull public final String healthCatsClassification;
    @NonNull public final String healthCatsText;
    @NonNull public final String healthTortoisesClassification;
    @NonNull public final String healthTortoisesText;

    // Care
    @NonNull public final String carePlacement;
    @NonNull public final String careCutting;
    @NonNull public final String careNutrients;

    public PlantDetailInfo(
            @NonNull String scientificName,
            @NonNull String plantFamily,
            @NonNull String dateAdded,
            @NonNull String sunExposure,
            @NonNull String wateringFrequency,
            @NonNull String soilType,
            boolean isIndoor,
            @NonNull String notes,
            @NonNull String description,
            @NonNull String healthHumansClassification,
            @NonNull String healthHumansText,
            @NonNull String healthCatsClassification,
            @NonNull String healthCatsText,
            @NonNull String healthTortoisesClassification,
            @NonNull String healthTortoisesText,
            @NonNull String carePlacement,
            @NonNull String careCutting,
            @NonNull String careNutrients) {
        this.scientificName = scientificName;
        this.plantFamily = plantFamily;
        this.dateAdded = dateAdded;
        this.sunExposure = sunExposure;
        this.wateringFrequency = wateringFrequency;
        this.soilType = soilType;
        this.isIndoor = isIndoor;
        this.notes = notes;
        this.description = description;
        this.healthHumansClassification = healthHumansClassification;
        this.healthHumansText = healthHumansText;
        this.healthCatsClassification = healthCatsClassification;
        this.healthCatsText = healthCatsText;
        this.healthTortoisesClassification = healthTortoisesClassification;
        this.healthTortoisesText = healthTortoisesText;
        this.carePlacement = carePlacement;
        this.careCutting = careCutting;
        this.careNutrients = careNutrients;
    }
}
