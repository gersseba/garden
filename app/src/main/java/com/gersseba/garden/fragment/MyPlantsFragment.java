package com.gersseba.garden.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gersseba.garden.R;
import com.gersseba.garden.adapter.PlantCardAdapter;
import com.gersseba.garden.databinding.FragmentMyPlantsBinding;
import com.gersseba.garden.di.ServiceLocator;
import com.gersseba.garden.di.ViewModelFactory;
import com.gersseba.garden.model.Plant;
import com.gersseba.garden.viewmodel.MyPlantsViewModel;

/**
 * Displays the persisted My Plants list with a floating action button to add plants.
 * Tapping a plant card navigates to the plant detail screen.
 */
public class MyPlantsFragment extends Fragment {

    private FragmentMyPlantsBinding binding;
    private MyPlantsViewModel viewModel;
    private PlantCardAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentMyPlantsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ServiceLocator serviceLocator = ServiceLocator.getInstance(requireContext());
        ViewModelFactory factory = new ViewModelFactory(requireActivity().getApplication(), serviceLocator);
        viewModel = new ViewModelProvider(this, factory).get(MyPlantsViewModel.class);

        setUpRecyclerView();
        setUpFab();
        observePlants();
    }

    private void setUpRecyclerView() {
        adapter = new PlantCardAdapter(this::onPlantClicked);
        binding.plantsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.plantsRecyclerView.setAdapter(adapter);
    }

    private void setUpFab() {
        binding.fabAddPlant.setOnClickListener(v -> viewModel.addRandomPlant());
    }

    private void observePlants() {
        viewModel.getPlants().observe(getViewLifecycleOwner(), plants -> {
            adapter.setPlants(plants);
            binding.emptyMessage.setVisibility(plants.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    private void onPlantClicked(@NonNull Plant plant) {
        Bundle args = new Bundle();
        args.putLong("plantId", plant.id);
        Navigation.findNavController(requireView())
                .navigate(R.id.action_myPlantsFragment_to_plantDetailFragment, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
