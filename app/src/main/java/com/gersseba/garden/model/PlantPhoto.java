package com.gersseba.garden.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

/**
 * Presentation model for a single plant photo shown in the detail screen gallery.
 *
 * Forward-compatible:
 *   - {@code drawableRes} maps to {@code PhotoEntity.photoPath} (replace with a file path or URI).
 *   - {@code aiSummaryRes} maps to {@code PhotoEntity.healthAnalysis} (replace with a String).
 */
public class PlantPhoto {

    /** Placeholder drawable resource; replace with a real photo URI when Room is wired. */
    @DrawableRes
    public final int drawableRes;

    /** Mocked AI-generated summary resource ID; replace with {@code PhotoEntity.healthAnalysis}. */
    @StringRes
    public final int aiSummaryRes;

    public PlantPhoto(@DrawableRes int drawableRes, @StringRes int aiSummaryRes) {
        this.drawableRes = drawableRes;
        this.aiSummaryRes = aiSummaryRes;
    }
}

