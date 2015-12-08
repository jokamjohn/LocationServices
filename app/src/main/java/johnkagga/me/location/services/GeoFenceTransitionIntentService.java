package johnkagga.me.location.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

import johnkagga.me.location.MainActivity;
import johnkagga.me.location.R;
import johnkagga.me.location.helper;

/**
 * Created by jokamjohn on 12/8/2015.
 */
public class GeoFenceTransitionIntentService extends IntentService {

    private static final String LOG_TAG = GeoFenceTransitionIntentService.class.getSimpleName();

    public GeoFenceTransitionIntentService() {
        super(LOG_TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError())
        {
            String errorMessage = helper.getErrorString(this,geofencingEvent.getErrorCode());

            Log.i(LOG_TAG,errorMessage);
            //return out of the function if there exists an error
            return;
        }

        //Get the Geo transition constant
        int transitionType = geofencingEvent.getGeofenceTransition();

        if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER
                || transitionType == Geofence.GEOFENCE_TRANSITION_EXIT)
        {
            //List of geo fences that might have been triggered
            List<Geofence> triggeringGeoFences = geofencingEvent.getTriggeringGeofences();

            //GeoFence transition string
            String geoFenceTransitionDetails = helper.getTransitionDetails(this,
                    transitionType,triggeringGeoFences);

            sendNotification(geoFenceTransitionDetails);
        }

    }

    /**
     * Sending notification
     * @param notificationDetails Notification message
     */
    private void sendNotification(String notificationDetails) {
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MainActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // Define the notification settings.
        builder.setSmallIcon(R.mipmap.ic_launcher)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                .setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText(getString(R.string.geofence_transition_notification_text))
                .setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());
    }

}
