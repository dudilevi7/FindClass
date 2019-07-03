package com.findclass.renan.findclass.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.findclass.renan.findclass.Building;
import com.findclass.renan.findclass.R;

import java.util.List;

public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.BuildingViewHolder>  {
    private List<Building> list;
    private BuildingListener listener ;

      public interface BuildingListener
    {
        void onItemClicked(int position, View view);
        void onLongItemClicked(int position , View view);
    }
    public void setListener (BuildingListener listener) {this.listener = listener;}
    public BuildingAdapter(List<Building> list) {
        this.list = list;
    }

    @Override
    public BuildingViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.building_cell, viewGroup, false);
        BuildingViewHolder buildingViewHolder = new BuildingViewHolder(view);
        return buildingViewHolder;
    }

    @Override
    public void onBindViewHolder(BuildingAdapter.BuildingViewHolder buildingViewHolder, int i) {
        Building buildingTemp = list.get(i);
        buildingViewHolder.mBuildingNumber.setText(buildingViewHolder.itemView.getResources().getString(R.string.building)
                +buildingTemp.getmBuildingNumber());
      //  buildingViewHolder.mBuildingName.setText(buildingTemp.getmBuildingName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BuildingViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        private TextView mBuildingNumber;
        private TextView mBuildingName;

        public BuildingViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.card_view);
           // mBuildingName = itemView.findViewById(R.id.building_name_tv);
            mBuildingNumber = itemView.findViewById(R.id.building_number_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                        listener.onItemClicked(getAdapterPosition(),v);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(listener != null)
                    {
                        listener.onLongItemClicked(getAdapterPosition(),v);
                    }
                    return false;
                }
            });
        }
    }
}