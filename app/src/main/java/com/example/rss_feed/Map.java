package com.example.rss_feed;
// Name: Sharon Shaji
// Student ID: S1623993

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Map extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ArrayList<RssItem> earthquakeMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        earthquakeMap = (ArrayList<RssItem>) getIntent().getExtras().getSerializable("EQuake");
        Log.i("MyTag", "EQuake Size - " + earthquakeMap.size());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);


        //Navigation Bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);

        bottomNavigationView.setSelectedItemId(R.id.navigation_map);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_search:
                        Intent intent = new Intent(getApplicationContext(), Search.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("EQuakeSearch", earthquakeMap);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navigation_map:
                        return true;

                    case R.id.navigation_exit:
                        finish();
                        System.exit(0);
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // ADD COLOUR CODED MARKERS ON THE MAP BASED ON STRENGTH
        for (int i = 0; i < earthquakeMap.size(); i++) {
            double magnitude = earthquakeMap.get(i).getMagnitude();
            BitmapDescriptor EquakeMapMarkerIcon = null;
            if (magnitude < 1) {
                EquakeMapMarkerIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
            } else if (magnitude >= 1 && magnitude < 2) {
                EquakeMapMarkerIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
            } else if (magnitude >= 2) {
                EquakeMapMarkerIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
            }
            mMap.addMarker(new MarkerOptions().position(
                    new LatLng(earthquakeMap.get(i).getLatitude(), (earthquakeMap.get(i).getLongitude())))
                    .title(earthquakeMap.get(i).getLocation() + "," + earthquakeMap.get(i).getMagnitude() + "," + earthquakeMap.get(i).getPubDate())
                    .icon(EquakeMapMarkerIcon));
        }
        // POSITION THE MAP TO SHOW UK
        LatLng UK = new LatLng(54, 2);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(UK));
    }

}// End of Main Activity