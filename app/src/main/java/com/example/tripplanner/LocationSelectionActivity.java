/**
 * FILE          : LocationSelectionActivity.java
 * AUTHORS       : Mher Keshishian
 * FIRST VERSION : January 28th
 * PURPOSE       : Activity to choose destination.
 */

package com.example.tripplanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LocationSelectionActivity extends Activity {
    private Button confirmButton;
    private List<DestinationModel> destinationModels; // Added to store destination data

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_selection);


        // Initialize views
        RadioGroup locationRadioGroup = findViewById(R.id.locationRadioGroup);
        Button backButton = findViewById(R.id.backButton);
        confirmButton = findViewById(R.id.confirmButton);

        // Load destination details asynchronously
        new LoadDestinationsTask().execute();

        // Event Listeners
        locationRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            confirmButton.setEnabled(true);

            DestinationModel selectedDestination = getSelectedDestination(checkedId);

            if (selectedDestination != null) {
                TripState.tripDetail.setLocation(selectedDestination);
            }
        });

        backButton.setOnClickListener(v -> onBackButtonClick());
        confirmButton.setOnClickListener(v -> onConfirmButtonClick());

        // Apply animations
        applyAnimations();
    }

    private void applyAnimations() {
        // Load animations from XML files
        Animation planTripAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_from_right);

        // Find views
        TextView planTripText = findViewById(R.id.instructionText);

        // Apply animations to views
        planTripText.startAnimation(planTripAnimation);
    }


    /**
     * FUNCTION      : onBackButtonClick
     * PURPOSE       : Handles the click event for the back button.
     * Finishes the current activity and returns to the previous screen.
     * RETURN        : void
     */
    public void onBackButtonClick() {
        finish();
    }

    /**
     * FUNCTION      : onConfirmButtonClick
     * PURPOSE       : Handles the click event for the confirm button.
     * Starts the TravelerInfoActivity to proceed with the trip planning.
     * RETURN        : void
     */
    public void onConfirmButtonClick() {
        DestinationModel selectedLocation = TripState.tripDetail.getLocation();

        if (selectedLocation == null) {
            // Show a toast if no destination is selected
            showErrorToast();
        } else {
            // Proceed to the next activity since a destination is selected
            Intent intent = new Intent(LocationSelectionActivity.this, TravelerInfoActivity.class);
            startActivity(intent);
        }
    }

    /**
     * FUNCTION      : showErrorToast
     * PURPOSE       : Displays a Toast with a constant error message on the UI thread.
     * PARAMETERS    : errorMessage - The custom error message to be displayed.
     * RETURN        : void
     */
    private void showErrorToast() {
        runOnUiThread(() ->
                Toast.makeText(LocationSelectionActivity.this, "Please select a destination", Toast.LENGTH_LONG).show()
        );
    }

    private DestinationModel getSelectedDestination(int checkedId) {
        for (DestinationModel destination : destinationModels) {
            if (checkedId == destination.getRadioButtonId()) {
                return destination;
            }
        }
        return null;
    }

    private class LoadDestinationsTask extends AsyncTask<Void, Void, List<DestinationModel>> {

        @Override
        protected List<DestinationModel> doInBackground(Void... voids) {
            return loadDestinationsFromJson();
        }

        @Override
        protected void onPostExecute(List<DestinationModel> result) {
            // Store destination data
            destinationModels = result;

            // Set destination details dynamically
            for (DestinationModel destinationModel : destinationModels) {
                TextView destinationInfo = getDestinationInfoView(destinationModel.getRadioButtonId());
                if (destinationInfo != null) {
                    long totalPrice = destinationModel.getPrice() * TripState.tripDetail.getDurationInDays();
                    destinationInfo.setText("Price: $" + totalPrice);

                    // Make sure to also set the name of the destination
                    TextView destinationName = getDestinationNameView(destinationModel.getRadioButtonId());
                    if (destinationName != null) {
                        destinationName.setText(destinationModel.getName());
                    }
                }
            }
        }


    }

    private TextView getDestinationNameView(int radioButtonId) {
        // You should implement a method to dynamically get TextView based on the radio button ID
        // For simplicity, I'll provide a basic example assuming you have TextViews with specific IDs

        if (radioButtonId == R.id.niagaraRadio) {
            return findViewById(R.id.niagaraName);
        } else if (radioButtonId == R.id.miamiRadio) {
            return findViewById(R.id.miamiName);
        } else if (radioButtonId == R.id.lasVegasRadio) {
            return findViewById(R.id.lasVegasName);
        } else {
            return null;
        }
    }


    private TextView getDestinationInfoView(int radioButtonId) {
        // You should implement a method to dynamically get TextView based on the radio button ID
        // For simplicity, I'll provide a basic example assuming you have TextViews with specific IDs

        if (radioButtonId == R.id.niagaraRadio) {
            return findViewById(R.id.niagaraInfo);
        } else if (radioButtonId == R.id.miamiRadio) {
            return findViewById(R.id.miamiInfo);
        } else if (radioButtonId == R.id.lasVegasRadio) {
            return findViewById(R.id.lasVegasInfo);
        } else {
            return null;
        }
    }

    private List<DestinationModel> loadDestinationsFromJson() {
        List<DestinationModel> destinationList = new ArrayList<>();

        try {
            InputStream inputStream = getResources().openRawResource(R.raw.destinations);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            JSONArray jsonArray = new JSONArray(stringBuilder.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                int price = jsonObject.getInt("price");
                int radioButtonId = getResources().getIdentifier(
                        jsonObject.getString("radioButtonId"), "id", getPackageName());

                destinationList.add(new DestinationModel(name, price, radioButtonId));
            }

            inputStream.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return destinationList;
    }
}
