package com.gersseba.garden.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "plants")
public class PlantEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    @ColumnInfo(name = "common_name")
    public String commonName;

    @NonNull
    @ColumnInfo(name = "scientific_name")
    public String scientificName;

    @NonNull
    @ColumnInfo(name = "plant_family")
    public String plantFamily;

    @NonNull
    @ColumnInfo(name = "date_added")
    public LocalDate dateAdded;

    @NonNull
    @ColumnInfo(name = "sun_exposure")
    public String sunExposure;

    @NonNull
    @ColumnInfo(name = "watering_frequency")
    public String wateringFrequency;

    @NonNull
    @ColumnInfo(name = "soil_type")
    public String soilType;

    @ColumnInfo(name = "is_indoor")
    public boolean isIndoor;

    @NonNull
    @ColumnInfo(name = "notes")
    public String notes;

    public PlantEntity(
            @NonNull String commonName,
            @NonNull String scientificName,
            @NonNull String plantFamily,
            @NonNull LocalDate dateAdded,
            @NonNull String sunExposure,
            @NonNull String wateringFrequency,
            @NonNull String soilType,
            boolean isIndoor,
            @NonNull String notes) {
        this.commonName = commonName;
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
