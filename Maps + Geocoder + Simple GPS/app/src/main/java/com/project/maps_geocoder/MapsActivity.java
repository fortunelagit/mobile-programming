package com.project.maps_geocoder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.maps_geocoder.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button go = (Button) findViewById(R.id.btnGo);
        go.setOnClickListener(op);

        ImageButton zoomin = (ImageButton) findViewById(R.id.btnZoomIn);
        zoomin.setOnClickListener(op);

        ImageButton zoomout = (ImageButton) findViewById(R.id.btnZoomOut);
        zoomout.setOnClickListener(op);

        Button geosearch = (Button) findViewById(R.id.btnSearch);
        geosearch.setOnClickListener(op);

        Button switchLive = (Button) findViewById(R.id.btnSwitchtoLive);

        switchLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, DynamicGPS.class);
                startActivity(intent);
            }
        });
    }

    View.OnClickListener op = new View.OnClickListener() {
        @Override
    public void onClick(View view) {
            if(view.getId() == R.id.btnGo){
                sembunyikanKeyBoard(view);
                gotoLokasi(8);
            }
            else if(view.getId() == R.id.btnZoomIn){
                zoomCamera('+');

            }
            else if(view.getId() == R.id.btnZoomOut){
                zoomCamera('-');
            }
            else if (view.getId() == R.id.btnSearch) {
                sembunyikanKeyBoard(view);
                goCari();
            }
            else if (view.getId() == R.id.btnSwitchtoLive) {
                setContentView(R.layout.gps_maps);
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

    private void gotoLokasi(int zoom)  {
        EditText lat = (EditText) findViewById(R.id.etLokasiLat);
        EditText lng = (EditText) findViewById(R.id.etLokasiLng);
//        EditText zoom = (EditText) findViewById(R.id.idZoom);
        Double dbllat = Double.parseDouble(lat.getText().toString());
        Double dbllng = Double.parseDouble(lng.getText().toString());
//        Float dblzoom = Float.parseFloat(zoom.getText().toString());
        Toast.makeText(this,"Move to Lat:" +dbllat + " Long:" +dbllng,Toast.LENGTH_LONG).show();
        gotoPeta(dbllat,dbllng,zoom);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng ITS = new LatLng(-7.28, 112.79);
        mMap.addMarker(new MarkerOptions().position(ITS).title("Marker in ITS"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ITS, 8));
    }

    private void gotoPeta(Double lat,
                          Double lng, float z){
        LatLng Lokasibaru = new LatLng(lat,lng);
        mMap.addMarker(new MarkerOptions().
            position(Lokasibaru).
            title("Marker in  " +lat +":" +lng));
        mMap.moveCamera(CameraUpdateFactory.
            newLatLngZoom(Lokasibaru,z));
    }

    private void goCari() {
        EditText tempat = (EditText) findViewById(R.id.etLocName);
        Geocoder g = new Geocoder(getBaseContext());
        try {
            List<Address> daftar = g.getFromLocationName(tempat.getText().toString(), 1);
            Address alamat = daftar.get(0);

            String nemuAlamat = alamat.getAddressLine(0);
            Double lintang = alamat.getLatitude();
            Double bujur = alamat.getLongitude();

            Toast.makeText(getBaseContext(), "Location Found: " + nemuAlamat, Toast.LENGTH_LONG).show();


            gotoPeta(lintang,bujur,8);

            Toast.makeText(this,"Move to "+ nemuAlamat +" Lat:" +
                    lintang + " Long:" +bujur,Toast.LENGTH_LONG).show();

            EditText lat = (EditText) findViewById(R.id.etLokasiLat);
            EditText lng = (EditText) findViewById(R.id.etLokasiLng);

            lat.setText(lintang.toString());
            lng.setText(bujur.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//
//    @Override
//    public void onPrepareMenu(@NonNull Menu menu) {
//        MenuProvider.super.onPrepareMenu(menu);
//    }
//
//    @Override
//    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
//        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
//    }
//
//    @Override
//    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
//        if(menuItem.getItemId() == R.id.normal){
//            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//            return true;
//        }
//        else if(menuItem.getItemId() == R.id.terrain){
//            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//            return true;
//        }
//        else if(menuItem.getItemId() == R.id.satellite){
//            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//            return true;
//        }
//        else if(menuItem.getItemId() == R.id.hybrid){
//            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//            return true;
//        }
//        else if(menuItem.getItemId() == R.id.none){
//            mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
//            return true;
//        }
//        return false;
//    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.normal){
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        else if(item.getItemId() == R.id.terrain){
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }
        else if(item.getItemId() == R.id.satellite){
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        else if(item.getItemId() == R.id.hybrid){
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
        else if(item.getItemId() == R.id.none){
            mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        }
        Toast.makeText(this,"Terrain changed",Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}