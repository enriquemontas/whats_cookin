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
import com.example.whatscookin.fragments.CalorieIntakeFragment;
import com.example.whatscookin.fragments.HomeFragment;
import com.example.whatscookin.fragments.RecipeLookupFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * MainActivity responsible for fragment manager and bottom navigation
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setLogo(R.drawable.logo_invert);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        final BottomNavigationView bottomNavigation = binding.bottomNavigation;

        final FragmentManager fragmentManager = getSupportFragmentManager();

        // define fragments
        final Fragment fragment1 = new HomeFragment();
        final Fragment fragment2 = new CalorieIntakeFragment();
        final Fragment fragment3 = new RecipeLookupFragment();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        fragment = fragment1;
                        break;
                    case R.id.action_caloire_view:
                        fragment = fragment2;
                        break;
                    case R.id.action_recipe:
                    default:
                        fragment = fragment3;
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.fragContainer, fragment).commit();
                return true;
            }
        });
        // Set default selection
        bottomNavigation.setSelectedItemId(R.id.action_home);
    }
}