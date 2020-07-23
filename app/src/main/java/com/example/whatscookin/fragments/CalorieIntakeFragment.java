package com.example.whatscookin.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.whatscookin.R;
import com.example.whatscookin.activities.LoginActivity;
import com.example.whatscookin.databinding.FragmentCalorieIntakeBinding;
import com.example.whatscookin.databinding.PopupCalorieGoalBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import static com.parse.Parse.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class CalorieIntakeFragment extends Fragment {

    public static final String TAG = "CalorieIntakeFragment";
    FragmentCalorieIntakeBinding binding;
    TextView tvTitle;
    TextView tvCalorieDisplay;
    TextView tvCalorieGoal;
    Button btnLogout;


    public CalorieIntakeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCalorieIntakeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTitle = binding.tvTitle;
        tvCalorieDisplay = binding.tvCalorieDisplay;
        tvCalorieGoal = binding.tvCalorieGoal;
        btnLogout = binding.btnLogout;

        Log.i(TAG, ParseUser.getCurrentUser().getUsername());

        ParseUser.getCurrentUser().fetchInBackground();
        final ParseUser user = ParseUser.getCurrentUser();

        if (String.valueOf(user.getInt("calIntake")) == null){
            Log.i(TAG, "Nothing on parse to retrieve");
            user.put("calIntake", 0);
            user.saveEventually();
        }

        tvCalorieDisplay.setText(String.valueOf(user.getInt("calIntake")));
        tvCalorieGoal.setText(tvCalorieGoal.getText().toString() + String.valueOf(user.getInt("calGoal")));
        
        tvCalorieGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goalPopup(user);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.logOut();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });


    }

    private void goalPopup(final ParseUser user) {
        final PopupWindow goalPopup = new PopupWindow(getContext());
        PopupCalorieGoalBinding calorieGoalBinding = PopupCalorieGoalBinding.inflate(getLayoutInflater());
        View view = calorieGoalBinding.getRoot();
        goalPopup.setContentView(view);

        goalPopup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        goalPopup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);

        goalPopup.setOutsideTouchable(true);
        goalPopup.setFocusable(true);

        goalPopup.showAtLocation(view, Gravity.CENTER, 0, 0);

        TextView tvCaption = calorieGoalBinding.tvCaption;
        final EditText etNewGoal = calorieGoalBinding.etNewGoal;
        Button btnConfirm = calorieGoalBinding.btnConfirm;

        tvCaption.setText("Your current caloric intake goal is :" + String.valueOf(user.getInt("calGoal")) + " calories");
        etNewGoal.setText(String.valueOf(user.getInt("calGoal")));
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newGoal = Integer.parseInt(etNewGoal.getText().toString());
                user.put("calGoal", newGoal);
                user.saveEventually();
                tvCalorieGoal.setText("Goal Calories: " + newGoal);
                goalPopup.dismiss();
            }
        });

    }
}