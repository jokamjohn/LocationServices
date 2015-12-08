package johnkagga.me.location.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

import johnkagga.me.location.Constants;

/**
 * Created by jokamjohn on 12/8/2015.
 */
public class DetectedActivitiesIntentService extends IntentService {

    private static final String LOG_TAG = DetectedActivitiesIntentService.class.getSimpleName();

    public DetectedActivitiesIntentService() {
        //Use the LOG_TAG to name the worker thread
        super(LOG_TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
        Intent localIntent = new Intent(Constants.BROADCAST_ACTION);

        //Get the list of probable activities
        ArrayList<DetectedActivity> detectedActivities = (ArrayList<DetectedActivity>) result.getProbableActivities();
        //Log each detected activity
        Log.i(LOG_TAG,detectedActivities.toString());
        Log.i(LOG_TAG,"detected activities");

        localIntent.putExtra(Constants.ACTIVITY_EXTRA, detectedActivities);
        //Sending the broadcast intent
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);




    }
}
