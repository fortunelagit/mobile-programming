package com.project.maps_geocoder;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DynamicGPS extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_maps);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new myLocationListener();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    private class myLocationListener implements LocationListener {
        private TextView tvLat, tvLong;

        @Override
        public void onLocationChanged(Location location) {
            tvLat = findViewById(R.id.tvTrackingLat);
            tvLong = findViewById(R.id.tvTrackingLng);

            setTextWithLimit(String.valueOf(location.getLatitude()), tvLat);
            setTextWithLimit(String.valueOf(location.getLongitude()), tvLong);

            setLastUpdate();

            Toast.makeText(getBaseContext(), "Current location captured", Toast.LENGTH_LONG).show();
        }
    }

    private void setTextWithLimit(String text, TextView tv){
        int maxLength = 8;

        if(text.length() > maxLength) {
            String truncated = text.substring(0, maxLength);
            tv.setText(truncated);
        }
        else{
            tv.setText(text);
        }
    }

    private void setLastUpdate() {
        long currentTimeMillis = System.currentTimeMillis();
        Date currentTime = new Date(currentTimeMillis);

        // Format the time as a string
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = sdf.format(currentTime);

        // Set the formatted time to a TextView
        TextView textView = findViewById(R.id.tvLastUpdate); // Replace with your actual TextView ID
        textView.setText(formattedTime);
    }


    View.OnClickListener op = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            else if(view.getId() == R.id.btnZoomIn){
                zoomCamera('+');

            }
            else if(view.getId() == R.id.btnZoomOut){
                zoomCamera('-');
            }
        }
    };

    public void zoomCamera(char zoomType) {
        // Assuming mMap is your GoogleMap object

        // Get the current camera position
        CameraPosition currentCameraPosition = mMap.getCameraPosition();
        float zoomLevel = currentCameraPosition.zoom;

        switch(zoomType) {
            case '+': zoomLevel+=1;
                break;
            case '-': zoomLevel-=1;
                break;
        }


        // Create a new CameraPosition with the same target location and the desired zoom level
        CameraPosition newCameraPosition = new CameraPosition.Builder()
                .target(currentCameraPosition.target) // Use the same target location
                .zoom(zoomLevel) // Set the desired zoom level
                .build();

        // Create a CameraUpdate with the new CameraPosition
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(newCameraPosition);

        // Move the camera to the new position with the specified zoom level
        mMap.animateCamera(cameraUpdate);
    }


    private void sembunyikanKeyBoard(View v){
        InputMethodManager a = (InputMethodManager)
                getSystemService(INPUT_METHOD_SERVICE);
        a.hideSoftInputFromWindow(v.getWindowToken(),0);
    }
}