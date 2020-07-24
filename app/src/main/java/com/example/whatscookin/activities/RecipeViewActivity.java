package com.example.whatscookin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.whatscookin.models.Recipe;
import com.example.whatscookin.adapters.RecipeViewAdapter;
import com.example.whatscookin.databinding.ActivityRecipeViewBinding;

import org.parceler.Parcels;

import java.util.List;

/**
 *  initialize RecipeViewAdapter and assigns it to a recycler view to display recipe query results
 */
public class RecipeViewActivity extends AppCompatActivity {
    private List<Recipe> recipes;
    private ActivityRecipeViewBinding binding;
    private RecyclerView rvRecipe;
    private RecipeViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeViewBinding.inflate(getLayoutInflater());
        final View view = binding.getRoot();
        setContentView(view);

        rvRecipe = binding.rvRecipe;
        recipes = Parcels.unwrap(getIntent().getParcelableExtra(Recipe.class.getSimpleName()));
        adapter = new RecipeViewAdapter(getApplicationContext(), recipes);
        rvRecipe.setAdapter(adapter);
        rvRecipe.setLayoutManager(new LinearLayoutManager(this));
    }
}