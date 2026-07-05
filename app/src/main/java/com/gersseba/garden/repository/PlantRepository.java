package com.gersseba.garden.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.gersseba.garden.database.AppDatabase;
import com.gersseba.garden.database.dao.PhotoDao;
import com.gersseba.garden.database.dao.PlantDao;
import com.gersseba.garden.database.entity.PhotoEntity;
import com.gersseba.garden.database.entity.PlantEntity;
import com.gersseba.garden.model.Plant;
import com.gersseba.garden.model.PlantPhoto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Repository boundary for plant and photo persistence state.
 */
public class PlantRepository implements PlantRepositoryContract {

    private final PlantDao plantDao;
    private final PhotoDao photoDao;
    private final Executor writeExecutor;

    public PlantRepository(@NonNull Application application) {
        this(AppDatabase.getInstance(application), Executors.newSingleThreadExecutor());
    }

    PlantRepository(@NonNull AppDatabase appDatabase, @NonNull Executor writeExecutor) {
        this.plantDao = appDatabase.plantDao();
        this.photoDao = appDatabase.photoDao();
        this.writeExecutor = writeExecutor;
    }

    @Override
    public LiveData<List<Plant>> observePlants() {
        return Transformations.map(plantDao.observeAllPlants(), entities -> {
            List<Plant> mapped = new ArrayList<>();
            if (entities == null) {
                return mapped;
            }
            for (PlantEntity entity : entities) {
                mapped.add(mapPlant(entity));
            }
            return mapped;
        });
    }

    @Override
    public LiveData<Plant> observePlant(long plantId) {
        return Transformations.map(plantDao.observePlant(plantId), entity -> {
            if (entity == null) {
                return null;
            }
            return mapPlant(entity);
        });
    }

    @Override
    public LiveData<List<PlantPhoto>> observePhotosForPlant(long plantId) {
        return Transformations.map(photoDao.observePhotosForPlant(plantId), entities -> {
            List<PlantPhoto> mapped = new ArrayList<>();
            if (entities == null) {
                return mapped;
            }
            for (PhotoEntity entity : entities) {
                mapped.add(new PlantPhoto(entity.imageResId, entity.photoPath, entity.capturedAt, entity.healthAnalysis));
            }
            return mapped;
        });
    }

    @Override
    public void addPlant(@NonNull String commonName,
            @NonNull String scientificName,
            @NonNull String plantFamily,
            @NonNull String sunExposure,
            @NonNull String wateringFrequency,
            @NonNull String soilType,
            boolean isIndoor,
            @NonNull String notes,
            int[] photoDrawableIds,
            @NonNull String[] photoSummaries) {
        writeExecutor.execute(() -> {
            PlantEntity plant = new PlantEntity(
                    commonName,
                    scientificName,
                    plantFamily,
                    LocalDate.now(),
                    sunExposure,
                    wateringFrequency,
                    soilType,
                    isIndoor,
                    notes);
            long plantId = plantDao.insert(plant);

            List<PhotoEntity> photos = new ArrayList<>();
            int photoCount = Math.min(photoDrawableIds.length, photoSummaries.length);
            for (int i = 0; i < photoCount; i++) {
                photos.add(new PhotoEntity(plantId, "", photoDrawableIds[i], LocalDateTime.now(), photoSummaries[i]));
            }
            if (!photos.isEmpty()) {
                photoDao.insertAll(photos);
            }
        });
    }

    @NonNull
    private Plant mapPlant(@NonNull PlantEntity entity) {
        return new Plant(
                entity.id,
                entity.commonName,
                entity.scientificName,
                entity.plantFamily,
                entity.dateAdded,
                entity.sunExposure,
                entity.wateringFrequency,
                entity.soilType,
                entity.isIndoor,
                entity.notes);
    }
}
