package com.example.tripplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tripplanner.model.DestinationModel;
import com.example.tripplanner.model.TripState;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String description = intent.getStringExtra("description");
            int imageResourceId = intent.getIntExtra("imageResourceId", R.drawable.recycler_detail); // Default image
            long totalPrice = intent.getLongExtra("totalPrice", 0);

            TextView titleTextView = findViewById(R.id.detailTitle);
            TextView descTextView = findViewById(R.id.detailDesc);
            ImageView detailImage = findViewById(R.id.detailImage);
            TextView totalPriceTextView = findViewById(R.id.totalPrice);

            // Set total price in the totalPrice TextView
            totalPriceTextView.setText("Price: $" + totalPrice);

            titleTextView.setText(title);
            descTextView.setText(description);
            detailImage.setImageResource(imageResourceId);

            Button confirmButton = findViewById(R.id.confirmButton);
            confirmButton.setOnClickListener(v -> onConfirmButtonClick());

            Button backButton = findViewById(R.id.backButton);
            backButton.setOnClickListener(v -> onBackButtonClick());
        }
    }


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

    private void onBackButtonClick() {
        // Handle the back button click to go back to the previous page
        finish();
    }


}

