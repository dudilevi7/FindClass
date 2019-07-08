package com.findclass.renan.findclass.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.findclass.renan.findclass.Building;
import com.findclass.renan.findclass.R;
import com.findclass.renan.findclass.model.User;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.BuildingViewHolder>  {
    private List<Building> list;
    private BuildingListener listener ;
    private DatabaseReference databaseReference;
    private String mosad;

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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_building, viewGroup, false);
        BuildingViewHolder buildingViewHolder = new BuildingViewHolder(view);
        return buildingViewHolder;
    }

    @Override
    public void onBindViewHolder(BuildingAdapter.BuildingViewHolder buildingViewHolder, int i) {
        Building buildingTemp = list.get(i);
        buildingViewHolder.mBuildingNumber.setText(buildingViewHolder.itemView.getResources().getString(R.string.building)
                +buildingTemp.getmBuildingNumber());
        int building_Image = getInstituteImage();
        buildingViewHolder.mInstituteIv.setImageResource(building_Image);
      //  buildingViewHolder.mBuildingName.setText(buildingTemp.getmBuildingName());
    }

    private int getInstituteImage() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser==null) return R.drawable.hit;
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mosad = user.getInstitute();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        switch (mosad){
            case "HIT":
                return R.drawable.hit;
            case "manage":
                return R.drawable.manage_college;
            case "ONO":
                return R.drawable.ono_academic;
            case "TLV":
                return R.drawable.tlv_academic;
            case "SCE":
                return R.drawable.sammyshimon;
        }
        return R.drawable.hit;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BuildingViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        private TextView mBuildingNumber;
        private TextView mBuildingName;
        private ImageView mInstituteIv;

        public BuildingViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.card_view);
           // mBuildingName = itemView.findViewById(R.id.building_name_tv);
            mBuildingNumber = itemView.findViewById(R.id.building_number_tv);
            mInstituteIv = itemView.findViewById(R.id.mosad_iv);

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