package com.gersseba.garden.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.gersseba.garden.model.Plant;
import com.gersseba.garden.model.PlantPhoto;

import java.util.List;

public interface PlantRepositoryContract {

    LiveData<List<Plant>> observePlants();

    LiveData<Plant> observePlant(long plantId);

    LiveData<List<PlantPhoto>> observePhotosForPlant(long plantId);

    void addPlant(@NonNull String commonName,
            @NonNull String scientificName,
            @NonNull String plantFamily,
            @NonNull String sunExposure,
            @NonNull String wateringFrequency,
            @NonNull String soilType,
            boolean isIndoor,
            @NonNull String notes,
            int[] photoDrawableIds,
            @NonNull String[] photoSummaries);

    void deletePhoto(long photoId);

    void deletePlant(long plantId);
}
