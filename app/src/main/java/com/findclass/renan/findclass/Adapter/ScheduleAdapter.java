package com.findclass.renan.findclass.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.findclass.renan.findclass.R;
import com.findclass.renan.findclass.Schedule;

import java.util.List;

public class ScheduleAdapter extends BaseAdapter {
    private List<Schedule> scheduleList;
    private Context m_Context;

    public ScheduleAdapter(List<Schedule> scheduleList, Context m_Context) {
        this.scheduleList = scheduleList;
        this.m_Context = m_Context;
    }

    @Override
    public int getCount() {
        return scheduleList.size();
    }

    @Override
    public Object getItem(int position) {
        return scheduleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if(row == null){
            LayoutInflater inflater = (LayoutInflater)m_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_schedule,parent,false);
        }

        Schedule schedule = scheduleList.get(position);
        TextView hourTv,userTv;

        hourTv = row.findViewById(R.id.hours_display);
        userTv = row.findViewById(R.id.user_tv);

        hourTv.setText(schedule.getHour()+"");
        userTv.setText(schedule.getUser());

        return row;
    }
}
