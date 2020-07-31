package com.example.whatscookin.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import com.example.whatscookin.activities.LoginActivity;
import com.example.whatscookin.databinding.FragmentCalorieIntakeBinding;
import com.example.whatscookin.databinding.PopupNumberBoxBinding;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;


import static com.parse.Parse.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class CalorieIntakeFragment extends Fragment {

    public static final String TAG = "CalorieIntakeFragment";
    private FragmentCalorieIntakeBinding binding;
    private ParseUser user;
    private TextView tvCalorieDisplay;
    private TextView tvCalorieGoal;
    private GraphView gvCalStats;
    private JSONArray calArray;


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

        tvCalorieDisplay = binding.tvCalorieDisplay;
        tvCalorieGoal = binding.tvCalorieGoal;
        gvCalStats = binding.gvCalStats;
        final Button btnStartDay = binding.btnStartDay;
        final Button btnLogout = binding.btnLogout;

        Log.i(TAG, ParseUser.getCurrentUser().getUsername());

        ParseUser.getCurrentUser().fetchInBackground();
        user = ParseUser.getCurrentUser();

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

        calArray = user.getJSONArray("calArray");
        if (calArray == null) {
            calArray = new JSONArray();
            calArray.put(0);
            calArray.put(0);
        }

        try {
            displayGraph();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnStartDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startDay();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    private void startDay() throws JSONException {
        gvCalStats.removeAllSeries();
        calArray.remove(0);
        calArray.put(user.getInt("calIntake"));
        user.put("calIntake", 0);
        user.put("calArray", calArray);
        tvCalorieDisplay.setText("0");
        user.pinInBackground();
        user.saveEventually();
        displayGraph();
    }

    private void displayGraph() throws JSONException {
        gvCalStats.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if ((int) value != value){
                    return "";
                }
                if (isValueX){
                    switch ((int) value) {
                        case -2:
                            return "Two Days Ago";
                        case -1:
                            return "Yesterday";
                        case 0:
                            return "Today";
                    }
                } else {
                    return super.formatLabel(value, isValueX);
                }
                return "";
            }
        });

        gvCalStats.setTitle("Calorie Intake Graph");

        gvCalStats.getViewport().setMinX(-2);
        gvCalStats.getViewport().setMaxX(0);
        gvCalStats.getViewport().setMinY(0);

        gvCalStats.getViewport().setScalable(true);
        gvCalStats.getViewport().setScalableY(true);

        LineGraphSeries<DataPoint> goalLine = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(-2, user.getInt("calGoal")),
                new DataPoint(-1, user.getInt("calGoal")),
                new DataPoint(0, user.getInt("calGoal"))
        });
        goalLine.setColor(Color.MAGENTA);
        gvCalStats.addSeries(goalLine);

        PointsGraphSeries<DataPoint> intakeBar = new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(-2, calArray.getInt(0)),
                new DataPoint(-1, calArray.getInt(1)),
                new DataPoint(0, user.getInt("calIntake"))
        });
        gvCalStats.addSeries(intakeBar);


    }

    /**
     * launch popup for the current user to update their caloric intake goals
     * @param user the current user
     */
    private void goalPopup(final ParseUser user) {
        final PopupWindow goalPopup = new PopupWindow(getContext());
        PopupNumberBoxBinding calorieGoalBinding = PopupNumberBoxBinding.inflate(getLayoutInflater());
        final View view = calorieGoalBinding.getRoot();
        goalPopup.setContentView(view);

        goalPopup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        goalPopup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);

        goalPopup.setOutsideTouchable(true);
        goalPopup.setFocusable(true);

        goalPopup.showAtLocation(view, Gravity.CENTER, 0, 0);

        final TextView tvCaption = calorieGoalBinding.tvCaption;
        final EditText etNewGoal = calorieGoalBinding.etNumber;
        final Button btnConfirm = calorieGoalBinding.btnConfirm;

        tvCaption.setText("Your current caloric intake goal is :" + String.valueOf(user.getInt("calGoal")) + " calories");
        etNewGoal.setText(String.valueOf(user.getInt("calGoal")));
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int newGoal = Integer.parseInt(etNewGoal.getText().toString());
                user.put("calGoal", newGoal);
                user.saveEventually();
                tvCalorieGoal.setText("Goal Calories: " + newGoal);
                goalPopup.dismiss();
            }
        });

    }
}