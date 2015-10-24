package ru.example.druger.obninskroutes;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import ru.example.druger.obninskroutes.db.DBHelper;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    public final String ID_ROUTE = "id_route";

    private ArrayList<String> titleStops; // названия остановок
    private List<LatLng> latLngStops; // координаты остановок

    private DBHelper dbHelper;
    //private Intent intent = getIntent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        dbHelper = new DBHelper(this);

        titleStops = dbHelper.getBusStops(getIntent().getIntExtra(ID_ROUTE, 0));

        setUpMapIfNeeded();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        latLngStops = dbHelper.getLatLngStops(getIntent().getIntExtra(ID_ROUTE, 0));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLngStops.get(0))
                .zoom(17)
                .bearing(45)
                .tilt(20)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.animateCamera(cameraUpdate);

        addMarkerOnMap();
        drawLineBetweenStops();
    }

    /**
     * добавляет остановки на карту
     */
    private void addMarkerOnMap(){
        for (int i = 0; i < titleStops.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(latLngStops.get(i))
                    .title(titleStops.get(i))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus)));
        }
    }

    /**
     * рисует линии между остановками
     */
    private void drawLineBetweenStops(){
        List<LatLng> latLngWaypoints = dbHelper.getLatLngWaypoints(getIntent().getIntExtra(ID_ROUTE, 0));

        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(latLngWaypoints)
                .color(Color.BLUE)
                .width(10);
        mMap.addPolyline(polylineOptions);
    }

    public void changeViewMap(View view) {
        switch (mMap.getMapType()){
            case GoogleMap.MAP_TYPE_SATELLITE:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case GoogleMap.MAP_TYPE_NORMAL:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}
