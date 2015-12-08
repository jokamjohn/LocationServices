package johnkagga.me.location.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

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
        }

    }
}
