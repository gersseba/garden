package com.gersseba.garden.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gersseba.garden.databinding.ItemCareTaskBinding;
import com.gersseba.garden.model.CurrentCareTask;

import java.util.Collections;
import java.util.List;

/**
 * RecyclerView adapter for the Care Plan task list.
 * Binds each {@link CurrentCareTask} to an item_care_task layout via ViewBinding.
 */
public class CarePlanAdapter extends RecyclerView.Adapter<CarePlanAdapter.CareTaskViewHolder> {

    private List<CurrentCareTask> tasks = Collections.emptyList();

    public void submitList(List<CurrentCareTask> newTasks) {
        tasks = newTasks != null ? newTasks : Collections.emptyList();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CareTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCareTaskBinding binding = ItemCareTaskBinding.inflate(
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

        private final ItemCareTaskBinding binding;

        CareTaskViewHolder(@NonNull ItemCareTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(CurrentCareTask task) {
            binding.careTaskName.setText(task.taskNameRes);
            binding.careTaskPlantName.setText(task.plantNameRes);
            binding.careTaskDescription.setText(task.descriptionRes);
        }
    }
}

