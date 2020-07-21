package com.example.whatscookin.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whatscookin.EdamamLookup;
import com.example.whatscookin.Food;
import com.example.whatscookin.R;
import com.example.whatscookin.Recipe;
import com.example.whatscookin.RecipeAdapter;
import com.example.whatscookin.activities.FoodDetailActivity;
import com.example.whatscookin.activities.RecipeViewActivity;
import com.example.whatscookin.databinding.FragmentCalorieIntakeBinding;
import com.example.whatscookin.databinding.FragmentRecipeLookupBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.parse.Parse.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeLookupFragment extends Fragment {

    public static final String TAG = "RecipeLookupFragment";
    public static final int QUERY_LIMIT = 20;
    FragmentRecipeLookupBinding binding;
    RecyclerView rvFood;
    RecipeAdapter adapter;
    List<Food> fridge;
    FloatingActionButton fabSearch;
    List<String> ingredients;
    String queryString;
    List<Recipe> recipes;

    public RecipeLookupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecipeLookupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvFood = binding.rvFood;
        fabSearch = binding.fabSearch;
        fridge = new ArrayList<>();
        recipes = new ArrayList<>();
        adapter = new RecipeAdapter(getContext(), fridge);
        rvFood.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvFood.setLayoutManager(layoutManager);

        queryFridge();

        fabSearch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O) // ?
            @Override
            public void onClick(View view) {
                ingredients = adapter.getIngredients();
                queryString = String.join("%20", ingredients);
//                Log.i(TAG, String.valueOf(ingredients.size()));
                search();
            }
        });

    }

    private void search() {
        final EdamamLookup edamamLookup = new EdamamLookup();
        EdamamLookup.searchRecipes(queryString, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "Error in searching recipe", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                recipes = edamamLookup.listResults(response);
                if (recipes.size() == 0){
                    Looper.prepare(); // android OS thing
                    Toast.makeText(getContext(),"No Recipes Found", Toast.LENGTH_LONG).show();
                } else {
                    Log.i(TAG, String.valueOf(recipes.size()));
                    Intent intent = new Intent(getContext(), RecipeViewActivity.class);
                    intent.putExtra(Recipe.class.getSimpleName(), Parcels.wrap(recipes));
                    getContext().startActivity(intent);
                }

            }
        });

    }

    private void queryFridge() {
        ParseQuery<Food> query = ParseQuery.getQuery(Food.class);
        query.include(Food.KEY_OWNER);
        query.whereEqualTo(Food.KEY_OWNER, ParseUser.getCurrentUser());
        query.setLimit(QUERY_LIMIT);
        query.addAscendingOrder(Food.KEY_NAME);
        query.findInBackground(new FindCallback<Food>() {
            @Override
            public void done(List<Food> foodList, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "something went wrong in the query", e);
                    return;
                }
                for (Food food : foodList){
                    Log.i(TAG, "Food: " + food.getName());
                }
                fridge.addAll(foodList);
                adapter.notifyDataSetChanged();
            }
        });
    }

}