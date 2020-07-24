package com.example.whatscookin.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.whatscookin.models.Recipe;
import com.example.whatscookin.databinding.ActivityRecipeDetailBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.parceler.Parcels;

/**
 * Detailed view of a recipe
 */
public class RecipeDetailActivity extends AppCompatActivity {

    private ActivityRecipeDetailBinding binding;
    private Recipe recipe;
    private ImageView ivImage;
    private TextView tvName;
    private TextView tvServing;
    private TextView tvCalories;
    private TextView tvIngredients;
    private FloatingActionButton fabView;
    private WebView webView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recipe = Parcels.unwrap(getIntent().getParcelableExtra(Recipe.class.getSimpleName()));

        binding = ActivityRecipeDetailBinding.inflate(getLayoutInflater());
        final View view = binding.getRoot();
        setContentView(view);

        ivImage = binding.ivImage;
        tvName = binding.tvName;
        tvServing = binding.tvServing;
        tvCalories = binding.tvCalories;
        tvIngredients = binding.tvIngredient;
        fabView = binding.fabView;
        webView = binding.webView;

        tvName.setText(recipe.getTitle());
        tvServing.setText(recipe.getServings());
        tvCalories.setText(recipe.getCalories());
        tvIngredients.setText(recipe.getIngredientLines());
        Glide.with(getApplicationContext()).load(recipe.getImage()).into(ivImage);

        fabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl(recipe.getUrl());
            }
        });
    }
}