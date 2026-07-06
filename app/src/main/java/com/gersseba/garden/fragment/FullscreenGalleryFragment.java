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
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.gersseba.garden.R;
import com.gersseba.garden.adapter.FullscreenPhotoAdapter;
import com.gersseba.garden.databinding.FragmentFullscreenGalleryBinding;
import com.gersseba.garden.model.PlantPhoto;
import com.gersseba.garden.viewmodel.PlantDetailViewModel;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class FullscreenGalleryFragment extends Fragment {

    private FragmentFullscreenGalleryBinding binding;
    private PlantDetailViewModel viewModel;
    private FullscreenPhotoAdapter adapter;
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MMMM d, yyyy, h:mm a");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentFullscreenGalleryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(PlantDetailViewModel.class);

        int startPosition = 0;
        if (getArguments() != null) {
            startPosition = getArguments().getInt("startPosition", 0);
        }

        setUpViewPager(startPosition);
        setUpButtons();
        observeViewModel();
    }

    private void setUpViewPager(int startPosition) {
        adapter = new FullscreenPhotoAdapter();
        binding.fullscreenViewPager.setAdapter(adapter);
        binding.fullscreenViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updatePhotoInfo(position);
            }
        });

        // Use post to ensure ViewPager is laid out before setting current items
        binding.fullscreenViewPager.post(() -> {
            binding.fullscreenViewPager.setCurrentItem(startPosition, false);
            updatePhotoInfo(startPosition);
        });
    }

    private void setUpButtons() {
        binding.fullscreenBackButton.setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());

        binding.fullscreenDeleteButton.setOnClickListener(v -> showDeleteConfirmation());
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.fullscreen_gallery_delete_content_description)
                .setMessage(R.string.delete_photo_confirmation)
                .setPositiveButton(R.string.delete_photo_positive, (dialog, which) -> {
                    int currentPos = binding.fullscreenViewPager.getCurrentItem();
                    viewModel.deletePhotoAt(currentPos);
                })
                .setNegativeButton(R.string.delete_photo_negative, null)
                .show();
    }

    private void observeViewModel() {
        viewModel.getPhotos().observe(getViewLifecycleOwner(), photos -> {
            if (photos.isEmpty()) {
                Navigation.findNavController(requireView()).navigateUp();
                return;
            }
            adapter.submitList(photos);
            updatePhotoInfo(binding.fullscreenViewPager.getCurrentItem());
        });
    }

    private void updatePhotoInfo(int position) {
        List<PlantPhoto> photos = viewModel.getPhotos().getValue();
        if (photos == null || position < 0 || position >= photos.size()) {
            return;
        }

        PlantPhoto photo = photos.get(position);
        binding.fullscreenDateText.setText(photo.capturedAt.format(DATE_FORMATTER));

        // Prefer DB localized ai summary when present
        viewModel.getPhotoSummaryLive(photo.id).observe(getViewLifecycleOwner(), dbSummary -> {
            String summary = dbSummary != null && !dbSummary.isEmpty() ? dbSummary : photo.aiSummary;
            if (summary != null && !summary.isEmpty()) {
                binding.fullscreenSummaryText.setVisibility(View.VISIBLE);
                binding.fullscreenSummaryText.setText(summary);
            } else {
                binding.fullscreenSummaryText.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

