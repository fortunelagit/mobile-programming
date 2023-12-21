package com.project.maps_geocoder;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DynamicGPS extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Marker currentMarker;
    private PolylineOptions polylineOptions;
    private boolean tracking = false;

    // Handler to update location every second
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final int delay = 1000; // 1 second delay

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gps_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapLive);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        Button buttonA = findViewById(R.id.btnStartUpdate);
        Button buttonB = findViewById(R.id.btnStopUpdate);

        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTracking();
            }
        });

        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTracking();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private class MyLocationListener implements LocationListener {
        private TextView tvLat, tvLong;

        @Override
        public void onLocationChanged(Location location) {
            if (location == null) {
                Toast.makeText(getBaseContext(), "Unable to capture current location", Toast.LENGTH_LONG).show();
                return;
            }

            tvLat = findViewById(R.id.tvTrackingLat);
            tvLong = findViewById(R.id.tvTrackingLng);

            setTextWithLimit(String.valueOf(location.getLatitude()), tvLat);
            setTextWithLimit(String.valueOf(location.getLongitude()), tvLong);

            setLastUpdate();

            gotoPeta(location.getLatitude(), location.getLongitude(), 14);
            updateLocation(location);

//            Toast.makeText(getBaseContext(), "Current location captured", Toast.LENGTH_LONG).show();
        }
    }

    private void setTextWithLimit(String text, TextView tv) {
        int maxLength = 8;

        if (text.length() > maxLength) {
            String truncated = text.substring(0, maxLength);
            tv.setText(truncated);
        } else {
            tv.setText(text);
        }
    }

    private void setLastUpdate() {
        long currentTimeMillis = System.currentTimeMillis();
        Date currentTime = new Date(currentTimeMillis);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = sdf.format(currentTime);

        TextView textView = findViewById(R.id.tvLastUpdate);
        textView.setText(formattedTime);
    }

    private void gotoPeta(Double lat, Double lng, float z) {
        LatLng loc = new LatLng(lat, lng);
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, z));
        } else {
            Toast.makeText(getBaseContext(), "Restart", Toast.LENGTH_LONG).show();
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapLive);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, z));
                }
            });
        }
    }

    View.OnClickListener op = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btnZoomIn) {
                zoomCamera('+');
            } else if (view.getId() == R.id.btnZoomOut) {
                zoomCamera('-');
            }
        }
    };

    public void zoomCamera(char zoomType) {
        CameraPosition currentCameraPosition = mMap.getCameraPosition();
        float zoomLevel = currentCameraPosition.zoom;

        switch (zoomType) {
            case '+':
                zoomLevel += 1;
                break;
            case '-':
                zoomLevel -= 1;
                break;
        }

        CameraPosition newCameraPosition = new CameraPosition.Builder()
                .target(currentCameraPosition.target)
                .zoom(zoomLevel)
                .build();

        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(newCameraPosition);

        mMap.animateCamera(cameraUpdate);
    }

    private void startTracking() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        if (mMap != null) {
            mMap.clear();

            polylineOptions = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
            Toast.makeText(getBaseContext(), "Tracking Start", Toast.LENGTH_LONG).show();
        }

        tracking = true;

        // Schedule the handler to run every second
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    private void updateLocation(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (mMap != null) {
            Toast.makeText(getBaseContext(), "Moving...", Toast.LENGTH_LONG).show();
            gotoPeta(location.getLatitude(), location.getLongitude(), 14);

            polylineOptions.add(latLng);

            if (currentMarker != null) {
                currentMarker.remove();
            }

            currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Marker A"));

            mMap.clear();
            mMap.addPolyline(polylineOptions);
        }
    }

    private void stopTracking() {
        if (tracking) {
            // Stop the handler
            handler.removeCallbacksAndMessages(null);

            if (currentMarker != null) {
                currentMarker = mMap.addMarker(new MarkerOptions().position(currentMarker.getPosition()).title("Marker B"));
            }

            tracking = false;
        }
    }
}
