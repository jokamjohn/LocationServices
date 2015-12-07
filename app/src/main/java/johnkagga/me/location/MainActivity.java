package johnkagga.me.location;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        initializeScreen();

    }

    /**
     * Initializing the Screen views
     */
    protected void initializeScreen() {
        mTextView = (TextView) findViewById(R.id.textViewLocation);
        mTimeTextview = (TextView) findViewById(R.id.textviewTime);
        mLongTextview = (TextView) findViewById(R.id.textViewlongitude);
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
        mGoogleApiClient.disconnect();
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
                mLocationRequest,this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG,"Connection suspended");
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
}
