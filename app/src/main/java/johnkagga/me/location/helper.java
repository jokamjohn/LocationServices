package johnkagga.me.location;

import android.content.Context;
import android.content.res.Resources;

import com.google.android.gms.location.DetectedActivity;


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
}
