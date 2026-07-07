package com.gersseba.garden.i18n;

import com.gersseba.garden.R;

/**
 * Central mapping of logical i18n keys to Android string resource IDs.
 * Keep keys stable across the app and tests.
 */
public final class ResourceKeyMapper {
    private ResourceKeyMapper() {}

    public static final String KEY_TASK_WATER = "task.water";
    public static final String KEY_TASK_FERTILIZE = "task.fertilize";
    public static final String KEY_PLANT_MONSTERA = "plant.monstera";
    public static final String KEY_MOCK_DESCRIPTION = "mock.description";

    public static int mapKeyToResId(String key) {
        if (key == null) return 0;
        switch (key) {
            case KEY_TASK_WATER:
                return R.string.task_name_water;
            case KEY_TASK_FERTILIZE:
                return R.string.task_name_fertilize;
            case KEY_PLANT_MONSTERA:
                return R.string.plant_name_monstera;
            case KEY_MOCK_DESCRIPTION:
                return R.string.mock_description;
            default:
                return 0;
        }
    }
}
