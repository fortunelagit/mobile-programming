package com.project.maps_geocoder;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.maps_geocoder.databinding.ActivityMapsBinding;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class MapsGPS extends AppCompatActivity implements OnMapReadyCallback,
GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
LocationListener{

    private LocationManager lm;
    private LocationListener ll;
    private GoogleMap mMap;

    public static final int PERMISSION_GET_LAST_LOCATION = 10;
    public static final int PERMISSION_REQUEST_LOCATION_UPDATES = 11;

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS/2;
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected Location mCurrentLocation;

    protected Button buttonStartUpdate;
    protected Button buttonStopUpdate;
    protected TextView textLatitude;
    protected TextView textLongitude;
    protected TextView textLastUpdate;

    protected Boolean mRequestingLocationUpdates;
    protected String mLastUpdateTime = "";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_maps);
        mRequestingLocationUpdates = false;

        buttonStartUpdate = (Button) findViewById(R.id.btnStartUpdate);
        buttonStopUpdate = (Button) findViewById(R.id.btnStopUpdate);

        textLatitude = (TextView) findViewById(R.id.tvTrackingLat);
        textLongitude = (TextView) findViewById(R.id.tvTrackingLng);
        textLastUpdate = (TextView) findViewById(R.id.tvLastUpdate);

//        updateValuesFromBundle(savedInstanceState);
        buildGoogleApiClient();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapLive);
        mapFragment.getMapAsync(this);

        LocationManager mylocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lokasiListener mylocationListener = new lokasiListener();

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mylocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 200, mylocationListener);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).
                                addConnectionCallbacks(this).
                                addOnConnectionFailedListener(this).
//                                addApi(LocationServices.API).
                                build();
        createLocationRequest();
    }

    private void createLocationRequest() {
//        mLocationRequest = new LocationRequest.createFromDeprecatedProvider();
//
//        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
//        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng ITS = new LatLng(-7.28, 112.79);
        mMap.addMarker(new MarkerOptions().position(ITS).title("Marker in ITS"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ITS, 8));
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mCurrentLocation == null){
            String[] permission = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, permission, PERMISSION_GET_LAST_LOCATION);
        }
        if (mRequestingLocationUpdates){
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        String[] permission = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};
        ActivityCompat.requestPermissions(this, permission, PERMISSION_REQUEST_LOCATION_UPDATES);
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions,
                                          @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_GET_LAST_LOCATION){
            if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Location access permission rejected", Toast.LENGTH_SHORT).show();
            }
            else{

//                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateUI();
            }
        }
        else if(requestCode == PERMISSION_REQUEST_LOCATION_UPDATES){
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location access permission rejected", Toast.LENGTH_SHORT).show();
            }
            else{
//                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        }
    }

    private void updateUI() {
        textLatitude.setText(String.format("%f", mCurrentLocation.getLatitude()));
        textLongitude.setText(String.format("%f", mCurrentLocation.getLongitude()));
        textLastUpdate.setText(String.format("%s", mLastUpdateTime));

        LatLng myLoc = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
//        mMap.clear;
        mMap.addMarker(new MarkerOptions().position(myLoc).title("Yout Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 15));
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }


    private class lokasiListener implements LocationListener{
        private TextView txtLat, txtLong;

        @Override
        public void onLocationChanged(@NonNull Location location) {
            txtLat = (TextView) txtLat.findViewById(R.id.tvTrackingLat);
            txtLong = (TextView) txtLong.findViewById(R.id.tvTrackingLng);

            txtLat.setText(String.valueOf(location.getLatitude()));
            txtLong.setText(String.valueOf(location.getLongitude()));

            Toast.makeText(getBaseContext(), "GPS Captured", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            LocationListener.super.onStatusChanged(provider, status, extras);
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            LocationListener.super.onProviderEnabled(provider);
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            LocationListener.super.onProviderDisabled(provider);
        }
    }
}
