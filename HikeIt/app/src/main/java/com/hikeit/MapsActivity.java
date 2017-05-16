package com.hikeit;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.HashMap;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;

    //Database Stuff
    private FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference rootRef = fbDatabase.getReference();
    private DatabaseReference childHikeRef = rootRef.child("hikes");
    private ArrayList<HikeListItem> allHikes = new ArrayList<HikeListItem>();

    //Marker variables
    private static final LatLng BISHOP = new LatLng(35.3025, -120.6974);
    //AKA Cerro San Luis
    private static final LatLng MADONNA = new LatLng(35.2828, -120.6804);
    //Valencia Peak
    private static final LatLng VALENCIA = new LatLng(35.2632, -120.8721);
    //Cerro Cabrillo
    private static final LatLng CERRO_CABRILLO = new LatLng(35.3521, -120.8150);
    //Irish Hills Natural Reserve
    private static final LatLng IRISH = new LatLng(35.2606, -120.7056);
    //Terrace Hill
    private static final LatLng TERRACE = new LatLng(35.273, -120.65);
    //Johnson Ranch Open Space
    private static final LatLng JOHNRANCH = new LatLng(35.2282, -120.6972);
    //WEST Cuesta Ridge
    private static final LatLng CUESTAWEST = new LatLng(35.347193, -120.629944);
    //Cal Poly "P"
    private static final LatLng POLYP = new LatLng(35.302801, -120.651702);
    //Reservoir Canyon Trailhead Parking Area
    private static final LatLng RESCANY = new LatLng(35.291159, -120.627435);
    //Oats Peak
    private static final LatLng OATS = new LatLng(35.2530, -120.8524);


    private Marker mBishop;
    private Marker mMadonna;
    private Marker mValencia;
    private Marker mCerroC;
    private Marker mIrish;
    private Marker mTerrace;
    private Marker mJohn;
    private Marker mWcuesta;
    private Marker mPolyp;
    private Marker mResCany;
    private Marker mOats;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    //START FOR DATABASE STUFF
    @Override
    public void onStart() {

        super.onStart();
        allHikes = new ArrayList<HikeListItem>();

        childHikeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    HashMap<String, Object> jsonValue = (HashMap<String, Object>) messageSnapshot.getValue();
                    String title = (String) jsonValue.get("title");
                    String difficulty = (String) jsonValue.get("difficulty");
                    ArrayList<String> imgSrc = (ArrayList<String>) jsonValue.get("imgSrc");
                    float distance = (float) ((double) jsonValue.get("distance"));
                    float rating = (float) ((double) jsonValue.get("rating"));
                    float lat = (float) ((double) jsonValue.get("lat"));
                    float lg = (float) ((double) jsonValue.get("lg"));

                    allHikes.add(new HikeListItem(imgSrc, title, HikeListItem.Difficulty.valueOf(difficulty), rating, distance, lat, lg));

                }

//                initAdapter();
            }
//
//            public void initAdapter()
//            {
//                HikeListAdapter adapter = new HikeListAdapter(curContext, allHikes.toArray(new HikeListItem[0]));
//                hikeList.setAdapter(adapter);
//            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        //Get users location

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Sydney Marker"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


//        for (int i = 0; i < allHikes.size(); i++) {
//            float latLoop = allHikes.get(i).lat;
//            float longLoop = allHikes.get(i).lg;
//            LatLng loopLL = new LatLng(latLoop, longLoop);
//
//            mMap.addMarker(new MarkerOptions()
//                    .position(loopLL)
//                    .title(allHikes.get(i).title));
//        }

        //Test Adding marker for Bishops Peak

        //System.out.println("LAT BISHOP: " + allHikes.get(0).lat);

        mBishop = mMap.addMarker(new MarkerOptions()
            .position(BISHOP)
            .title("BISHOPS"));//change the name to the database name

        //Adding Waypoints(Markers) to the Map:

        mMadonna = mMap.addMarker(new MarkerOptions()
                .position(MADONNA)
                .title("MADONNA"));//change the name to the database name

        mCerroC = mMap.addMarker(new MarkerOptions()
                .position(CERRO_CABRILLO)
                .title("CERRO_CABRILLO"));//change the name to the database name

        mIrish = mMap.addMarker(new MarkerOptions()
                .position(IRISH)
                .title("Irish Hills Natural Reserve"));//change the name to the database name

        mValencia = mMap.addMarker(new MarkerOptions()
                .position(VALENCIA)
                .title("VALENCIA"));//change the name to the database name

        mTerrace = mMap.addMarker(new MarkerOptions()
                .position(TERRACE)
                .title("TERRACE HILL"));//change the name to the database name

        mJohn = mMap.addMarker(new MarkerOptions()
                .position(JOHNRANCH)
                .title("JOHNRANCH"));//change the name to the database name

        mWcuesta = mMap.addMarker(new MarkerOptions()
                .position(CUESTAWEST)
                .title("West Cuesta Ridge"));//change the name to the database name

        mPolyp = mMap.addMarker(new MarkerOptions()
                .position(POLYP)
                .title("Cal Poly 'P'"));//change the name to the database name

        mResCany = mMap.addMarker(new MarkerOptions()
                .position(RESCANY)
                .title("Reservoir Canyon Trailhead Parking Area"));//change the name to the database name

        mOats = mMap.addMarker(new MarkerOptions()
                .position(OATS)
                .title("Oats Peak"));//change the name to the database name


//        LatLng bishopPeak = new LatLng(35, 120);
//        mMap.addMarker(new MarkerOptions().position(bishopPeak).title("Bishops Peak Marker"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(bishopPeak));



        //now get the info from firebase

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            //You can add here other case statements according to your requirement.
        }
    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
