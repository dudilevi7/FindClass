package com.findclass.renan.findclass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.findclass.renan.findclass.model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static maes.tech.intentanim.CustomIntent.customType;

@SuppressLint("ValidFragment")
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    private DatabaseReference databaseReference;
    private String mosad;
    private MapView mapView;
    private View fragment_view;
    private Context context;

    public MapFragment() {
    }

    @SuppressLint("ValidFragment")
    public MapFragment(Context context) {
        this.context = context;
    }

    public static MapFragment getInstance() {
        MapFragment mapFragment = new MapFragment();
        return mapFragment;
    }

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        fragment_view = inflater.inflate(R.layout.fragment_map, container,false);
        context = getActivity();
        return fragment_view;
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = fragment_view.findViewById(R.id.map);
        if (mapView != null && isAdded()){
            mapView.onCreate(null );
            mapView.onResume();
            mapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getActivity());
        map = googleMap;
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser==null) {
            mosad = "hit";
            hitMap(map);
        }
        else {
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    mosad = user.getInstitute();
                    if (mosad.contains("manage")) {

                        LatLng buildings1 = new LatLng(31.9702487 , 34.77247);
                          if (isAdded())map.addMarker(new MarkerOptions().position(buildings1).title(getResources().getString(R.string.buildings1)));
                        map.moveCamera(CameraUpdateFactory.newLatLng(buildings1));

                        LatLng buildings2 = new LatLng(31.97015 , 34.77159);
                        if (isAdded()) map.addMarker(new MarkerOptions().position(buildings2).title(getResources().getString(R.string.buildings2)));
                        map.moveCamera(CameraUpdateFactory.newLatLng(buildings2));

                        LatLng buildings3 = new LatLng(31.969688,34.77272);
                        if (isAdded())map.addMarker(new MarkerOptions().position(buildings3).title(getResources().getString(R.string.buildings3)));
                        map.moveCamera(CameraUpdateFactory.newLatLng(buildings3));

                        LatLng buildings4 = new LatLng(31.9694022,34.772570);
                        if (isAdded())   map.addMarker(new MarkerOptions().position(buildings4).title(getResources().getString(R.string.buildings4)));
                        map.moveCamera(CameraUpdateFactory.newLatLng(buildings4));

                        LatLng buildings5 = new LatLng(31.969793,34.770972);
                        if (isAdded())  map.addMarker(new MarkerOptions().position(buildings5).title(getResources().getString(R.string.buildings5)));
                        map.moveCamera(CameraUpdateFactory.newLatLng(buildings5));

                        LatLng buildings6 = new LatLng(31.970908,34.771395);
                        if (isAdded())    map.addMarker(new MarkerOptions().position(buildings6).title(getResources().getString(R.string.buildings6)));
                        map.moveCamera(CameraUpdateFactory.newLatLng(buildings6));

                        LatLng buildings7 = new LatLng(31.969320,34.771621);
                        if (isAdded())   map.addMarker(new MarkerOptions().position(buildings7).title(getResources().getString(R.string.buildings7)));
                        map.moveCamera(CameraUpdateFactory.newLatLng(buildings7));

                        LatLng manage = new LatLng(31.969857,34.772093);
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(manage,17),10000,null);

                        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
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
                                }
                            }
                        });
                    }
                    else hitMap(map);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void hitMap(GoogleMap map) {

        LatLng buildings1 = new LatLng(32.016051, 34.773182);
        if (isAdded())  map.addMarker(new MarkerOptions().position(buildings1).title(getResources().getString(R.string.buildings1)));
        map.moveCamera(CameraUpdateFactory.newLatLng(buildings1));

        LatLng buildings2 = new LatLng(32.015872, 34.772960);
        if (isAdded())  map.addMarker(new MarkerOptions().position(buildings2).title(getResources().getString(R.string.buildings2)));
        map.moveCamera(CameraUpdateFactory.newLatLng(buildings2));

        LatLng buildings3 = new LatLng(32.015426,34.772844);
        if (isAdded()) map.addMarker(new MarkerOptions().position(buildings3).title(getResources().getString(R.string.buildings3)));
        map.moveCamera(CameraUpdateFactory.newLatLng(buildings3));

        LatLng buildings5 = new LatLng(32.014593,34.773410);
        if (isAdded()) map.addMarker(new MarkerOptions().position(buildings5).title(getResources().getString(R.string.buildings5)));
        map.moveCamera(CameraUpdateFactory.newLatLng(buildings5));

        LatLng buildings6 = new LatLng(32.014407,34.774094);
        if (isAdded()) map.addMarker(new MarkerOptions().position(buildings6).title(getResources().getString(R.string.buildings6)));
        map.moveCamera(CameraUpdateFactory.newLatLng(buildings6));

        LatLng buildings7 = new LatLng(32.014171,34.774432);
        if (isAdded()) map.addMarker(new MarkerOptions().position(buildings7).title(getResources().getString(R.string.buildings7)));
        map.moveCamera(CameraUpdateFactory.newLatLng(buildings7));

        LatLng buildings8 = new LatLng(32.014057,34.773074);
        if (isAdded())  map.addMarker(new MarkerOptions().position(buildings8).title(getResources().getString(R.string.buildings8)));
        map.moveCamera(CameraUpdateFactory.newLatLng(buildings8));

        LatLng HIT = new LatLng(32.014561,34.773860);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(HIT,17),10000,null);

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
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
            }
        });
    }

    private void manageMap(GoogleMap map) { //college Of Management Map

    }

    private void getMosad() {

    }

    private void startBuildingsActivity(String building_number) {
        Intent intent = new Intent(context, ClassesInBuildingActivity.class);
        intent.putExtra("building_number",building_number);
        startActivity(intent);
        customType(context,"bottom-to-up"); //open classes in specific buildings
    }

}
