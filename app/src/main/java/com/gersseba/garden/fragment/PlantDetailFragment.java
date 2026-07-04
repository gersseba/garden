package com.gersseba.garden.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gersseba.garden.databinding.FragmentPlantDetailBinding;

/**
 * Placeholder for the plant detail screen.
 * Full implementation will be provided in ticket #33.
 */
public class PlantDetailFragment extends Fragment {

    private FragmentPlantDetailBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentPlantDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            String plantName = getArguments().getString("plantName", "");
            if (!plantName.isEmpty()) {
                binding.plantDetailName.setText(plantName);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

