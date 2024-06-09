package com.example.companioniiit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class CalendarAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> dates;
    private LayoutInflater inflater;

    public CalendarAdapter(Context context, ArrayList<String> dates) {
        this.context = context;
        this.dates = dates;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.single_cell_calendar_layout, parent, false);
            holder = new ViewHolder();
            holder.dateTextView = convertView.findViewById(R.id.calendar_day);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.dateTextView.setText(dates.get(position));

        return convertView;
    }

    static class ViewHolder {
        TextView dateTextView;
    }
}

