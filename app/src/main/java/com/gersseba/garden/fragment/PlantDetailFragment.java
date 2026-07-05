package com.gersseba.garden.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    private void observeViewModel() {
        viewModel.getPlantName().observe(getViewLifecycleOwner(), name ->
                binding.plantDetailName.setText(name));

        viewModel.getPhotos().observe(getViewLifecycleOwner(),
                photos -> photoAdapter.submitList(photos));

        viewModel.getGeneralInfo().observe(getViewLifecycleOwner(),
                this::bindGeneralInfo);

        viewModel.getCareTasks().observe(getViewLifecycleOwner(),
                tasks -> careTaskAdapter.submitList(tasks));
    }

    private void bindGeneralInfo(@NonNull PlantDetailInfo info) {
        binding.generalInfoSpeciesValue.setText(info.scientificName);
        binding.generalInfoFamilyValue.setText(info.plantFamily);
        binding.generalInfoLightValue.setText(info.sunExposure);
        binding.generalInfoWateringValue.setText(info.wateringFrequency);
        binding.generalInfoSoilValue.setText(info.soilType);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
