package com.example.whatscookin;

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

    public Recipe(String title, String image, String url, List<String> ingredientLines, String calories, String servings) {
        this.title = title;
        this.image = image;
        this.url = url;
        this.ingredientLines = ingredientLines;
        this.calories = calories;
        this.servings = servings;
    }

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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setIngredientLines(List<String> ingredientLines) {
        this.ingredientLines = ingredientLines;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }
}
