/*
 * FILE          : TravelerInfoActivity.java
 * AUTHORS       : Mher Keshishian
 * FIRST VERSION : January 28th
 * PURPOSE       : Final check-in activity.
 */

package com.example.tripplanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tripplanner.model.TripDetail;
import com.example.tripplanner.model.TripState;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TravelerInfoActivity extends Activity {
    private Button finishButton;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveler_info);

        TextView selectedDestination = findViewById(R.id.selectedDestination);
        TextView selectedNumberOfPassengers = findViewById(R.id.selectedNumberOfPassengers);
        TextView startDateText = findViewById(R.id.startDateText);
        TextView endDateText = findViewById(R.id.endDateText);
        TextView priceText = findViewById(R.id.totalPrice);
        EditText travelerNameEditText = findViewById(R.id.travelerNameEditText);
        Button backButton = findViewById(R.id.backButton);
        finishButton = findViewById(R.id.finishButton);

        Intent intent = getIntent();
        if (intent != null) {
            // Retrieve selected location name and total price
            String selectedLocationName = intent.getStringExtra("selectedLocationName");
            long totalPrice = intent.getLongExtra("totalPrice", 0);

            selectedDestination.setText(selectedLocationName);
            priceText.setText("$" + totalPrice);

            // Set the total price in TripDetail
            TripState.tripDetail.setTotalPrice(totalPrice);
        }

        TripDetail tripDetail = TripState.tripDetail;
        // Autofill Traveler`s information (Name should be filled later)
        selectedDestination.setText(TripState.tripDetail.getLocation().getName());
        selectedNumberOfPassengers.setText(String.valueOf(TripState.tripDetail.getNumberOfPassengers()));
        startDateText.setText(dateFormat.format(TripState.tripDetail.getStartDate()));
        endDateText.setText(dateFormat.format(TripState.tripDetail.getEndDate()));

        // Set a listener for passenger's name
        travelerNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}


            public void afterTextChanged(Editable s) {
                // Set passenger name once the traveler's name field changes
                String name = s.toString();
                TripState.tripDetail.setBuyerName(name);
                finishButton.setEnabled(!name.isEmpty());
            }
        });

        // Set click listeners
        backButton.setOnClickListener(v -> onBackButtonClick());
        finishButton.setOnClickListener(v -> onFinishButtonClick());
        CheckBox insuranceCheckBox = findViewById(R.id.insuranceCheckBox);
        insuranceCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalPrice(isChecked));
        Button checkDealsButton = findViewById(R.id.checkDealsButton);
        checkDealsButton.setOnClickListener(v -> onCheckDealsButtonClick());


        // Apply animations
        applyAnimations();
    }

    private void applyAnimations() {
        // Load animations from XML files
        Animation animFromRight = AnimationUtils.loadAnimation(this, R.anim.anim_from_right);

        // Find views
        TextView planTripText = findViewById(R.id.selectedDestination);
        TextView passengers = findViewById(R.id.selectedNumberOfPassengers);
        TextView price = findViewById(R.id.totalPrice);
        TextView start = findViewById(R.id.startDateText);
        TextView end = findViewById(R.id.endDateText);

        // Apply animations to views
        planTripText.startAnimation(animFromRight);
        passengers.startAnimation(animFromRight);
        price.startAnimation(animFromRight);
        start.startAnimation(animFromRight);
        end.startAnimation(animFromRight);
    }

    /**
     * FUNCTION      : onCheckDealsButtonClick
     * PURPOSE       : Handles the click event for the "Check Deals" button.
     *                 Opens the Expedia webpage.
     * PARAMETERS    : view - The View that was clicked.
     * RETURN        : void
     */
    public void onCheckDealsButtonClick() {
        // Open the Expedia webpage deals
        String url = "https://www.expedia.ca/deals";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    /**
     * FUNCTION      : onBackButtonClick
     * PURPOSE       : Handles the click event for the back button.
     *                 Finishes the current activity.
     * RETURN        : void
     */
    public void onBackButtonClick() {
        finish();
    }

    /**
     * FUNCTION      : onFinishButtonClick
     * PURPOSE       : Handles the click event for the finish button.
     *                 Displays the passenger information in an AlertDialog.
     * RETURN        : void
     */
    public void onFinishButtonClick() {
        TripDetail tripDetail = TripState.tripDetail;

        // Check if the traveler's name is entered
        String travelerName = tripDetail.getBuyerName();
        if (travelerName == null || travelerName.isEmpty()) {
            // Show a toast if the traveler's name is not entered
            showErrorToast();
            return;
        }

        // Proceed with displaying passenger information
        String message =
                "You have been registered!" + "\n" +
                        "\n" +
                        "Ticket issued for: " + travelerName + "\n" +
                        "Destination: " + tripDetail.getLocation().getName() + "\n" +
                        "Start Date: " + dateFormat.format(tripDetail.getStartDate()) + "\n" +
                        "End Date: " + dateFormat.format(tripDetail.getEndDate()) + "\n" +
                        "Number of Passengers: " + tripDetail.getNumberOfPassengers() + "\n" +
                        "Price: $" + tripDetail.getTotalPrice();

        // Create and display a Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Travel Itinerary")
                .setMessage(message)
                .setPositiveButton("Close App", (dialog, id) -> onPassengerInfoDialogClose());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * FUNCTION      : showErrorToast
     * PURPOSE       : Displays a Toast with an error message on the UI thread.
     * PARAMETERS    : errorMessage - The error message to be displayed.
     * RETURN        : void
     */
    private void showErrorToast() {
        runOnUiThread(() ->
                Toast.makeText(TravelerInfoActivity.this, "Please enter your name", Toast.LENGTH_LONG).show()
        );
    }

    @SuppressLint("SetTextI18n")
    private void updateTotalPrice(boolean isInsuranceChecked) {
        TripDetail tripDetail = TripState.tripDetail;

        // Update the tripDetail with the insurance status
        tripDetail.setInsuranceSelected(isInsuranceChecked);

        // Update the UI
        TextView priceText = findViewById(R.id.totalPrice);
        long totalPrice = tripDetail.getTotalPrice();
        priceText.setText("$" + totalPrice);
    }


    /**
         * FUNCTION      : onPassengerInfoDialogClose
         * PURPOSE       : Handles the close event for the passenger information dialog.
         *                 Finishes the current activity and closes the application.
         * RETURN        : void
         */
    public void onPassengerInfoDialogClose() {
        this.finishAffinity();
    }
}