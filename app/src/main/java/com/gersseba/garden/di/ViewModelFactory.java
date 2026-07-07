package com.gersseba.garden.di;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.gersseba.garden.R;
import com.gersseba.garden.repository.LocalizedTextRepository;
import com.gersseba.garden.repository.PlantRepositoryContract;
import com.gersseba.garden.viewmodel.CarePlanViewModel;
import com.gersseba.garden.viewmodel.MyPlantsViewModel;
import com.gersseba.garden.viewmodel.PlantDetailViewModel;

import java.util.List;
import java.util.Random;

/**
 * ViewModelProvider.Factory that creates ViewModels with dependencies injected via ServiceLocator.
 *
 * Eliminates the need for manual instantiation in ViewModels and removes try-catch blocks.
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final ServiceLocator serviceLocator;

    public ViewModelFactory(@NonNull Application application, @NonNull ServiceLocator serviceLocator) {
        this.application = application;
        this.serviceLocator = serviceLocator;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MyPlantsViewModel.class)) {
            return createMyPlantsViewModel();
        } else if (modelClass.isAssignableFrom(PlantDetailViewModel.class)) {
            return createPlantDetailViewModel();
        } else if (modelClass.isAssignableFrom(CarePlanViewModel.class)) {
            return createCarePlanViewModel();
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends ViewModel> T createMyPlantsViewModel() {
        PlantRepositoryContract repository = serviceLocator.getPlantRepository();
        List<String> plantCatalog = List.of(application.getResources().getStringArray(R.array.my_plants_catalog_entries));
        Random random = new Random();
        String defaultPlantFamily = application.getString(R.string.default_plant_family);
        String defaultSunExposure = application.getString(R.string.default_sun_exposure);
        String defaultWateringFrequency = application.getString(R.string.default_watering_frequency);
        String defaultSoilType = application.getString(R.string.default_soil_type);
        boolean defaultIsIndoor = true;
        String defaultNotes = application.getString(R.string.default_plant_notes);
        int[] defaultPhotoDrawables = new int[] {
                R.drawable.plant_placeholder,
                R.drawable.plant_placeholder_b,
                R.drawable.plant_placeholder_c
        };
        String[] defaultPhotoSummaries = new String[] {
                application.getString(R.string.mock_photo_summary_1),
                application.getString(R.string.mock_photo_summary_2),
                application.getString(R.string.mock_photo_summary_3)
        };

        return (T) new MyPlantsViewModel(
                application,
                repository,
                plantCatalog,
                random,
                defaultPlantFamily,
                defaultSunExposure,
                defaultWateringFrequency,
                defaultSoilType,
                defaultIsIndoor,
                defaultNotes,
                defaultPhotoDrawables,
                defaultPhotoSummaries);
    }

    @SuppressWarnings("unchecked")
    private <T extends ViewModel> T createPlantDetailViewModel() {
        PlantRepositoryContract repository = serviceLocator.getPlantRepository();
        LocalizedTextRepository localizedTextRepository = serviceLocator.getLocalizedTextRepository();
        com.gersseba.garden.i18n.LocaleManager localeManager = serviceLocator.getLocaleManager();

        return (T) new PlantDetailViewModel(
                application,
                repository,
                localizedTextRepository,
                localeManager);
    }

    @SuppressWarnings("unchecked")
    private <T extends ViewModel> T createCarePlanViewModel() {
        return (T) new CarePlanViewModel();
    }
}

