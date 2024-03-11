/**
 * FILE          : DetailActivity.java
 * AUTHOR        : Mher Keshishian
 * FIRST VERSION : 2024-03-05
 * PURPOSE       : Display details of a selected trip, including title, description, and animation effects.
 */

package com.example.tripplanner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.tripplanner.model.DestinationModel;
import com.example.tripplanner.model.TripState;

public class DetailActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Retrieve intent data
        Intent intent = getIntent();
        if (intent != null) {
            // Extract trip details from the intent
            String title = intent.getStringExtra("title");
            String description = intent.getStringExtra("description");
            int imageResourceId = intent.getIntExtra("imageResourceId", R.drawable.recycler_detail); // Default image
            long totalPrice = intent.getLongExtra("totalPrice", 0);

            // Find views in the layout
            TextView titleTextView = findViewById(R.id.detailTitle);
            TextView descTextView = findViewById(R.id.detailDesc);
            ImageView detailImage = findViewById(R.id.detailImage);
            TextView totalPriceTextView = findViewById(R.id.totalPrice);

            // Set total price in the totalPrice TextView
            totalPriceTextView.setText("Price: $" + totalPrice);

            TextView durationTextView = findViewById(R.id.durationTextView);

            // Assuming that TripState.tripDetail is the instance of TripDetail class
            int durationInDays = (int) TripState.tripDetail.getDurationInDays();

            // Set the duration in the TextView
            durationTextView.setText("Duration of Trip: " + durationInDays + " Days");

            // Set text and image resources
            titleTextView.setText(title);
            descTextView.setText(description);
            detailImage.setImageResource(imageResourceId);

            // Set click listeners
            Button confirmButton = findViewById(R.id.confirmButton);
            confirmButton.setOnClickListener(v -> onConfirmButtonClick());

            Button backButton = findViewById(R.id.backButton);
            backButton.setOnClickListener(v -> onBackButtonClick());
        }

        applyAnimations();
    }

    /**
     * Handles the click event for the confirm button.
     * Sets the selected location and proceeds to the next activity.
     */
    private void onConfirmButtonClick() {
        // Assuming you have information about the selected location
        String selectedLocationName = getIntent().getStringExtra("title");
        DestinationModel selectedLocation = new DestinationModel(selectedLocationName, 0, "", 0);

        // Set the selected location in the tripDetail
        TripState.tripDetail.setLocation(selectedLocation);

        // Get the total price from the intent
        long totalPrice = getIntent().getLongExtra("totalPrice", 0);

        // Proceed to the next activity since both start and end dates are selected
        Intent intent = new Intent(DetailActivity.this, TravelerInfoActivity.class);

        // Pass the selected location name and total price to TravelerInfoActivity
        intent.putExtra("selectedLocationName", selectedLocationName);
        intent.putExtra("totalPrice", totalPrice);

        startActivity(intent);
    }

    /**
     * Handles the click event for the back button.
     * Finishes the current activity and goes back to the previous page.
     */
    private void onBackButtonClick() {
        // Handle the back button click to go back to the previous page
        finish();
    }

    /**
     * Applies animations to various views in the layout.
     */
    private void applyAnimations() {
        Animation animFromBottom = AnimationUtils.loadAnimation(this, R.anim.anim_from_bottom);

        // Find views
        TextView titleTextView = findViewById(R.id.detailTitle);
        CardView cardView = findViewById(R.id.cardView2);
        TextView totalPriceTextView = findViewById(R.id.totalPrice);
        TextView durationTextView = findViewById(R.id.durationTextView);
        TextView descTextView = findViewById(R.id.detailDesc);
        TextView recAdditionalTextView = findViewById(R.id.recAdditional);


        // Apply animations to views
        titleTextView.startAnimation(animFromBottom);
        cardView.startAnimation(animFromBottom);
        totalPriceTextView.startAnimation(animFromBottom);
        durationTextView.startAnimation(animFromBottom);
        descTextView.startAnimation(animFromBottom);
        recAdditionalTextView.startAnimation(animFromBottom);

    }
}