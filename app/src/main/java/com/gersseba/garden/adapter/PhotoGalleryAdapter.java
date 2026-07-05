package com.gersseba.garden.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gersseba.garden.databinding.ItemPhotoCardBinding;
import com.gersseba.garden.model.PlantPhoto;

import java.util.Collections;
import java.util.List;

/**
 * RecyclerView adapter that renders the horizontal photo gallery on the Plant Detail screen.
 */
public class PhotoGalleryAdapter extends RecyclerView.Adapter<PhotoGalleryAdapter.PhotoViewHolder> {

    private List<PlantPhoto> photos = Collections.emptyList();
    private OnPhotoClickListener onPhotoClickListener;

    public interface OnPhotoClickListener {
        void onPhotoClick(int position);
    }

    public void setOnPhotoClickListener(OnPhotoClickListener listener) {
        this.onPhotoClickListener = listener;
    }

    /** Replaces the current photo list and refreshes the RecyclerView. */
    public void submitList(@NonNull List<PlantPhoto> newPhotos) {
        photos = newPhotos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPhotoCardBinding binding = ItemPhotoCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new PhotoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        holder.bind(photos.get(position), position, onPhotoClickListener);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {

        private final ItemPhotoCardBinding binding;

        PhotoViewHolder(@NonNull ItemPhotoCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(@NonNull PlantPhoto photo, int position, OnPhotoClickListener listener) {
            binding.photoImage.setImageResource(photo.imageResId);
            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPhotoClick(position);
                }
            });
        }
    }
}

