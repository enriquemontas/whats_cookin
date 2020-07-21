package com.example.whatscookin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.whatscookin.Food;
import com.example.whatscookin.R;
import com.example.whatscookin.Recipe;
import com.example.whatscookin.RecipeViewAdapter;
import com.example.whatscookin.databinding.ActivityRecipeViewBinding;

import org.parceler.Parcels;

import java.util.List;

public class RecipeViewActivity extends AppCompatActivity {
    List<Recipe> recipes;
    ActivityRecipeViewBinding binding;
    RecyclerView rvRecipe;
    RecipeViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeViewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        rvRecipe = binding.rvRecipe;

        recipes = Parcels.unwrap(getIntent().getParcelableExtra(Recipe.class.getSimpleName()));

        adapter = new RecipeViewAdapter(getApplicationContext(), recipes);
        rvRecipe.setAdapter(adapter);
        rvRecipe.setLayoutManager(new LinearLayoutManager(this));

    }
}