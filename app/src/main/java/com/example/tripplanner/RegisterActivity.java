/**
 * FILE          : RegisterActivity.java
 * AUTHOR        : Mher Keshishian
 * FIRST VERSION : 2024-03-05
 * PURPOSE       : User registration activity allowing users to create an account.
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

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, confirmPasswordEditText;
    private TravelDB travelDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize database and UI components
        travelDB = new TravelDB(this);
        usernameEditText = findViewById(R.id.usernameTextView);
        passwordEditText = findViewById(R.id.passwordTextView);
        confirmPasswordEditText = findViewById(R.id.confPasswordTextView);
        Button registerButton = findViewById(R.id.registerBtn);
        TextView loginTextView = findViewById(R.id.loginBtn);

        // Set click listeners
        registerButton.setOnClickListener(view -> registerUser());
        loginTextView.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

        applyAnimations();

    }

    /**
     * Validates user inputs, registers the user, and navigates to the login activity upon successful registration.
     */
    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Validate user inputs
        if (!username.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
            if (password.equals(confirmPassword)) {

                // Attempt to add the user to the database
                long result = travelDB.addUser(username, password);
                if (result > 0) {
                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    // Navigate to the login activity or any other desired activity
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    // Registration failed
                    Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Passwords do not match
                Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Empty fields
            Toast.makeText(RegisterActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void applyAnimations() {
        Animation animFromBottom = AnimationUtils.loadAnimation(this, R.anim.anim_from_bottom);

        // Find views in the register layout
        EditText usernameEditText = findViewById(R.id.usernameTextView);
        EditText passwordEditText = findViewById(R.id.passwordTextView);
        EditText confPasswordEditText = findViewById(R.id.confPasswordTextView);
        Button registerButton = findViewById(R.id.registerBtn);
        TextView loginTextView = findViewById(R.id.loginBtn);

        // Apply animations to views
        usernameEditText.startAnimation(animFromBottom);
        passwordEditText.startAnimation(animFromBottom);
        confPasswordEditText.startAnimation(animFromBottom);
        registerButton.startAnimation(animFromBottom);
        loginTextView.startAnimation(animFromBottom);
    }
}

