package com.example.whatscookin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.whatscookin.Food;
import com.example.whatscookin.R;
import com.example.whatscookin.databinding.ActivityFoodDetailBinding;

import org.parceler.Parcels;

public class FoodDetailActivity extends AppCompatActivity {

    Food food;

    ImageView ivImage;
    TextView tvName;
    TextView tvCalorieCount;
    TextView tvBarcode;
    ActivityFoodDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFoodDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ivImage = binding.ivImage;
        tvName = binding.tvName;
        tvCalorieCount = binding.tvCalorieCount;
        tvBarcode = binding.tvBarcode;

        food = Parcels.unwrap(getIntent().getParcelableExtra(Food.class.getSimpleName()));

        tvName.setText(food.getName());
        if (food.getServingSize() == null) {
            tvCalorieCount.setVisibility(View.GONE);
            // TODO: Add a button to add missing info
        } else {
            tvCalorieCount.setText(food.getCalories() + " per " + food.getServingSize());
        }
//        tvBarcode.setText(food.getBarcode());

        Context context = getApplicationContext();
        Glide.with(context).load(food.getImage().getUrl()).into(ivImage);


    }
}