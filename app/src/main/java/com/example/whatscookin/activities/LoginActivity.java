package com.example.whatscookin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whatscookin.ActivityUtils;
import com.example.whatscookin.R;
import com.example.whatscookin.databinding.ActivityLoginBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Activity responsible for logging in a user
 * Launched on app startup, but if a user wasn't logged out they are directed back to the home page
 */
public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        final View view = binding.getRoot();
        setContentView(view);

        if (ParseUser.getCurrentUser() != null){
            goMainActivity();
        }

        final TextView tvSignUp = binding.tvSignUp;
        final ImageView ivLogo = binding.ivLogo;

        if (isDarkMode()){
            Glide.with(getApplicationContext()).load(R.drawable.logo_invert).into(ivLogo);
        }
        etUsername = binding.etUsername;
        etPassword = binding.etPassword;
        btnLogin = binding.btnLogin;
        btnSignUp = binding.btnSignUp;

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLogin.setVisibility(View.GONE);
                btnSignUp.setVisibility(View.VISIBLE);
                tvSignUp.setVisibility(View.GONE);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });
        
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                signUpUser(username, password);
            }
        });

    }

    private boolean isDarkMode(){
        int nightModeFlags =
                getApplicationContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }


    private void loginUser(String username, String password) {
        Log.i(TAG, "logging in user: " + username);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with login", e);
                    return;
                }
                goMainActivity();
                Toast.makeText(LoginActivity.this, "Loging in!", Toast.LENGTH_SHORT);
            }
        });
    }

    private void signUpUser(String username, String password) {
        Log.i(TAG, "signing up user: " + username);
        final ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with Signup", e);
                    return;
                }
                goMainActivity();
                Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT);
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}