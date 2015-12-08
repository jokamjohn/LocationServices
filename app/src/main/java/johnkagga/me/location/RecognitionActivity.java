package johnkagga.me.location;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;


import java.util.ArrayList;

import johnkagga.me.location.services.DetectedActivitiesIntentService;

public class RecognitionActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks,
        ResultCallback<Status>{

    private static final String LOG_TAG = RecognitionActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private ActivityDetectionBroadcastReceiver mBroadcastReceiver;
    private TextView mStatusTextView;
    private Button mRequestButton;
    private Button mRemoveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_recognition);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buildGoogleClientApi();

        mBroadcastReceiver = new ActivityDetectionBroadcastReceiver();

        initializeUi();
    }

    /**
     * Initialize UI
     */
    protected void initializeUi() {
        mStatusTextView = (TextView) findViewById(R.id.detectedActivities);
        mRequestButton = (Button) findViewById(R.id.request_activity_updates_button);
        mRemoveButton = (Button) findViewById(R.id.remove_activity_updates_button);
    }

    /**
     * Build the Api client with the callbacks
     * and add the location service Apis
     */
    protected synchronized void buildGoogleClientApi() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "Connection failed");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        //Unregister the broadcast receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Register the broadcast service with same intent filter as in the service class
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter(Constants.BROADCAST_ACTION));
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected())
        {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    /**
     * Click listener for requesting updates
     *
     * @param view Request button
     */
    public void requestActivityUpdatesButtonHandler(View view)
    {
        if (!mGoogleApiClient.isConnected())
        {
            Toast.makeText(this,getString(R.string.not_connected),Toast.LENGTH_LONG).show();
            return;
        }
        ActivityRecognition.ActivityRecognitionApi
                .requestActivityUpdates(mGoogleApiClient,Constants.BROADCAST_INTERVAL, getPendingIntent())
                .setResultCallback(this);

        mRequestButton.setEnabled(false);
        mRemoveButton.setEnabled(true);
    }

    /**
     * Pending intent from the DetectedActivitiesIntentService
     * @return PendingIntent
     */
    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, DetectedActivitiesIntentService.class);
        return PendingIntent.getService(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Click listener for removing updates
     *
     * @param view Remove Button
     */
    public void removeActivityUpdatesButtonHandler (View view)
    {
        if (!mGoogleApiClient.isConnected())
        {
            Toast.makeText(this,getString(R.string.not_connected),Toast.LENGTH_LONG).show();
            return;
        }
        ActivityRecognition.ActivityRecognitionApi
        .removeActivityUpdates(mGoogleApiClient, getPendingIntent())
                .setResultCallback(this);

        mRemoveButton.setEnabled(false);
        mRequestButton.setEnabled(true);
    }

    /**
     * OnResult callback
     * @param status Status
     */
    @Override
    public void onResult(Status status) {
        if (status.isSuccess())
        {
            Log.i(LOG_TAG,"Successfully added activity recognition");
        }
        else {
            Log.i(LOG_TAG,"Activity recognition unsuccessful" + status.getStatusMessage());
        }
    }

    /**
     * Nested Broadcast receiver class
     */
    public class ActivityDetectionBroadcastReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<DetectedActivity> detectedActivities = intent
                    .getParcelableArrayListExtra(Constants.ACTIVITY_EXTRA);

            String status = "";
            for (DetectedActivity thisActivity : detectedActivities)
            {
                status += helper.getActivityString(context,thisActivity.getType())
                        + " " + thisActivity.getConfidence() + "\n";
            }
            mStatusTextView.setText(status);

        }
    }
}
