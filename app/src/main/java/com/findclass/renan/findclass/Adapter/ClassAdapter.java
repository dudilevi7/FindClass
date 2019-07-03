package com.findclass.renan.findclass.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.findclass.renan.findclass.Building;
import com.findclass.renan.findclass.Class;
import com.findclass.renan.findclass.R;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder>  {
    private List<Class> list;
    private ClassListener listener ;

    public interface ClassListener
    {
        void onItemClicked(int position, View view);
        void onLongItemClicked(int position , View view);
    }
    public void setListener (ClassAdapter.ClassListener listener) {this.listener = listener;}
    public ClassAdapter(List<Class> list) {
        this.list = list;
    }

    @Override
    public ClassAdapter.ClassViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.classes_cell, viewGroup, false);
        ClassAdapter.ClassViewHolder classViewHolder = new ClassAdapter.ClassViewHolder(view);
        return classViewHolder;
    }

    @Override
    public void onBindViewHolder(ClassAdapter.ClassViewHolder classViewHolder, int i) {
        Class classTemp = list.get(i);
        classViewHolder.mClassNumber.setText(classViewHolder.itemView.getResources().getString(R.string.classes)+" "+
                classTemp.getmClassNumber());
        if (classTemp.getmVacant().contains("no")){
            classViewHolder.vacantColor.setBackgroundColor(classViewHolder.itemView.getResources().getColor(R.color.red));
            classViewHolder.mAvailable.setText(classViewHolder.itemView.getResources().getString(R.string.unavailable));
            classViewHolder.mHours.setText(classTemp.getmHours()+"");
        }else {
            classViewHolder.vacantColor.setBackgroundColor(classViewHolder.itemView.getResources().getColor(R.color.green));
            classViewHolder.mAvailable.setText(classViewHolder.itemView.getResources().getString(R.string.available));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ClassViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        private TextView mClassNumber;
        private TextView mAvailable;
        private TextView mHours;
        private LinearLayout vacantColor;

        public ClassViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.card_view_class);
            mClassNumber = itemView.findViewById(R.id.class_id_tv);
            mAvailable = itemView.findViewById(R.id.emptyorfull);
            vacantColor = itemView.findViewById(R.id.vacant_color_cell);
            mHours = itemView.findViewById(R.id.hours);

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
