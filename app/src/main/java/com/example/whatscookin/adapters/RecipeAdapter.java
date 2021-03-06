package com.example.whatscookin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatscookin.models.Food;
import com.example.whatscookin.databinding.ItemFoodRecipeBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter that keeps track of the selected ingredients for the recipe query
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private Context context;
    private List<Food> fridge;
    private ItemFoodRecipeBinding binding;
    private List<String> ingredients;

    public RecipeAdapter(Context context, List<Food> fridge) {
        this.context = context;
        this.fridge = fridge;
        ingredients = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemFoodRecipeBinding.inflate(LayoutInflater.from(context), parent, false);
        final View view = binding.getRoot();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Food food = fridge.get(position);
        holder.bind(food);
    }

    @Override
    public int getItemCount() {
        return fridge.size();
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener{

        private CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = binding.checkBox;
        }

        public void bind(Food food) {
            checkBox.setText(food.getName());
            checkBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b){
                ingredients.add(compoundButton.getText().toString());
            } else {
                ingredients.remove(compoundButton.getText().toString());
            }
        }
    }
}