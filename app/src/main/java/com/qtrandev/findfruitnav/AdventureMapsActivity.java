package com.qtrandev.findfruitnav;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class AdventureMapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Adventure");
        setContentView(R.layout.activity_adventure_maps);
        setUpMapIfNeeded();
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(25.7717896, -80.2412616), 12.0f));
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(25.7717896, -80.2412616))
                .title("Mango")
                .snippet("Allowed Picking: Yes")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        Marker marker1 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(25.830696,-80.2749993))
                .title("Mango")
                .snippet("Allowed Picking: Yes")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        Marker marker2 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(25.7762188,-80.2938821))
                .title("Mango")
                .snippet("Allowed Picking: Yes")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        PolylineOptions line = new PolylineOptions();
        line.width(5);
        line.color(Color.RED);

        line.add(new LatLng(25.7717896, -80.2412616));
        line.add(new LatLng(25.830696,-80.2749993));
        line.add(new LatLng(25.7762188,-80.2938821));

        mMap.addPolyline(line);

        String[] trees = new String[3];
        trees[0] = "abc";
        trees[1] = "def";
        trees[2] = "ghi";
        Hunt newHunt = new Hunt("Mango Hunt", "Mango", trees);
        Firebase ref = new Firebase("https://findfruit.firebaseio.com/").child("hunts");
        ref.push().setValue(newHunt.getMapToWrite());
    }
}
