package com.example.whatscookin.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.whatscookin.OnSwipeTouchListener;
import com.example.whatscookin.activities.FoodDetailActivity;
import com.example.whatscookin.adapters.TagAdapter;
import com.example.whatscookin.databinding.PopupTagsBinding;
import com.example.whatscookin.extenalresources.ParseApplication;
import com.example.whatscookin.models.Food;
import com.example.whatscookin.adapters.FoodAdapter;
import com.example.whatscookin.R;
import com.example.whatscookin.activities.AddFoodActivity;
import com.example.whatscookin.databinding.FragmentHomeBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    public static final String TAG = "HomeFragment";
    public static final int QUERY_LIMIT = 20;
    private FragmentHomeBinding binding;
    private FoodAdapter adapter;
    private List<Food> fridge;
    private SwipeRefreshLayout swipeLayout;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_search, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String s) {

                //perform query here
                try {
                    searchFridge(s);
                } catch (JSONException e) {
                    queryFridge();
                }

                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        searchItem.expandActionView();
        searchView.requestFocus();

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_search:
                // Handle this selection
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * filter the visible food by its name and tag
     * @param s substring to be searched
     */
    private void searchFridge(String s) throws JSONException {
        final List<Food> fullFridge = new ArrayList<>();
        fullFridge.addAll(fridge);
        fridge.clear();

        for (Food f : fullFridge) {
            if (f.getName().toLowerCase().contains(s.toLowerCase())) {
                fridge.add(f);
                continue; // the continue ensures no repeats for matching on both name and tag
            }
            if (f.getTags() != null) {
                JSONArray tags = f.getTags();
                for (int i = 0; i < tags.length(); i++) {
                    String tag = tags.getString(i);
                    if (tag.toLowerCase().contains(s.toLowerCase())){
                        fridge.add(f);
                        break;
                    }
                }
            }
        }
        ParseObject.pinAllInBackground(fridge);
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        final View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // temporary code to transition to the add page
        final Button btn = binding.btn;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getContext(), AddFoodActivity.class);
                getContext().startActivity(intent);
            }
        });

        final RecyclerView rvFridge = binding.rvFridge;

        fridge = new ArrayList<>();
        adapter = new FoodAdapter(getContext(), fridge);
        rvFridge.setAdapter(adapter);
        rvFridge.setLayoutManager(new GridLayoutManager(getContext(), 3));

        swipeLayout = binding.swipeContainer;
        swipeLayout.setOnRefreshListener(this);

        rvFridge.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                Toast.makeText(getContext(), "Swipe Right gesture detected", Toast.LENGTH_SHORT).show();
                try {
                    tagPopup();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        queryFridge();
    }

    /**
     *  popup a recycler view of all the tags for a given item, allowing the user to add and edit
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void tagPopup() throws JSONException {
        final PopupWindow popupWindow = new PopupWindow(getContext());
        final PopupTagsBinding tagsBinding = PopupTagsBinding.inflate(getLayoutInflater());
        final View view = tagsBinding.getRoot();

        popupWindow.setContentView(view);

        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        final JSONArray tags = new JSONArray();
        final Set<String> seen = new HashSet<>();

        for (Food food : fridge) {
            if (food.getTags() == null){
                // here because I didn't initialize parse column with default variable
                // can remake column to delete this check
                continue;
            }
            JSONArray foodTags = food.getTags();
            for (int i = 0; i < foodTags.length(); i++) {
                String tag = foodTags.getString(i);
                if (seen.contains(tag)){
                    continue;
                } else {
                    seen.add(tag.toLowerCase()); // make lowercase so "Tag" and "tag" are equal
                    tags.put(tag);
                }
            }
        }

        Log.i(TAG, tags.toString());

        // ToDo: add an animation for this popup window to follow the swipe
        TagAdapter tagAdapter = new TagAdapter(getContext(), tags);
        final RecyclerView rvTags = tagsBinding.rvTags;
        rvTags.setAdapter(tagAdapter);
        rvTags.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    /**
     * get all the current user's Food objects
     */
    private void queryFridge() {
        final ParseQuery<Food> query = ParseQuery.getQuery(Food.class);
        query.include(Food.KEY_OWNER);
        query.whereEqualTo(Food.KEY_OWNER, ParseUser.getCurrentUser());
        query.setLimit(QUERY_LIMIT);
        query.addAscendingOrder(Food.KEY_NAME);

        if (ParseApplication.isOffline()){
            query.fromLocalDatastore();
        }

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
                ParseObject.pinAllInBackground(fridge);
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