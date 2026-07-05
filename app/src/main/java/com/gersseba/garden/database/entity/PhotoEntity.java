package com.gersseba.garden.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(
        tableName = "photos",
        foreignKeys = @ForeignKey(
                entity = PlantEntity.class,
                parentColumns = "id",
                childColumns = "plant_id",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("plant_id")})
public class PhotoEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "plant_id")
    public long plantId;

    @NonNull
    @ColumnInfo(name = "photo_path")
    public String photoPath;

    @ColumnInfo(name = "image_res_id")
    public int imageResId;

    @NonNull
    @ColumnInfo(name = "captured_at")
    public LocalDateTime capturedAt;

    @NonNull
    @ColumnInfo(name = "health_analysis")
    public String healthAnalysis;

    public PhotoEntity(
            long plantId,
            @NonNull String photoPath,
            int imageResId,
            @NonNull LocalDateTime capturedAt,
            @NonNull String healthAnalysis) {
        this.plantId = plantId;
        this.photoPath = photoPath;
        this.imageResId = imageResId;
        this.capturedAt = capturedAt;
        this.healthAnalysis = healthAnalysis;
    }
}
