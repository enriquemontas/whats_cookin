package com.example.whatscookin.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatscookin.Food;
import com.example.whatscookin.R;
import com.example.whatscookin.RecipeAdapter;
import com.example.whatscookin.databinding.FragmentCalorieIntakeBinding;
import com.example.whatscookin.databinding.FragmentRecipeLookupBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

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
        fridge = new ArrayList<>();
        adapter = new RecipeAdapter(getContext(), fridge);
        rvFood.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvFood.setLayoutManager(layoutManager);

        queryFridge();
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