package com.gersseba.garden.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gersseba.garden.databinding.ItemPlantCardBinding;
import com.gersseba.garden.model.MockPlant;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView adapter that renders a list of {@link MockPlant} entries as tappable cards.
 */
public class PlantCardAdapter extends RecyclerView.Adapter<PlantCardAdapter.PlantViewHolder> {

    /** Callback invoked when the user taps a plant card. */
    public interface OnPlantClickListener {
        void onPlantClicked(MockPlant plant);
    }

    private List<MockPlant> plants = new ArrayList<>();
    private final OnPlantClickListener clickListener;

    public PlantCardAdapter(@NonNull OnPlantClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /** Replaces the displayed list and notifies the adapter. */
    public void setPlants(@NonNull List<MockPlant> newPlants) {
        plants = new ArrayList<>(newPlants);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlantCardBinding binding = ItemPlantCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new PlantViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        holder.bind(plants.get(position), clickListener);
    }

    @Override
    public int getItemCount() {
        return plants.size();
    }

    static class PlantViewHolder extends RecyclerView.ViewHolder {

        private final ItemPlantCardBinding binding;

        PlantViewHolder(@NonNull ItemPlantCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(@NonNull MockPlant plant, @NonNull OnPlantClickListener listener) {
            binding.plantName.setText(plant.name);
            binding.getRoot().setOnClickListener(v -> listener.onPlantClicked(plant));
        }
    }
}

