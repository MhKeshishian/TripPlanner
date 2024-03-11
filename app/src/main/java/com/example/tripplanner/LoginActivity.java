/**
 * FILE          : LoginActivity.java
 * AUTHOR        : Mher Keshishian
 * FIRST VERSION : 2024-03-05
 * PURPOSE       : User login activity allowing users to log in and navigate to the main activity.
 */

package com.example.tripplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tripplanner.database.TravelDB;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private TravelDB travelDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        travelDB = new TravelDB(this);
        usernameEditText = findViewById(R.id.usernameTextView);
        passwordEditText = findViewById(R.id.passwordTextView);
        Button loginButton = findViewById(R.id.loginBtn);
        TextView registerTextView = findViewById(R.id.registerTextView);

        applyAnimations();

        loginButton.setOnClickListener(view -> loginUser());

        registerTextView.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    /**
     * The login activity for users to log in and access the trip planning application.
     */
    private void loginUser() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (!username.isEmpty() && !password.isEmpty()) {
            boolean isValidUser = travelDB.checkUser(username, password);
            if (isValidUser) {
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                // Navigate to the main activity or any other desired activity
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Invalid Login", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Applies animation to the login activity views.
     */
    private void applyAnimations() {
        Animation animFromBottom = AnimationUtils.loadAnimation(this, R.anim.anim_from_bottom);

        // Find views in the login layout
        EditText usernameEditText = findViewById(R.id.usernameTextView);
        EditText passwordEditText = findViewById(R.id.passwordTextView);
        Button loginButton = findViewById(R.id.loginBtn);
        TextView registerTextView = findViewById(R.id.registerTextView);

        // Apply animations to views
        usernameEditText.startAnimation(animFromBottom);
        passwordEditText.startAnimation(animFromBottom);
        loginButton.startAnimation(animFromBottom);
        registerTextView.startAnimation(animFromBottom);
    }
}