/**
 * FILE          : LocationSelectionActivity.java
 * AUTHORS       : Mher Keshishian
 * FIRST VERSION : January 28th
 * PURPOSE       : Activity to choose destination.
 */

package com.example.tripplanner;

import static com.example.tripplanner.model.TripState.tripDetail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripplanner.model.DestinationModel;
import com.example.tripplanner.repository.DestinationDataProvider;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LocationSelectionActivity extends Activity {

    private Button confirmButton;
    private List<DestinationModel> destinationModels;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_selection);

        initializeViews();
        setEventListeners();
        loadDestinationsAsync();
        applyAnimations();
    }

    private void initializeViews() {
        Button backButton = findViewById(R.id.backButton);
    }

    private void setEventListeners() {
        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> onBackButtonClick());
    }

    private void loadDestinationsAsync() {
        new LoadDestinationsTask().execute();
    }

    private void applyAnimations() {
        // Load animations from XML files
        Animation planTripAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_from_right);

        // Find views
        TextView planTripText = findViewById(R.id.instructionText);

        // Apply animations to views
        planTripText.startAnimation(planTripAnimation);
    }

    private void onRecyclerViewItemClick(int position, int checkedId) {
        DestinationModel selectedDestination = destinationModels.get(position);
        Intent intent = new Intent(LocationSelectionActivity.this, DetailActivity.class);
        intent.putExtra("title", selectedDestination.getName());
        intent.putExtra("description", selectedDestination.getDescription());
        intent.putExtra("imageResourceId", selectedDestination.getDataImage());
        intent.putExtra("totalPrice", selectedDestination.getTotalPrice());
        startActivity(intent);
    }


    public void onBackButtonClick() {
        finish();
    }

    private class LoadDestinationsTask extends AsyncTask<Void, Void, List<DestinationModel>> {

        @Override
        protected List<DestinationModel> doInBackground(Void... voids) {
            return DestinationDataProvider.loadDestinationsFromJson(LocationSelectionActivity.this);
        }

        @Override
        protected void onPostExecute(List<DestinationModel> result) {
            destinationModels = result;

            // Modify this part to set the correct dataImage drawable resource ID
            for (DestinationModel destination : destinationModels) {
                switch (destination.getName()) {
                    case "Niagara":
                        destination.setDataImage(R.drawable.niagara);
                        break;
                    case "Miami":
                        destination.setDataImage(R.drawable.miami);
                        break;
                    case "Las Vegas":
                        destination.setDataImage(R.drawable.las_vegas);
                        break;
                    case "London":
                        destination.setDataImage(R.drawable.london);
                        break;
                    case "Sydney":
                        destination.setDataImage(R.drawable.sydney);
                        break;
                    case "Kuala Lumpur":
                        destination.setDataImage(R.drawable.kualalumpar);
                        break;
                    // Add more cases for other destinations if needed
                }

                // Set total price for each destination
                destination.setTotalPrice(tripDetail);
            }

            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            MyAdapter myAdapter = new MyAdapter(LocationSelectionActivity.this, destinationModels);

            // Set item click listener for RecyclerView
            myAdapter.setOnItemClickListener((view, position, checkedId) -> onRecyclerViewItemClick(position, checkedId));

            recyclerView.setAdapter(myAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(LocationSelectionActivity.this));

            new CountdownTimerTask().execute();
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class CountdownTimerTask extends AsyncTask<Void, Long, Void> {
        private long startTimeMillis;
        private Timer timer;

        @Override
        protected void onPreExecute() {
            // Set the initial time for the countdown (1 hour)
            startTimeMillis = System.currentTimeMillis() + 3600000;
            timer = new Timer(true);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Timer timer = new Timer(true);

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    long currentTimeMillis = System.currentTimeMillis();
                    long remainingTimeMillis = startTimeMillis - currentTimeMillis;

                    if (remainingTimeMillis <= 0) {
                        // Timer finished, you can handle this as needed
                        timer.cancel();
                    } else {
                        // Update the UI with the remaining time
                        publishProgress(remainingTimeMillis);
                    }
                }
            };

            timer.schedule(task, 0, 1000);

            return null;
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            updateCountdownTimer(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (timer != null) {
                timer.cancel();
            }
        }
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void updateCountdownTimer(long remainingTimeMillis) {
        runOnUiThread(() -> {
            long minutes = (remainingTimeMillis % 3600000) / 60000;
            long seconds = (remainingTimeMillis % 60000) / 1000;

            TextView countdownTextView = findViewById(R.id.countdownTextView);
            countdownTextView.setText("Deals end in: " + String.format("%02d:%02d", minutes, seconds));
        });
    }
}
