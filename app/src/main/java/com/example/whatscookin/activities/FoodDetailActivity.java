package com.example.whatscookin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.whatscookin.Food;
import com.example.whatscookin.R;
import com.example.whatscookin.databinding.ActivityFoodDetailBinding;
import com.example.whatscookin.databinding.PopupConsumeBinding;
import com.parse.ParseUser;

import org.parceler.Parcels;

public class FoodDetailActivity extends AppCompatActivity {

    Food food;

    ImageView ivImage;
    TextView tvName;
    TextView tvCalorieCount;
    TextView tvCurrentQuantity;
    TextView tvQuantityUnits;
    TextView tvBarcode;
    Button btnConsume;
    Button btnEdit;
    Button btnCancel;
    Button btnSubmit;
    EditText etName;
    EditText etCalorieCount;
    EditText etServingSize;
    EditText etBarcode;
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
        tvCurrentQuantity = binding.tvCurrentQuantity;
        tvQuantityUnits = binding.tvQuantityUnits;
        tvBarcode = binding.tvBarcode;
        btnConsume = binding.btnConsume;
        btnEdit = binding.btnEdit;
        btnCancel = binding.btnCancel;
        btnSubmit = binding.btnSubmit;
        etName = binding.etName;
        etCalorieCount = binding.etCalorieCount;
        etServingSize = binding.etServingSize;
        etBarcode = binding.etBarcode;

        food = Parcels.unwrap(getIntent().getParcelableExtra(Food.class.getSimpleName()));

        tvName.setText(food.getName());
        tvCalorieCount.setText(String.valueOf(food.getCalories()) + " per " + food.getServingSize());
        tvCurrentQuantity.setText(String.valueOf(food.getCurrentQuantity()));
        tvQuantityUnits.setText(food.getQuantityUnit());
        tvBarcode.setText(String.valueOf(food.getBarcode()));
        Context context = getApplicationContext();
        Glide.with(context).load(food.getImage().getUrl()).into(ivImage);

        btnConsume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consumePopup();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvName.setVisibility(View.GONE);
                tvCalorieCount.setVisibility(View.GONE);
                tvBarcode.setVisibility(View.GONE);
                btnEdit.setVisibility(View.GONE);
                btnCancel.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.VISIBLE);
                etName.setVisibility(View.VISIBLE);
                etCalorieCount.setVisibility(View.VISIBLE);
                etServingSize.setVisibility(View.VISIBLE);
                etBarcode.setVisibility(View.VISIBLE);
                etName.setText(food.getName());
                etCalorieCount.setText(String.valueOf(food.getCalories()));
                etServingSize.setText(food.getServingSize());
                etBarcode.setText(String.valueOf(food.getBarcode()));
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvName.setVisibility(View.VISIBLE);
                tvCalorieCount.setVisibility(View.VISIBLE);
                tvBarcode.setVisibility(View.VISIBLE);
                btnEdit.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.GONE);
                etName.setVisibility(View.GONE);
                etCalorieCount.setVisibility(View.GONE);
                etServingSize.setVisibility(View.GONE);
                etBarcode.setVisibility(View.GONE);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                food.setName(etName.getText().toString());
                food.setCalories(Integer.parseInt(etCalorieCount.getText().toString()));
                food.setServingSize(etServingSize.getText().toString());
                food.setBarcode(Integer.parseInt(etBarcode.getText().toString()));
                tvName.setText(food.getName());
                tvCalorieCount.setText(String.valueOf(food.getCalories()) + " per " + food.getServingSize());
                tvBarcode.setText(String.valueOf(food.getBarcode()));
                tvName.setVisibility(View.VISIBLE);
                tvCalorieCount.setVisibility(View.VISIBLE);
                tvBarcode.setVisibility(View.VISIBLE);
                btnEdit.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.GONE);
                etName.setVisibility(View.GONE);
                etCalorieCount.setVisibility(View.GONE);
                etServingSize.setVisibility(View.GONE);
                etBarcode.setVisibility(View.GONE);

                food.saveEventually();
            }
        });

    }

    private void consumePopup() {
        final PopupWindow popupWindow = new PopupWindow(FoodDetailActivity.this);
        PopupConsumeBinding consumeBinding = PopupConsumeBinding.inflate(getLayoutInflater());
        View view = consumeBinding.getRoot();

        popupWindow.setContentView(view);

        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        TextView tvCaption = consumeBinding.tvCaption;
        final NumberPicker npQuantityConsumed = consumeBinding.npQuantityConsumed;
        Button btnConfirm = consumeBinding.btnConfirm;

        tvCaption.setText("How many servings are you eating? \n One serving: " + food.getServingSize());

        npQuantityConsumed.setMinValue(1);
        npQuantityConsumed.setMaxValue(food.getCurrentQuantity());
        npQuantityConsumed.setWrapSelectorWheel(true);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.getCurrentUser().fetchInBackground();
                final ParseUser user = ParseUser.getCurrentUser();
                int intake = user.getInt("calIntake");
                intake += food.getCalories() * npQuantityConsumed.getValue();
                user.put("calIntake", intake);
                user.saveEventually();
                int remaining = food.getCurrentQuantity() - npQuantityConsumed.getValue();
                food.setCurrentQuantity(remaining);
                tvCurrentQuantity.setText(remaining);
                food.saveEventually();
                popupWindow.dismiss();
            }
        });
    }
}