package com.example.companioniiit;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class activity_myCalender_card extends AppCompatActivity {

    private GridView dateGrid;
    private CalendarAdapter calendarAdapter;
    private ArrayList<String> dates;
    private TextView currentYear;
    private Calendar calendar;
    private SimpleDateFormat sdf;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String currentUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_calendar_card);

        // Initialize Firebase reference
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        } else {
            // Handle the case where the user is not logged in
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(currentUserId).child("events");

        dateGrid = findViewById(R.id.date_grid);
        currentYear = findViewById(R.id.current_year);
        dates = new ArrayList<>();
        calendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

        displayCurrentMonth();

        dateGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDate = dates.get(position) + " " + sdf.format(calendar.getTime());
                showAddEventDialog(selectedDate);
            }
        });

        // Add click listeners for the navigation buttons
        findViewById(R.id.calendar_prev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                displayCurrentMonth();
            }
        });

        findViewById(R.id.calendar_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                displayCurrentMonth();
            }
        });
    }

    private void displayCurrentMonth() {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String currentMonth = sdf.format(calendar.getTime());
        currentYear.setText(currentMonth);

        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        dates.clear();

        // Calculate the offset for the first day of the month
        int offset = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;

        // Add empty cells for the offset
        for (int i = 0; i < offset; i++) {
            dates.add("");
        }

        // Add the dates for the month
        for (int i = 1; i <= maxDays; i++) {
            dates.add(String.valueOf(i));
        }

        if (calendarAdapter == null) {
            calendarAdapter = new CalendarAdapter(this, dates);
            dateGrid.setAdapter(calendarAdapter);
        } else {
            calendarAdapter.notifyDataSetChanged();
        }
    }


    private void showAddEventDialog(final String date) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_event);
        dialog.setTitle("Add Event for " + date);

        final EditText eventEditText = dialog.findViewById(R.id.event_edit_text);
        Button addButton = dialog.findViewById(R.id.add_event_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String event = eventEditText.getText().toString().trim();
                if (!event.isEmpty()) {
                    saveEventToFirebase(date, event);
                    dialog.dismiss();
                } else {
                    Toast.makeText(activity_myCalender_card.this, "Event cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void saveEventToFirebase(String date, String event) {
        String key = databaseReference.push().getKey();
        Event newEvent = new Event(date, event);
        if (key != null) {
            databaseReference.child(key).setValue(newEvent)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(activity_myCalender_card.this, "Event added successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity_myCalender_card.this, "Failed to add event", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public static class Event {
        public String date;
        public String event;

        public Event() {
            // Default constructor required for calls to DataSnapshot.getValue(Event.class)
        }

        public Event(String date, String event) {
            this.date = date;
            this.event = event;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }
    }
}
