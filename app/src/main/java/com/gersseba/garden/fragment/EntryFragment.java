package com.gersseba.garden.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.gersseba.garden.R;
import com.gersseba.garden.databinding.FragmentEntryBinding;

public class EntryFragment extends Fragment {
    private FragmentEntryBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentEntryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.myPlantsButton.setOnClickListener(buttonView -> NavHostFragment.findNavController(this)
                .navigate(R.id.action_entryFragment_to_myPlantsFragment));
        binding.carePlanButton.setOnClickListener(buttonView -> NavHostFragment.findNavController(this)
                .navigate(R.id.action_entryFragment_to_carePlanFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

