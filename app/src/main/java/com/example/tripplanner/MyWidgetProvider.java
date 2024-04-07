package com.example.tripplanner;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;

import com.example.tripplanner.model.TripDetail;
import com.example.tripplanner.model.TripState;

public class MyWidgetProvider extends AppWidgetProvider {

    /**
     * FUNCTION      : onUpdate
     * PURPOSE       : Called when the AppWidgetProvider is being updated.
     *                 Updates all widget instances with trip information.
     * PARAMETERS    : context - The context in which the receiver is running.
     *                 appWidgetManager - The AppWidgetManager object.
     *                 appWidgetIds - The appWidgetIds for which an update is needed.
     * RETURN        : void
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Update each widget instance
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    /**
     * FUNCTION      : updateWidget
     * PURPOSE       : Updates a specific widget instance with trip information.
     * PARAMETERS    : context - The context in which the receiver is running.
     *                 appWidgetManager - The AppWidgetManager object.
     *                 appWidgetId - The ID of the widget instance to update.
     * RETURN        : void
     */
    private void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        // Populate widget with trip information
        TripDetail tripDetail = TripState.tripDetail;
        if (tripDetail != null) {
            String destination = tripDetail.getLocation().getName();
            int durationInDays = (int) tripDetail.getDurationInDays();

            // Set data to widget
            views.setTextViewText(R.id.widgetDestination, "Destination: " + destination);
            views.setTextViewText(R.id.widgetDuration, "Duration of Trip: " + durationInDays + " Days");
        } else {
            // If tripDetail is not available, display default values
            views.setTextViewText(R.id.widgetDestination, "Destination: N/A");
            views.setTextViewText(R.id.widgetDuration, "Duration of Trip: N/A");
        }

        // Update widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * FUNCTION      : updateWidgetUI
     * PURPOSE       : Updates the widget UI with specified destination name and duration of stay.
     * PARAMETERS    : context - The context in which the receiver is running.
     *                 destinationName - The name of the destination to display.
     *                 durationInDays - The duration of the trip in days.
     * RETURN        : void
     */
    public static void updateWidgetUI(Context context, String destinationName, int durationInDays) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        // Set destination name and duration of stay
        views.setTextViewText(R.id.widgetDestination, "Destination: " + destinationName);
        views.setTextViewText(R.id.widgetDuration, "Duration of Trip: " + durationInDays + " Days");

        // Update the widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, MyWidgetProvider.class);
        appWidgetManager.updateAppWidget(thisWidget, views);
    }
}
