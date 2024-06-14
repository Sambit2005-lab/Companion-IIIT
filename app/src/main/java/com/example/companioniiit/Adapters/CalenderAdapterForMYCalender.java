package com.example.companioniiit.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.companioniiit.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

public class CalenderAdapterForMYCalender extends BaseAdapter {

    private Context context;
    private ArrayList<String> dates;
    private Map<String, Integer> eventDates;
    private LayoutInflater inflater;
    private Calendar calendar;

    public CalenderAdapterForMYCalender(Context context, ArrayList<String> dates, Map<String, Integer> eventDates, Calendar calendar) {
        this.context = context;
        this.dates = dates;
        this.eventDates = eventDates;
        this.calendar = calendar;
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
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.single_cell_calendar_layout, parent, false);
        }

        TextView dateText = convertView.findViewById(R.id.calendar_day);
        TextView eventCountText = convertView.findViewById(R.id.event_count_text);

        String date = dates.get(position);
        dateText.setText(date);

        if (date.isEmpty()) {
            eventCountText.setVisibility(View.GONE);
        } else {
            String fullDate = getFullDate(position);
            Log.d("CalendarAdapter", "FullDate: " + fullDate);
            if (eventDates.containsKey(fullDate)) {
                eventCountText.setVisibility(View.VISIBLE);
                eventCountText.setText(String.valueOf(eventDates.get(fullDate)));
            } else {
                eventCountText.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    private String getFullDate(int position) {
        String day = dates.get(position);
        if (day.isEmpty()) return "";

        Calendar tempCalendar = (Calendar) calendar.clone();
        try {
            tempCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
        } catch (NumberFormatException e) {
            Log.e("CalendarAdapter", "Error parsing day: " + day, e);
            return "";
        }

        SimpleDateFormat fullSdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return fullSdf.format(tempCalendar.getTime());
    }
}


