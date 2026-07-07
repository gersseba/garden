package com.gersseba.garden.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gersseba.garden.adapter.CarePlanAdapter;
import com.gersseba.garden.databinding.FragmentCarePlanBinding;
import com.gersseba.garden.di.ServiceLocator;
import com.gersseba.garden.di.ViewModelFactory;
import com.gersseba.garden.viewmodel.CarePlanViewModel;

public class CarePlanFragment extends Fragment {

    private FragmentCarePlanBinding binding;
    private CarePlanAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentCarePlanBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new CarePlanAdapter();
        binding.careTaskList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.careTaskList.setAdapter(adapter);

        ServiceLocator serviceLocator = ServiceLocator.getInstance(requireContext());
        ViewModelFactory factory = new ViewModelFactory(requireActivity().getApplication(), serviceLocator);
        CarePlanViewModel viewModel = new ViewModelProvider(this, factory).get(CarePlanViewModel.class);

        viewModel.getTasks().observe(getViewLifecycleOwner(), adapter::submitList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
