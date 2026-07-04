package com.gersseba.garden.model;

import androidx.annotation.StringRes;

/**
 * Presentation model for the General Information section of the plant detail screen.
 *
 * Forward-compatible: field names mirror {@code PlantEntity} so the real Room object
 * can replace the mocked resource IDs with minimal rework.
 *   - {@code scientificNameRes}   → {@code PlantEntity.scientificName}
 *   - {@code plantFamilyRes}      → {@code PlantEntity.plantFamily}
 *   - {@code sunExposureRes}      → {@code PlantEntity.sunExposure}
 *   - {@code wateringFrequencyRes}→ {@code PlantEntity.wateringFrequency}
 *   - {@code soilTypeRes}         → {@code PlantEntity.soilType}
 */
public class PlantDetailInfo {

    @StringRes public final int scientificNameRes;
    @StringRes public final int plantFamilyRes;
    @StringRes public final int sunExposureRes;
    @StringRes public final int wateringFrequencyRes;
    @StringRes public final int soilTypeRes;

    public PlantDetailInfo(
            @StringRes int scientificNameRes,
            @StringRes int plantFamilyRes,
            @StringRes int sunExposureRes,
            @StringRes int wateringFrequencyRes,
            @StringRes int soilTypeRes) {
        this.scientificNameRes = scientificNameRes;
        this.plantFamilyRes = plantFamilyRes;
        this.sunExposureRes = sunExposureRes;
        this.wateringFrequencyRes = wateringFrequencyRes;
        this.soilTypeRes = soilTypeRes;
    }
}

