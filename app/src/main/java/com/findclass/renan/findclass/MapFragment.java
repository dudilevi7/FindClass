package com.findclass.renan.findclass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static maes.tech.intentanim.CustomIntent.customType;

@SuppressLint("ValidFragment")
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    private MapView mapView;
    private View fragment_view;
    private Context context;

    public MapFragment() {
    }

    @SuppressLint("ValidFragment")
    public MapFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        fragment_view = inflater.inflate(R.layout.fragment_map, container,false);
        return fragment_view;
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = fragment_view.findViewById(R.id.map);
        if (mapView != null){
            mapView.onCreate(null );
            mapView.onResume();
            mapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        map = googleMap;

        LatLng buildings1 = new LatLng(32.016051, 34.773182);
        map.addMarker(new MarkerOptions().position(buildings1).title(getResources().getString(R.string.buildings1)));
        map.moveCamera(CameraUpdateFactory.newLatLng(buildings1));

        LatLng buildings2 = new LatLng(32.015872, 34.772960);
        map.addMarker(new MarkerOptions().position(buildings2).title(getResources().getString(R.string.buildings2)));
        map.moveCamera(CameraUpdateFactory.newLatLng(buildings2));

        LatLng buildings3 = new LatLng(32.0153047,34.7730639);
        map.addMarker(new MarkerOptions().position(buildings3).title(getResources().getString(R.string.buildings3)));
        map.moveCamera(CameraUpdateFactory.newLatLng(buildings3));

        LatLng buildings4 = new LatLng(32.0148768,34.7728831);
        map.addMarker(new MarkerOptions().position(buildings4).title(getResources().getString(R.string.buildings4)));
        map.moveCamera(CameraUpdateFactory.newLatLng(buildings4));

        LatLng buildings5 = new LatLng(32.014593,34.773410);
        map.addMarker(new MarkerOptions().position(buildings5).title(getResources().getString(R.string.buildings5)));
        map.moveCamera(CameraUpdateFactory.newLatLng(buildings5));

        LatLng buildings6 = new LatLng(32.014380,34.774123);
        map.addMarker(new MarkerOptions().position(buildings6).title(getResources().getString(R.string.buildings6)));
        map.moveCamera(CameraUpdateFactory.newLatLng(buildings6));

        LatLng buildings7 = new LatLng(32.014175,34.774503);
        map.addMarker(new MarkerOptions().position(buildings7).title(getResources().getString(R.string.buildings7)));
        map.moveCamera(CameraUpdateFactory.newLatLng(buildings7));

        LatLng buildings8 = new LatLng(32.014034,34.773099);
        map.addMarker(new MarkerOptions().position(buildings8).title(getResources().getString(R.string.buildings8)));
        map.moveCamera(CameraUpdateFactory.newLatLng(buildings8));

        LatLng HIT = new LatLng(32.014561,34.773860);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(HIT,17),10000,null);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                switch (marker.getTitle())
                {
                    case "buildings1":case "בניין 1":
                        startBuildingsActivity("1");
                        break;
                    case "buildings2":case "בניין 2":
                        startBuildingsActivity("2");
                        break;
                    case "buildings3":case "בניין 3":
                        startBuildingsActivity("3");
                        break;
                    case "buildings4":case "בניין 4":
                        startBuildingsActivity("4");
                        break;
                    case "buildings5":case "בניין 5":
                        startBuildingsActivity("5");
                        break;
                    case "buildings6":case "בניין 6":
                        startBuildingsActivity("6");
                        break;
                    case "buildings7":case "בניין 7":
                        startBuildingsActivity("7");
                        break;
                    case "buildings8":case "בניין 8":
                        startBuildingsActivity("8");
                        break;
                }

                return false;
            }
        });
    }

    private void startBuildingsActivity(String building_number) {
        Intent intent = new Intent(context, ClassesInBuildingActivity.class);
        intent.putExtra("building_number",building_number);
        startActivity(intent);
        customType(context,"bottom-to-up"); //open classes in specific buildings
    }

}
