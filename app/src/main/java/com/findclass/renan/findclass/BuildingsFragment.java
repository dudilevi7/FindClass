package com.findclass.renan.findclass;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.findclass.renan.findclass.Adapter.BuildingAdapter;
import java.util.ArrayList;
import java.util.List;
import static maes.tech.intentanim.CustomIntent.customType;


@SuppressLint("ValidFragment")
public class BuildingsFragment extends Fragment implements View.OnClickListener {
    private Context context;
    private List<Building> buildingsList;
    private BuildingAdapter buildingAdapter;

    public BuildingsFragment() {
    }

    public BuildingsFragment(Context context) {
        this.context = context;
    }

    public static BuildingsFragment getInstance() {
        BuildingsFragment buildingsFragment = new BuildingsFragment();
        return buildingsFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.recycle_view_frag,container,false);
        context = getActivity();
        buildingsList = new ArrayList<>();
        for (int i = 1 ; i<=8 ; i++) {
            buildingsList.add(new Building(i+""));
        }
        RecyclerView recyclerView = view.findViewById(R.id.recycle);
        buildingAdapter = new BuildingAdapter(buildingsList);
        buildingAdapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        buildingAdapter.setListener(new BuildingAdapter.BuildingListener() {
            @Override
            public void onItemClicked(int position, View view) {
                Building buildingTemp = buildingsList.get(position);
                startBuildingsActivity(buildingTemp.getmBuildingNumber());
            }

            @Override
            public void onLongItemClicked(int position, View view) {

            }
        });
        recyclerView.setAdapter(buildingAdapter);
        return view;
    }


    private void startBuildingsActivity(String building_number) {
        Intent intent = new Intent(getContext(), ClassesInBuildingActivity.class);
        intent.putExtra("building_number",building_number);
        startActivity(intent);
        customType(context,"fadein-to-fadeout"); //open classes in specific buildings
    }

    @Override
    public void onClick(View v) {

    }

}
