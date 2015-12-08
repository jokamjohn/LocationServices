package johnkagga.me.location;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Created by jokamjohn on 12/8/2015.
 */
public final class Constants {

    public static final String PACKAGE_NAME = "johnkagga.me.location";
    public static final String BROADCAST_ACTION = PACKAGE_NAME + ".BROADCAST_ACTION";
    public static final String ACTIVITY_EXTRA = PACKAGE_NAME + ".ACTIVITY_EXTRA";
    public static final String SHARED_PREFERENCE_NAME = PACKAGE_NAME + ".SHARED_PREF_NAME";

    public static final long BROADCAST_INTERVAL = 1000;

    public static final float GEOFENCE_RADIUS_IN_METERS = 1609; // 1 mile
    public static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;

    public static final HashMap<String, LatLng> HOSTEL_GEO_CODES =
            new HashMap<>();
    static {
        //Panicol hostel
        HOSTEL_GEO_CODES.put("Panicol",new LatLng(0.3393549,32.5622852));

        //Nana Hostel
        HOSTEL_GEO_CODES.put("Nana", new LatLng(0.3249589,32.5642499));

        //Tech Mak
        HOSTEL_GEO_CODES.put("Tech", new LatLng(0.337239,32.5621557));

        //Olympia hostel
        HOSTEL_GEO_CODES.put("Olympia", new LatLng(0.3358354,32.5583293));

    }

}
