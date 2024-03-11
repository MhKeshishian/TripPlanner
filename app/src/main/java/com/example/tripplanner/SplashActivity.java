/**
 * FILE          : SplashActivity.java
 * AUTHOR        : Mher Keshishian
 * FIRST VERSION : 2024-03-05
 * PURPOSE       : Splash screen activity to display a welcome screen with a delay before navigating to the login activity.
 */

package com.example.tripplanner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int secondsDelayed = 3;
        // Delay for 3 seconds using the Handler.
        // I could use Runnable instead on lambda.
        new Handler().postDelayed(() -> {
            // Start MainActivity after the delay.
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            // Finish SplashActivity to prevent going back to it.
            finish();
        }, secondsDelayed * 1000);
    }
}