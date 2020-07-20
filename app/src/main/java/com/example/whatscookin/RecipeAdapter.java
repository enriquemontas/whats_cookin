package com.example.whatscookin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatscookin.databinding.ItemFoodRecipeBinding;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    Context context;
    List<Food> fridge;
    ItemFoodRecipeBinding binding;

    public RecipeAdapter(Context context, List<Food> fridge) {
        this.context = context;
        this.fridge = fridge;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemFoodRecipeBinding.inflate(LayoutInflater.from(context), parent, false);
        View view = binding.getRoot();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food food = fridge.get(position);
        holder.bind(food);
    }

    @Override
    public int getItemCount() {
        return fridge.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = binding.checkBox;
        }

        public void bind(Food food) {
            checkBox.setText(food.getName());
        }
    }
}