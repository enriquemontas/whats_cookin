package com.example.whatscookin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatscookin.models.Recipe;
import com.example.whatscookin.activities.RecipeDetailActivity;
import com.example.whatscookin.databinding.ItemRecipeBinding;

import org.parceler.Parcels;

import java.util.List;

/**
 * Display recipes retrieved from query in a recycler view
 */
public class RecipeViewAdapter extends RecyclerView.Adapter<RecipeViewAdapter.ViewHolder> {

    private Context context;
    private List<Recipe> recipes;
    private ItemRecipeBinding binding;

    public RecipeViewAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemRecipeBinding.inflate(LayoutInflater.from(context), parent, false);
        final View view = binding.getRoot();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Recipe recipe = recipes.get(position);
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() { return recipes.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
            itemView.setOnClickListener(this);

        }

        public void bind(Recipe recipe) {
            tvTitle.setText(recipe.getTitle());
            tvCalories.setText(recipe.getCalories());
            tvServings.setText(recipe.getServings());
            Glide.with(context).load(recipe.getImage()).into(ivRecipe);

        }

        @Override
        public void onClick(View view) {
            final int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION){
                Recipe recipe = recipes.get(position);
                Intent intent = new Intent(context.getApplicationContext(), RecipeDetailActivity.class);
                intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Recipe.class.getSimpleName(), Parcels.wrap(recipe));
                context.getApplicationContext().startActivity(intent);

            }
        }
    }
}
