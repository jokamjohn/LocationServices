package johnkagga.me.location;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
                GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    private TextView mTextView;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private TextView mTimeTextview;
    private TextView mLongTextview;
    private String mLastUpdatedTime;
    private Button mLocationTwoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buildGoogleClientApi();

        initializeScreen();

    }

    /**
     * Build the Api client with the callbacks
     * and add the location service Apis
     */
    protected synchronized void buildGoogleClientApi() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    /**
     * Initializing the Screen views
     */
    protected void initializeScreen() {
        mTextView = (TextView) findViewById(R.id.textViewLocation);
        mTimeTextview = (TextView) findViewById(R.id.textviewTime);
        mLongTextview = (TextView) findViewById(R.id.textViewlongitude);
        mLocationTwoButton = (Button) findViewById(R.id.locationTwo);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Connect the client
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        //Disconnect the client
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();

    }

    /**
     * Getting Location Updates from the OnConnected
     */
    protected void startLocationUpdates() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);//Update every second

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "connection failed");
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.i(LOG_TAG,location.toString());
        mLastUpdatedTime = DateFormat.getTimeInstance().format(new Date());

        updateUi(location);
    }

    /**
     * Upadting the User Interface
     * @param location Location object
     */
    protected void updateUi(Location location) {
        mTextView.setText(String.valueOf(location.getLatitude()));
        mLongTextview.setText(String.valueOf(location.getLongitude()));
        mTimeTextview.setText(mLastUpdatedTime);
    }

    /**
     * Launch Activity two
     * @param view
     */
    public void toLocationTwo(View view)
    {
        Intent intent = new Intent(this,LocationTwo.class);
        startActivity(intent);
    }

    /**
     * Start Recognition activity
     * @param view Button
     */
    public void toActivityRecognition(View view)
    {
        Intent intent = new Intent(this,RecognitionActivity.class);
        startActivity(intent);
    }
}
