/**
 * FILE          : AmazingService.java
 * AUTHORS       : Mher Keshishian
 * FIRST VERSION : 2024-04-02
 * PURPOSE       : Background service performing an amazing task and displaying a notification.
 */

package com.example.tripplanner.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.tripplanner.MainActivity;
import com.example.tripplanner.R;

public class AmazingService extends Service {

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "AmazingServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("AmazingService", "Service created");
    }

    /**
     * FUNCTION      : onStartCommand
     * PURPOSE       : Called by the system every time a client explicitly starts the service by calling startService(Intent).
     *                 Performs the amazing task, sends a broadcast, displays a notification, and stops the service.
     * PARAMETERS    : intent - The Intent supplied to startService(Intent).
     *                 flags - Additional data about this start request.
     *                 startId - A unique integer representing this specific request to start.
     * RETURN        : int - The return value indicates what semantics the system should use for the service's current state
     *                      after it has been killed.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Perform your amazing task here
        Log.d("AmazingService", "Amazing task is being performed");

        // Notify the main application
        Intent broadcastIntent = new Intent("amazing_event");
        sendBroadcast(broadcastIntent);

        // Display a notification
        showNotification();

        // Stop the service after performing the task
        stopSelf();
        return START_NOT_STICKY;
    }

    /**
     * FUNCTION      : showNotification
     * PURPOSE       : Displays a notification to inform the user about the amazing service.
     * RETURN        : void
     */
    private void showNotification() {
        // Create notification channel (required for Android 8.0 and higher)
        createNotificationChannel();

        // Create an explicit intent for launching the app when the notification is tapped
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.travel_icon)
                .setContentTitle("Amazing Service")
                .setContentText("Prepare yourself for an exciting trip")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Show the notification
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    /**
     * FUNCTION      : createNotificationChannel
     * PURPOSE       : Creates a notification channel required for Android 8.0 (Oreo) and higher.
     * RETURN        : void
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Amazing Service Channel";
            String description = "Channel for Amazing Service notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * FUNCTION      : onBind
     * PURPOSE       : Called when another component wants to bind with the service.
     *                 Since this service is not intended to be bound, it returns null.
     * PARAMETERS    : intent - The Intent that was used to bind to this service.
     * RETURN        : IBinder - This method always returns null.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
