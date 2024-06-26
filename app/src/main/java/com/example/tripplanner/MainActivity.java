/**
 * FILE          : MainActivity.java
 * AUTHORS       : Mher Keshishian
 * FIRST VERSION : January 28th
 * PURPOSE       : Main Menu Activity.
 */

package com.example.tripplanner;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tripplanner.model.TripState;
import com.example.tripplanner.service.AmazingService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private TextView startDateLabel;
    private TextView endDateLabel;
    private Calendar calendar;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check and request location permissions if necessary
        if (!checkLocationPermission()) {
            requestLocationPermission();
        }

        // Start the AmazingService
        Intent serviceIntent = new Intent(this, AmazingService.class);
        startService(serviceIntent);

        try {
            Log.d("This", "Initializing widgets...");

            // Initialize views
            Spinner passengersSpinner = findViewById(R.id.passengersSpinner);
            startDateLabel = findViewById(R.id.startDateLabel);
            endDateLabel = findViewById(R.id.endDateLabel);
            Button nextButton = findViewById(R.id.nextButton);
            Button exitButton = findViewById(R.id.exitButton);

            calendar = Calendar.getInstance();

            // Number of Travelers spinner(dropDown menu)
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.passenger_options_display, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            passengersSpinner.setAdapter(adapter);

            passengersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // Use the corresponding values array
                    int numberOfPassengers = Integer.parseInt(getResources().getStringArray(R.array.passenger_options_values)[position]);
                    TripState.tripDetail.setNumberOfPassengers(numberOfPassengers);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    TripState.tripDetail.setNumberOfPassengers(-1);
                }
            });

            // Set click listeners
            startDateLabel.setOnClickListener(this);
            endDateLabel.setOnClickListener(this);
            nextButton.setOnClickListener(this);
            exitButton.setOnClickListener(this);

            // Apply animations
            applyAnimations();

            Log.d("MainActivity:onCreate", "Widgets initialized");
        } catch (Exception e) {
            Log.e("MainActivity:onCreate", "An error occurred: " + e.getMessage());
            showErrorToast("Internal Error: " + e.getMessage());
        }
    }

    /**
     * FUNCTION      : applyAnimations
     * PURPOSE       : Loads animations and applies them to certain views in the layout.
     * PARAMETERS    : None
     * RETURN        : void
     */
    private void applyAnimations() {
        // Load animations from XML files
        Animation planTripAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_from_right);
        Animation startDateAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_from_bottom);
        Animation endDateAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_from_bottom);

        // Find views
        TextView planTripText = findViewById(R.id.planTripText);
        TextView startDateLabel = findViewById(R.id.startDateLabel);
        TextView endDateLabel = findViewById(R.id.endDateLabel);

        // Apply animations to views
        planTripText.startAnimation(planTripAnimation);
        startDateLabel.startAnimation(startDateAnimation);
        endDateLabel.startAnimation(endDateAnimation);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if (viewId == R.id.startDateLabel) {
            onStartDateClick();
        } else if (viewId == R.id.endDateLabel) {
            onEndDateClick();
        } else if (viewId == R.id.nextButton) {
            onNextButtonClick();
        } else if (viewId == R.id.exitButton) {
            onExitButtonClick();
        } else {
            Log.w("MainActivity:onClick", "Unexpected click event with view ID: " + viewId);
        }
    }

    /**
     * FUNCTION      : pickStartOrEndDate
     * PARAMETERS    : isStartDate - A boolean indicating if the selected date is for the start date of the trip.
     * PURPOSE       : Opens a DatePickerDialog to select a start or end date for the trip.
     * RETURN        : void
     */
    private void pickStartOrEndDate(boolean isStartDate) {
        new DatePickerDialog(
                this,
                (datePicker, year, monthOfYear, dayOfMonth) -> {
                    // Initialize calendar
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    Date pickedDate = calendar.getTime();

                    Date startDate = TripState.tripDetail.getStartDate();
                    Date endDate = TripState.tripDetail.getEndDate();

                    if (isStartDate) {
                        startDate = pickedDate;
                    } else {
                        endDate = pickedDate;
                    }

                    boolean hasBothDates = startDate != null && endDate != null;

                    // Swap start and end dates, if start date is later than end date
                    if (hasBothDates && startDate.getTime() > endDate.getTime()) {
                        Date temp = startDate;
                        startDate = endDate;
                        endDate = temp;
                    }

                    // Update storage
                    TripState.tripDetail.setStartDate(startDate);
                    TripState.tripDetail.setEndDate(endDate);

                    // Update widgets
                    if (startDate != null) {
                        startDateLabel.setText(dateFormat.format(startDate));
                    }
                    if (endDate != null) {
                        endDateLabel.setText(dateFormat.format(endDate));
                    }

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    /**
     * FUNCTION      : onStartDateClick
     * PURPOSE       : Handles the click event for the start date label.
     *                  Opens a DatePickerDialog to select a start date for the trip.
     * RETURN        : void
     */
    public void onStartDateClick() {
        pickStartOrEndDate(true);
    }

    /**
     * FUNCTION      : onEndDateClick
     * PURPOSE       : Handles the click event for the end date label.
     *                  Opens a DatePickerDialog to select an end date for the trip.
     * RETURN        : void
     */
    public void onEndDateClick() {
        pickStartOrEndDate(false);
    }

    /**
     * FUNCTION      : onNextButtonClick
     * PURPOSE       : Handles the click event for the next button.
     *                  Starts the LocationSelectionActivity to proceed with the trip planning.
     * RETURN        : void
     */
    private void onNextButtonClick() {
        Date startDate = TripState.tripDetail.getStartDate();
        Date endDate = TripState.tripDetail.getEndDate();

        if (startDate == null || endDate == null) {
            // Show a toast if either start date or end date is not selected
            showErrorToast("Please select both start and end dates");
        } else {
            // Proceed to the next activity since both start and end dates are selected
            Intent intent = new Intent(getApplicationContext(), LocationSelectionActivity.class);
            startActivity(intent);
        }
    }

    /**
     * FUNCTION      : onExitButtonClick
     * PURPOSE       : Handles the click event for the exit button.
     *                  Finishes the current activity and closes the application.
     * RETURN        : void
     */
    public void onExitButtonClick() {
        this.finishAffinity();
    }

    /**
     * FUNCTION      : showErrorToast
     * PURPOSE       : Displays a Toast with an error message on the UI thread.
     * PARAMETERS    : errorMessage - The error message to be displayed.
     * RETURN        : void
     */
    private void showErrorToast(String errorMessage) {
        runOnUiThread(() ->
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show()
        );
    }

    /**
     * FUNCTION      : checkLocationPermission
     * PURPOSE       : Check if the app has location permission.
     * RETURN        : boolean - True if the app has permission, false otherwise.
     */
    private boolean checkLocationPermission() {
        int permissionState = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * FUNCTION      : requestLocationPermission
     * PURPOSE       : Request location permission.
     * RETURN        : void
     */
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    /**
     * FUNCTION      : onRequestPermissionsResult
     * PURPOSE       : Callback for the result from requesting permissions.
     * PARAMETERS    : requestCode - The request code passed in requestPermissions.
     *                 permissions - The requested permissions.
     *                 grantResults - The grant results for the corresponding permissions.
     * RETURN        : void
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, do your task requiring location access
            } else {
                // Permission denied, show a message and offer to navigate to app settings
                Toast.makeText(this, "Location permission is required for this app to function properly. Please grant the permission in app settings.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.fromParts("package", getPackageName(), null));
                startActivity(intent);
            }

        }
    }
}
