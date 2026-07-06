package com.gersseba.garden.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gersseba.garden.databinding.ItemDetailCareTaskBinding;
import com.gersseba.garden.model.PlantCareTask;

import java.util.Collections;
import java.util.List;

/**
 * RecyclerView adapter that renders the plant-specific care task list
 * on the Plant Detail screen.
 */
public class PlantCareTaskAdapter
        extends RecyclerView.Adapter<PlantCareTaskAdapter.CareTaskViewHolder> {

    private List<PlantCareTask> tasks = Collections.emptyList();

    /** Replaces the current task list and refreshes the RecyclerView. */
    public void submitList(@NonNull List<PlantCareTask> newTasks) {
        tasks = newTasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CareTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDetailCareTaskBinding binding = ItemDetailCareTaskBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CareTaskViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CareTaskViewHolder holder, int position) {
        holder.bind(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class CareTaskViewHolder extends RecyclerView.ViewHolder {

        private final ItemDetailCareTaskBinding binding;

        CareTaskViewHolder(@NonNull ItemDetailCareTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(@NonNull PlantCareTask task) {
            // Keep short labels resource-backed; descriptions may be long and could be localized via DB in future.
            binding.detailCareTaskName.setText(task.taskTypeRes);
            binding.detailCareTaskDescription.setText(task.descriptionRes);
        }
    }
}
