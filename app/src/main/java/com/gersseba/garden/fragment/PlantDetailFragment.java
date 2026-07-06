package com.gersseba.garden.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gersseba.garden.R;
import com.gersseba.garden.adapter.PhotoGalleryAdapter;
import com.gersseba.garden.adapter.PlantCareTaskAdapter;
import com.gersseba.garden.databinding.FragmentPlantDetailBinding;
import com.gersseba.garden.model.PlantDetailInfo;
import com.gersseba.garden.viewmodel.PlantDetailViewModel;

/**
 * Displays the full plant detail screen: photo gallery with AI summaries,
 * general information card, and a plant-specific care plan list.
 *
 * All data comes from {@link PlantDetailViewModel}; this fragment is
 * responsible only for UI orchestration.
 */
public class PlantDetailFragment extends Fragment {

    private FragmentPlantDetailBinding binding;
    private PlantDetailViewModel viewModel;
    private PhotoGalleryAdapter photoAdapter;
    private PlantCareTaskAdapter careTaskAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentPlantDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(PlantDetailViewModel.class);

        long plantId = 0L;
        if (getArguments() != null) {
            plantId = getArguments().getLong("plantId", 0L);
        }
        viewModel.init(plantId);

        setUpPhotoGallery();
        setUpCareTaskList();
        setUpDeleteAction();
        observeViewModel();
    }

    private void setUpPhotoGallery() {
        photoAdapter = new PhotoGalleryAdapter();
        photoAdapter.setOnPhotoClickListener(position -> {
            Bundle args = new Bundle();
            args.putInt("startPosition", position);
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_plantDetailFragment_to_fullscreenGalleryFragment, args);
        });
        binding.photoGalleryRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.photoGalleryRecyclerView.setAdapter(photoAdapter);
    }

    private void setUpCareTaskList() {
        careTaskAdapter = new PlantCareTaskAdapter();
        binding.careTasksRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext()));
        binding.careTasksRecyclerView.setNestedScrollingEnabled(false);
        binding.careTasksRecyclerView.setAdapter(careTaskAdapter);
    }

    private void setUpDeleteAction() {
        binding.deletePlantButton.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.delete_plant_action)
                .setMessage(R.string.delete_plant_confirmation)
                .setPositiveButton(R.string.delete_plant_positive, (dialog, which) -> viewModel.deletePlant())
                .setNegativeButton(R.string.delete_plant_negative, null)
                .show();
    }

    private void observeViewModel() {
        viewModel.getPlantName().observe(getViewLifecycleOwner(), name ->
                binding.plantDetailName.setText(name));

        viewModel.getPhotos().observe(getViewLifecycleOwner(),
                photos -> photoAdapter.submitList(photos));

        viewModel.getGeneralInfo().observe(getViewLifecycleOwner(), info -> {
            bindGeneralInfo(info);
            // Re-check localized text when general info (including fallback) changes
            String localized = viewModel.getGeneralInfoText().getValue();
            if (localized == null || localized.isEmpty()) {
                binding.generalInfoDescription.setText(info.description);
            }
        });

        // Prefer DB-backed long-form general info when available; fall back to resource/mocked description
        viewModel.getGeneralInfoText().observe(getViewLifecycleOwner(), text -> {
            if (text != null && !text.isEmpty()) {
                binding.generalInfoDescription.setText(text);
            } else {
                PlantDetailInfo info = viewModel.getGeneralInfo().getValue();
                if (info != null) binding.generalInfoDescription.setText(info.description);
            }
        });

        viewModel.getCareTasks().observe(getViewLifecycleOwner(),
                tasks -> careTaskAdapter.submitList(tasks));

        viewModel.getPlantDeleted().observe(getViewLifecycleOwner(), deleted -> {
            if (deleted) {
                NavHostFragment.findNavController(this).popBackStack();
            }
        });
    }

    private void bindGeneralInfo(@NonNull PlantDetailInfo info) {
        binding.generalInfoSpeciesValue.setText(info.scientificName);
        binding.generalInfoFamilyValue.setText(info.plantFamily);
        binding.generalInfoLightValue.setText(info.sunExposure);
        binding.generalInfoWateringValue.setText(info.wateringFrequency);
        binding.generalInfoSoilValue.setText(info.soilType);

        // Bind Health and Toxicity
        binding.healthHumansClassification.setText(info.healthHumansClassification);
        binding.healthHumansText.setText(info.healthHumansText);
        binding.healthCatsClassification.setText(info.healthCatsClassification);
        binding.healthCatsText.setText(info.healthCatsText);
        binding.healthTortoisesClassification.setText(info.healthTortoisesClassification);
        binding.healthTortoisesText.setText(info.healthTortoisesText);

        // Bind Care
        binding.carePlacementText.setText(info.carePlacement);
        binding.careCuttingText.setText(info.careCutting);
        binding.careNutrientsText.setText(info.careNutrients);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
