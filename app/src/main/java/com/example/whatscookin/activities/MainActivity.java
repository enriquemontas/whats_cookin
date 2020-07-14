package com.example.whatscookin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.whatscookin.R;
import com.example.whatscookin.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        bottomNavigation = binding.bottomNavigation;

        final FragmentManager fragmentManager = getSupportFragmentManager();

        // define your fragments
//        final Fragment fragment1 = new HomeFragment();
//        final Fragment fragment2 = new ViewCalorieFragment();
//        final Fragment fragment3 = new RecipeFragment();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        Toast.makeText(MainActivity.this,"HOME!", Toast.LENGTH_SHORT).show();
//                        fragment = fragment1;
                        break;
                    case R.id.action_caloire_view:
                        Toast.makeText(MainActivity.this,"Cal View!", Toast.LENGTH_SHORT).show();
//                        fragment = fragment2;
                        break;
                    case R.id.action_recipe:
                    default:
                        Toast.makeText(MainActivity.this,"Recipe!", Toast.LENGTH_SHORT).show();
//                        fragment = fragment3;
                        break;
                }
//                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // Set default selection
        bottomNavigation.setSelectedItemId(R.id.action_home);
    }
}