package com.findclass.renan.findclass;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import static maes.tech.intentanim.CustomIntent.customType;


@SuppressLint("ValidFragment")
public class BuildingsFragment extends Fragment implements View.OnClickListener {
    private Context context;
    private View fragment_view;
    private ImageButton building1,building2,building3,building4,
            building5,building6,building7,building8;

    public BuildingsFragment() {
    }

    public BuildingsFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment_view = inflater.inflate(R.layout.fragment_buildings, null);
        initBtns();
        return fragment_view;
    }

    private void initBtns() {
        building1 = fragment_view.findViewById(R.id.b1_Ib);
        building2 = fragment_view.findViewById(R.id.b2_Ib);
        building3 = fragment_view.findViewById(R.id.b3_Ib);
        building4 = fragment_view.findViewById(R.id.b4_Ib);
        building5 = fragment_view.findViewById(R.id.b5_Ib);
        building6 = fragment_view.findViewById(R.id.b6_Ib);
        building7 = fragment_view.findViewById(R.id.b7_Ib);
        building8 = fragment_view.findViewById(R.id.b8_Ib);

        building1.setOnClickListener(this);
        building2.setOnClickListener(this);
        building3.setOnClickListener(this);
        building4.setOnClickListener(this);
        building5.setOnClickListener(this);
        building6.setOnClickListener(this);
        building7.setOnClickListener(this);
        building8.setOnClickListener(this);
    }
    private void startBuildingsActivity(String building_number) {
        Intent intent = new Intent(context, ClassesInBuildingActivity.class);
        intent.putExtra("building_number",building_number);
        startActivity(intent);
        customType(context,"bottom-to-up"); //open classes in specific buildings
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b1_Ib:
                startBuildingsActivity("1");
                break;

            case R.id.b2_Ib:
                startBuildingsActivity("2");
                break;

            case R.id.b3_Ib:
                startBuildingsActivity("3");
                break;

            case R.id.b4_Ib:
                startBuildingsActivity("4");
                break;

            case R.id.b5_Ib:
                startBuildingsActivity("5");
                break;

            case R.id.b6_Ib:
                startBuildingsActivity("6");
                break;

            case R.id.b7_Ib:
                startBuildingsActivity("7");
                break;

            case R.id.b8_Ib:
                startBuildingsActivity("8");
                break;
        }
    }


}
