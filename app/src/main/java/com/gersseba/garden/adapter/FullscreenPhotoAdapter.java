package com.gersseba.garden.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gersseba.garden.databinding.ItemFullscreenPhotoBinding;
import com.gersseba.garden.model.PlantPhoto;

import java.util.Collections;
import java.util.List;

public class FullscreenPhotoAdapter extends RecyclerView.Adapter<FullscreenPhotoAdapter.PhotoViewHolder> {

    private List<PlantPhoto> photos = Collections.emptyList();

    public void submitList(@NonNull List<PlantPhoto> newPhotos) {
        photos = newPhotos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFullscreenPhotoBinding binding = ItemFullscreenPhotoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new PhotoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        holder.bind(photos.get(position));
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private final ItemFullscreenPhotoBinding binding;

        PhotoViewHolder(@NonNull ItemFullscreenPhotoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(@NonNull PlantPhoto photo) {
            binding.fullscreenImage.setImageResource(photo.imageResId);
        }
    }
}

