package com.example.mapki;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.ZoomControls;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;


public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback, View.OnClickListener{
    private GoogleMap mMap;
    index i = new index();
    private static final String TAG = MapsActivity.class.getSimpleName();
    private static final int REQUEST_LOCATION = 1;

    Button button;
    SupportMapFragment mapFragment;
    SearchView searchView;
    LocationManager locationManager;
    LocationListener locationListener;
    ZoomControls zoom;
    Marker m;








    public void Saved(View view){
        Intent intent= new Intent(this, SavedPlaces.class);
        startActivity(intent);
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar);
        zoom = findViewById(R.id.zoom);
        zoom.show();


        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        button = findViewById(R.id.button_location);
        button.setOnClickListener(this);


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        searchView = findViewById(R.id.sv_location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        zoom.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.moveCamera(CameraUpdateFactory.zoomBy(1.0f));
            }
        });

        zoom.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.moveCamera(CameraUpdateFactory.zoomBy(-1.0f));
            }
        });


    }

    @Override
    public void onClick(View view) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                m = mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 5));
            }

        }
    }







    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        //choose the style of your app
        {
            try {
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.
                if (i.style == 1) {
                    boolean success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    this, R.raw.style_json));
                    if (!success) {
                        Log.e(TAG, "Style parsing failed.");
                    }
                } else if (i.style == 2) {
                    boolean success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    this, R.raw.style2_json));

                    if (!success) {
                        Log.e(TAG, "Style parsing failed.");
                    }
                } else if (i.style == 4) {
                    boolean success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    this, R.raw.style4_json));
                    if (!success) {
                        Log.e(TAG, "Style parsing failed.");
                    }
                } else if (i.style == 3) {
                    boolean success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    this, R.raw.style3_json));
                    if (!success) {
                        Log.e(TAG, "Style parsing failed.");
                    }
                }
            } catch (Resources.NotFoundException e) {
                Log.e(TAG, "Can't find style. Error: ", e);
            }
        }


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (m != null) {
                    m.remove();
                }
                List<Address> addressList = null;
                final String location = searchView.getQuery().toString();


                if (location != null || !location.equals((""))) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        Address address = addressList.get(0);
                        final LatLng userLocation = new LatLng(address.getLatitude(), address.getLongitude());
                        m = mMap.addMarker(new MarkerOptions().position(userLocation).title(location));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10));
                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                                if (marker.equals(m)){
                                    builder.setTitle("Dear User, ");
                                    builder.setMessage("Do You want to save this place?");
                                }

                                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (SavedPlaces.savedLoc.size()!=0){
                                        SavedPlaces.savedLoc.add(SavedPlaces.savedLoc.size()-1,userLocation);
                                        SavedPlaces.savedName.add(SavedPlaces.savedLoc.size()-1, location);}else{
                                            SavedPlaces.savedLoc.add(0,userLocation);
                                            SavedPlaces.savedName.add(0, location);
                                        }

                                    }
                                });
                                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });

                                // Create the AlertDialog
                                AlertDialog dialog = builder.create();
                                dialog.show();
                                return false;
                            }
                        });


                    } catch (IndexOutOfBoundsException e) {
                        Toast.makeText(getApplicationContext(), "Sorry, I don't know this place", Toast.LENGTH_LONG).show();
                    }
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        // Zoom into users location
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location userLocation) {
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };


        mapFragment.getMapAsync(this);


    }



}

