package com.example.whatscookin.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Recipe {

    private String title;
    private String image;
    private String url;
    private List<String> ingredientLines = new ArrayList<>();
    private String calories;
    private String servings;

    public Recipe() { }

    // constructor
    public Recipe(String title, String image, String url, List<String> ingredientLines, String calories, String servings) {
        this.title = title;
        this.image = image;
        this.url = url;
        this.ingredientLines = ingredientLines;
        this.calories = calories;
        this.servings = servings;
    }

    // getters
    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getIngredientLines() { return String.join("\n", ingredientLines); }

    public String getCalories() {
        return calories;
    }

    public String getServings() {
        return servings;
    }
}
