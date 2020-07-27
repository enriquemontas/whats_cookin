package com.example.whatscookin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.whatscookin.databinding.PopupNumberBoxBinding;
import com.example.whatscookin.databinding.PopupTextBoxBinding;
import com.example.whatscookin.models.Food;
import com.example.whatscookin.databinding.ActivityFoodDetailBinding;
import com.example.whatscookin.databinding.PopupConsumeBinding;
import com.parse.ParseUser;

import org.parceler.Parcels;

/**
 * This activity gives a detailed view of a user's selected food item
 */
public class FoodDetailActivity extends AppCompatActivity {

    private Food food;

    private TextView tvName;
    private TextView tvCalorieCount;
    private TextView tvCurrentQuantity;
    private TextView tvQuantityUnits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityFoodDetailBinding binding = ActivityFoodDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        final ImageView ivImage = binding.ivImage;
        tvName = binding.tvName;
        tvCalorieCount = binding.tvCalorieCount;
        tvCurrentQuantity = binding.tvCurrentQuantity;
        tvQuantityUnits = binding.tvQuantityUnits;
        final TextView tvBarcode = binding.tvBarcode;
        final Button btnConsume = binding.btnConsume;

        food = Parcels.unwrap(getIntent().getParcelableExtra(Food.class.getSimpleName()));

        tvName.setText(food.getName());
        tvCalorieCount.setText(String.valueOf(food.getCalories()) + " per " + food.getServingSize());
        tvCurrentQuantity.setText(String.valueOf(food.getCurrentQuantity()));
        tvQuantityUnits.setText(food.getQuantityUnit());
        tvBarcode.setText(String.valueOf(food.getBarcode()));
        Context context = getApplicationContext();
        Glide.with(context).load(food.getImage().getUrl()).into(ivImage);

        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String prompt = "What food is this?";
                textPopup(prompt, Food.KEY_NAME, food.getName());
                tvName.setText(food.getName());
            }
        });

        tvCalorieCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String prompt = "How many calories per " + food.getServingSize() + "?";
                numberPopup(prompt, Food.KEY_CALORIES, String.valueOf(food.getCalories()));
                tvCalorieCount.setText(String.valueOf(food.getCalories()) + " per " + food.getServingSize());
            }
        });

        tvCurrentQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String prompt = "How many servings of " + food.getName() + " do you have left?";
                numberPopup(prompt, Food.KEY_CURRENT_QUANTITY, String.valueOf(food.getCurrentQuantity()));
                tvCurrentQuantity.setText(String.valueOf(food.getCurrentQuantity()));
            }
        });

        tvQuantityUnits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String prompt = "What unit do you want to track quantity with?";
                textPopup(prompt, Food.KEY_QUANTITY_UNIT, food.getQuantityUnit());
                tvQuantityUnits.setText(food.getQuantityUnit());
            }
        });

        // popup to launch barcode scanner?

        btnConsume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consumePopup();
            }
        });

    }

    /**
     * Use to launch a popup that takes numeric inputs allowing the user to update a field
     * of the Food object they are viewing
     * @param prompt String to display to the user on the popup
     * @param field Field of food object being updated
     * @param originalValue what the field currently holds
     */
    private void numberPopup(String prompt, final String field, String originalValue) {
        final PopupWindow popupWindow = new PopupWindow(FoodDetailActivity.this);
        final PopupNumberBoxBinding numberBinding = PopupNumberBoxBinding.inflate(getLayoutInflater());
        final View view = numberBinding.getRoot();

        popupWindow.setContentView(view);

        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        final TextView tvCaption = numberBinding.tvCaption;
        final EditText etNumber = numberBinding.etNumber;
        final Button btnConfirm = numberBinding.btnConfirm;

        tvCaption.setText(prompt);
        etNumber.setText(originalValue);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                food.put(field, Integer.parseInt(etNumber.getText().toString()));
                food.saveInBackground();
                popupWindow.dismiss();
            }
        });
    }

    /**
     * Use to launch a popup that takes a text input allowing the user to update a field
     * of the Food object they are viewing
     * @param prompt String to display to the user on the popup
     * @param field Field of food object being updated
     * @param originalValue what the field currently holds
     */
    private void textPopup(String prompt, final String field, String originalValue) {
        final PopupWindow popupWindow = new PopupWindow(FoodDetailActivity.this);
        final PopupTextBoxBinding textBinding = PopupTextBoxBinding.inflate(getLayoutInflater());
        final View view = textBinding.getRoot();

        popupWindow.setContentView(view);

        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        final TextView tvCaption = textBinding.tvCaption;
        final EditText etText = textBinding.etText;
        final Button btnConfirm = textBinding.btnConfirm;

        tvCaption.setText(prompt);
        etText.setText(food.getName());

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                food.put(field, etText.getText().toString());
                food.saveInBackground();
                popupWindow.dismiss();
            }
        });
    }

    /**
     * popup allowing the user to consume between one and total quantity of the viewed Food object
     * the caloric information of the user is then updated
     */
    private void consumePopup() {
        final PopupWindow popupWindow = new PopupWindow(FoodDetailActivity.this);
        final PopupConsumeBinding consumeBinding = PopupConsumeBinding.inflate(getLayoutInflater());
        final View view = consumeBinding.getRoot();

        popupWindow.setContentView(view);

        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        final TextView tvCaption = consumeBinding.tvCaption;
        final NumberPicker npQuantityConsumed = consumeBinding.npQuantityConsumed;
        final Button btnConfirm = consumeBinding.btnConfirm;

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
                final int remaining = food.getCurrentQuantity() - npQuantityConsumed.getValue();
                food.setCurrentQuantity(remaining);
                tvCurrentQuantity.setText(String.valueOf(remaining));
                food.saveEventually();
                popupWindow.dismiss();
            }
        });
    }
}