package com.LSK.iamlivingalone;

import android.Manifest;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    public Button Markerbtn;

    private GoogleMap mGoogleMap = null;
    private Marker currentMarker = null;

    private static final String TAG = "googlemap.example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500;

    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;

    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    Location mCurrentLocation;
    LatLng currentPosition;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;

    private View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        Markerbtn = (Button)findViewById(R.id.addMarkerBtn);
        mLayout=findViewById(R.id.layout_main);

        Log.d(TAG,"onCreate");

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        MapFragment mapFragment=(MapFragment) getFragmentManager()
                .findFragmentById(R.id.gmap);
        mapFragment.getMapAsync(this);

        Markerbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                make_marker_dialog();
            }
        });

    }

    void make_marker_dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_makemarker,null);
        builder.setView(view);

        builder.show();
    }




    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if (locationList.size() > 0) {
                location = locationList.get(locationList.size() - 1);

                currentPosition
                        = new LatLng(location.getLatitude(), location.getLongitude());

                String markerTitle = getCurrentAddress(currentPosition);
                String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                        + " 경도:" + String.valueOf(location.getLongitude());

                Log.d(TAG, "onLocationResult : " + markerSnippet);
                setCurrentLocation(location, markerTitle, markerSnippet);
                mCurrentLocation = location;
            }
        }
    };

    private void startLocationUpdates() {
        if (!checkLocationServicesStatus()) {

            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        }else {
            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);

            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED   ) {
                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }

            Log.d(TAG, "startLocationUpdates : call mFusedLocationClient.requestLocationUpdates");
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            if (checkPermission())
                mGoogleMap.setMyLocationEnabled(true);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady :");

        mGoogleMap = googleMap;



        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {


            startLocationUpdates();


        } else {

            setDefaultLocation();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();


            } else {
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }

        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                Log.d(TAG, "onMapClick :");
            }
        });
    }
        @Override
        protected void onStart() {
            super.onStart();

            Log.d(TAG, "onStart");

            if (checkPermission()) {

                Log.d(TAG, "onStart : call mFusedLocationClient.requestLocationUpdates");
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

                if (mGoogleMap!=null)
                    mGoogleMap.setMyLocationEnabled(true);

            }


        }


        @Override
        protected void onStop() {

            super.onStop();

            if (mFusedLocationClient != null) {

                Log.d(TAG, "onStop : call stopLocationUpdates");
                mFusedLocationClient.removeLocationUpdates(locationCallback);
            }
        }




        public String getCurrentAddress(LatLng latlng) {

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            List<Address> addresses;

            try {

                addresses = geocoder.getFromLocation(
                        latlng.latitude,
                        latlng.longitude,
                        1);
            } catch (IOException ioException) {
                Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
                return "지오코더 서비스 사용불가";
            } catch (IllegalArgumentException illegalArgumentException) {
                Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
                return "잘못된 GPS 좌표";
            }

            if (addresses == null || addresses.size() == 0) {
                Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
                return "주소 미발견";

            } else {
                Address address = addresses.get(0);
                return address.getAddressLine(0).toString();
            }

        }


        public boolean checkLocationServicesStatus() {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }


        public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {

            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng,17);
            mGoogleMap.moveCamera(cameraUpdate);

        }


        public void setDefaultLocation() {

            //디폴트 위치, 컴공관
            LatLng DEFAULT_LOCATION = new LatLng(35.230928, 129.082462);

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 17);
            mGoogleMap.moveCamera(cameraUpdate);

        }

        private boolean checkPermission() {

            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);

            if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                    hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {
                return true;
            }
            return false;
        }

        @Override
        public void onRequestPermissionsResult(int permsRequestCode,
        @NonNull String[] permissions,
        @NonNull int[] grandResults) {

            if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

                boolean check_result = true;

                for (int result : grandResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        check_result = false;
                        break;
                    }
                }


                if ( check_result ) {

                    startLocationUpdates();
                }
                else {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                        Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                                Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                finish();
                            }
                        }).show();

                    }else {

                        Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                                Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                finish();
                            }
                        }).show();
                    }
                }

            }
        }

        //여기부터는 GPS 활성화를 위한 메소드들
        private void showDialogForLocationServiceSetting() {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("위치 서비스 비활성화");
            builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                    + "위치 설정을 수정하실래요?");
            builder.setCancelable(true);
            builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    Intent callGPSSettingIntent
                            = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
                }
            });
            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            builder.create().show();
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
                case GPS_ENABLE_REQUEST_CODE:
                    if (checkLocationServicesStatus()) {
                        if (checkLocationServicesStatus()) {
                            Log.d(TAG, "onActivityResult : GPS 활성화 되있음");
                            needRequest = true;
                            return;
                        }
                    }
                    break;
            }
        }

}
