package com.example.whatscookin.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.whatscookin.Food;
import com.example.whatscookin.FoodAdapter;
import com.example.whatscookin.R;
import com.example.whatscookin.activities.AddFoodActivity;
import com.example.whatscookin.databinding.FragmentHomeBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    public static final String TAG = "HomeFragment";
    public static final int QUERY_LIMIT = 20;
    FragmentHomeBinding binding;
    private RecyclerView rvFridge;
    private FoodAdapter adapter;
    private List<Food> fridge;
    SwipeRefreshLayout swipeLayout;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // temporary code to transition to the add page
        Button btn = binding.btn;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddFoodActivity.class);
                getContext().startActivity(intent);
            }
        });

        rvFridge = binding.rvFridge;

        fridge = new ArrayList<>();
        adapter = new FoodAdapter(getContext(), fridge);
        rvFridge.setAdapter(adapter);
        rvFridge.setLayoutManager(new GridLayoutManager(getContext(), 3));

        swipeLayout = binding.swipeContainer;
        swipeLayout.setOnRefreshListener(this);

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

    @Override
    public void onRefresh() {
        fridge.clear();
        queryFridge();
        swipeLayout.setRefreshing(false);
    }
}