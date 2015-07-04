package com.qtrandev.findfruitnav;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A fragment that launches other parts of the demo application.
 */
public class MapFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    private View v;
    private double currentLat = 0;
    private double currentLon = 0;
    private Marker newMarker;
    private HashMap<Marker,Tree> treeMarkerReference = new HashMap<Marker,Tree>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            // inflate and return the layout
            v = inflater.inflate(R.layout.fragment_map, container,
                    false);
            mMapView = (MapView) v.findViewById(R.id.mapView);
            mMapView.onCreate(savedInstanceState);

            mMapView.onResume();// needed to get the map to display immediately

            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }

            googleMap = mMapView.getMap();
            googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

            Firebase myFirebaseRef = new Firebase("https://findfruit.firebaseio.com/");
            myFirebaseRef.child("tree").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    googleMap.clear();
                    treeMarkerReference.clear();
                    for (DataSnapshot tree : snapshot.getChildren()) {
                        Double lat = tree.child("lat").getValue(Double.class);
                        Double lng = tree.child("lng").getValue(Double.class);
                        LatLng latlng = new LatLng(lat, lng);
                        String title = tree.child("treetype").getValue(String.class);
                        String allowPick = tree.child("allowpick").getValue(String.class);
                        String content = "Allowed Picking: " + allowPick;
                        float markerColor = BitmapDescriptorFactory.HUE_RED;
                        if (allowPick.equals("Yes")) {
                            markerColor = BitmapDescriptorFactory.HUE_GREEN;
                        }
                        Tree newTree = new Tree(tree.getKey(), title, lat, lng);
                        Marker savedMarker = googleMap.addMarker(new MarkerOptions()
                                .position(latlng)
                                .title(title)
                                .snippet(content)
                                .icon(BitmapDescriptorFactory.defaultMarker(markerColor)));
                        treeMarkerReference.put(savedMarker,newTree);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) { }
            });

            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker arg0) {
                    if (arg0.getTitle().equals("Add New Tree")) {
                        Intent i = new Intent(getActivity(), NewTreeActivity.class);
                        i.putExtra("Lat", currentLat);
                        i.putExtra("Lon", currentLon);
                        startActivity(i);
                    } else {
                        Tree savedTree = treeMarkerReference.get(arg0);
                        Intent i = new Intent(getActivity(), TreeActivity.class);
                        i.putExtra("Id", savedTree.getId());
                        i.putExtra("Type", savedTree.getType());
                        startActivity(i);
                    }
                }
            });

            googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng point) {
                    currentLat = point.latitude;
                    currentLon = point.longitude;
                    newMarker = googleMap.addMarker(new MarkerOptions()
                            .position(point)
                            .title("Add New Tree")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    newMarker.showInfoWindow();
                }
            });

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(25.7717896, -80.2412616)).zoom(9).build();

            googleMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        if (newMarker != null) {
            newMarker.setVisible(false);
            newMarker = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }

    public GoogleMap getMap() {
        return googleMap;
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

        private final View myContentsView;

        MyInfoWindowAdapter(){
            myContentsView = getActivity().getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
            tvTitle.setText(marker.getTitle());
            TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.snippet));
            tvSnippet.setText(marker.getSnippet());
            ImageView imageView = (ImageView)myContentsView.findViewById(R.id.image);
            switch (marker.getTitle()) {
                case "Mango":
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.mango));
                    break;
                case "Avocado":
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.avocado));
                    break;
                case "Lychee":
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.lychee));
                    break;
                case "Longan":
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.longan));
                    break;
                default:
                    imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
            }

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

    }
}