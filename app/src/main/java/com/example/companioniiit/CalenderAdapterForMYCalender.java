package com.example.companioniiit;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class CalenderAdapterForMYCalender extends BaseAdapter {
    private Context context;
    private ArrayList<String> dates;
    private Map<String, Integer> eventDates;

    public CalenderAdapterForMYCalender(Context context, ArrayList<String> dates, Map<String, Integer> eventDates) {
        this.context = context;
        this.dates = dates;
        this.eventDates = eventDates;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.single_cell_calendar_layout, parent, false);
        }

        TextView dateText = convertView.findViewById(R.id.calendar_day);
        TextView eventCountText = convertView.findViewById(R.id.event_reminder);

        String date = dates.get(position);
        dateText.setText(date);

        if (eventDates.containsKey(date)) {
            int eventCount = eventDates.get(date);
            eventCountText.setText(String.valueOf(eventCount));
            eventCountText.setVisibility(View.VISIBLE);
        } else {
            eventCountText.setVisibility(View.GONE);
        }

        return convertView;
    }
}
