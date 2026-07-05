package com.gersseba.garden.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import java.time.LocalDateTime;

/**
 * Presentation model for a single persisted plant photo shown in the detail screen gallery.
 */
public class PlantPhoto {

    public final long id;

    @DrawableRes
    public final int imageResId;

    @NonNull
    public final String photoPath;

    @NonNull
    public final LocalDateTime capturedAt;

    @NonNull public final String aiSummary;

    public PlantPhoto(
            long id,
            @DrawableRes int imageResId,
            @NonNull String photoPath,
            @NonNull LocalDateTime capturedAt,
            @NonNull String aiSummary) {
        this.id = id;
        this.imageResId = imageResId;
        this.photoPath = photoPath;
        this.capturedAt = capturedAt;
        this.aiSummary = aiSummary;
    }
}
