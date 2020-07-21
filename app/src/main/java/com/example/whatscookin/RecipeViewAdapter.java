package com.example.whatscookin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatscookin.databinding.ItemRecipeBinding;

import org.w3c.dom.Text;

import java.util.List;

public class RecipeViewAdapter extends RecyclerView.Adapter<RecipeViewAdapter.ViewHolder> {

    Context context;
    List<Recipe> recipes;
    ItemRecipeBinding binding;

    public RecipeViewAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemRecipeBinding.inflate(LayoutInflater.from(context), parent, false);
        View view = binding.getRoot();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() { return recipes.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivRecipe;
        private TextView tvTitle;
        private TextView tvCalories;
        private TextView tvServings;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivRecipe = binding.ivRecipe;
            tvTitle = binding.tvTitle;
            tvCalories = binding.tvCalories;
            tvServings = binding.tvServings;

        }

        public void bind(Recipe recipe) {
            tvTitle.setText(recipe.getTitle());
            tvCalories.setText(recipe.getCalories());
            tvServings.setText(recipe.getServings());
            Glide.with(context).load(recipe.getImage()).into(ivRecipe);

        }
    }
}
