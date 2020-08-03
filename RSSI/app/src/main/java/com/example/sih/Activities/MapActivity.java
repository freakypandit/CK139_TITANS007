package com.example.sih.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import com.example.sih.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap googleMap;
    SupportMapFragment mapFragment;
    SearchView searchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //Initializing
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        searchView = findViewById(R.id.searchView);

        //get Search query
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                final String location = searchView.getQuery().toString();
                List<Address> addressList=null;

                if(location !=null || !location.equals(""))
                {
                    Geocoder geocoder = new Geocoder(MapActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    googleMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {

                            Toast.makeText(MapActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();

                            AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                            builder.setTitle("You want to skip QR activity ?");

                            builder.setNegativeButton("NO",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(MapActivity.this, QrActivity.class));
                                        }
                                    });

                            builder.setPositiveButton("YES",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(MapActivity.this, BlockActivity.class));
                                        }
                                    });

                            AlertDialog dialog = builder.create();
                            dialog.show();

                            return false;
                        }
                    });
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap gmap) {

        googleMap = gmap;
        //Update Latitude and Longitude of Hospital
        googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(30.750400, 76.740230) , 4.5f) );
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
