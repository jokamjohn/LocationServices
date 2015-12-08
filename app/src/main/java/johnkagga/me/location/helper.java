package johnkagga.me.location;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jokamjohn on 12/8/2015.
 */
public class helper {

    /**
     * Returns a string of the probable activity of the user
     * @param mContext Context
     * @param detectedActivityType activity int
     * @return string
     */
    public static String getActivityString (Context mContext, int detectedActivityType)
    {
        Resources resources = mContext.getResources();
        switch (detectedActivityType)
        {
            case DetectedActivity.IN_VEHICLE:
                return resources.getString(R.string.in_vehicle);
            case DetectedActivity.ON_BICYCLE:
                return resources.getString(R.string.on_bicycle);
            case DetectedActivity.ON_FOOT:
                return resources.getString(R.string.on_foot);
            case DetectedActivity.RUNNING:
                return resources.getString(R.string.running);
            case DetectedActivity.STILL:
                return resources.getString(R.string.still);
            case DetectedActivity.TILTING:
                return resources.getString(R.string.tilting);
            case DetectedActivity.UNKNOWN:
                return resources.getString(R.string.unknown);
            case DetectedActivity.WALKING:
                return resources.getString(R.string.walking);
            default:
                return resources.getString(R.string.unidentifiable_activity);

        }
    }

    /**
     * Get a human readable error string according to the status code
     * @param mContext Context
     * @param statusCode Status code from the GeoFence Api
     * @return Status code string
     */
    public static String getErrorString (Context mContext, int statusCode)
    {
        Resources resources = mContext.getResources();
        switch (statusCode)
        {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return resources.getString(R.string.geo_fence_not_available);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return resources.getString(R.string.geo_fence_too_many);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return resources.getString(R.string.geo_fence_too_many_pending_intents);
            default:
                return resources.getString(R.string.geo_fence_unknown_error);
        }
    }

    /**
     * Get a string for the transition int type
     *
     * @param mContext Context
     * @param transitionType Int transition type
     * @return Transition String
     */
    private static String getTransitionString(Context mContext, int transitionType)
    {
        Resources resources = mContext.getResources();

        switch (transitionType)
        {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return resources.getString(R.string.geo_transition_entered);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return resources.getString(R.string.geo_transition_existed);
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                return resources.getString(R.string.geo_fence_dwell);
            default:
                return resources.getString(R.string.geo_transition_default);
        }
    }

    /**
     * Get a String representing a transition type
     *
     * @param mContext Context
     * @param transitionType  Transition Type
     * @param triggeringGeoFences List<GeoFences>
     * @return Detail string
     */
    public static String getTransitionDetails(Context mContext,int transitionType, List<Geofence> triggeringGeoFences)
    {
        String geoTransitionStr = getTransitionString(mContext,transitionType);

        //Get the Ids of each triggered GeoFence
        ArrayList triggeringGeoFenceIdsList = new ArrayList();
        for (Geofence geofence : triggeringGeoFences)
        {
            triggeringGeoFenceIdsList.add(geofence.getRequestId());
        }

        String triggeringGeoFenceIdsStr = TextUtils.join(", ",triggeringGeoFenceIdsList);

        return geoTransitionStr + ": " + triggeringGeoFenceIdsStr;



    }
}
