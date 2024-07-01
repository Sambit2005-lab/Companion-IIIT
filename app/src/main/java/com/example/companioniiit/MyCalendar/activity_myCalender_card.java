package com.example.companioniiit.MyCalendar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.companioniiit.MainActivity;
import com.example.companioniiit.Kotlin_Reminder_functionality.AddreminderPage;
import com.example.companioniiit.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class activity_myCalender_card extends AppCompatActivity {

    private GridView dateGrid;
    private CalenderAdapterForMYCalender calendarAdapter;
    private ArrayList<String> dates;
    private TextView currentYear;
    private Calendar calendar;
    private SimpleDateFormat sdf;

    private AppCompatImageButton backbtn;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String currentUserId;

    private Map<String, Integer> eventDates;
    private boolean isFabExpanded = false;

    @SuppressLint("MissingInflatedId")
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
            finish();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(currentUserId).child("events");

        dateGrid = findViewById(R.id.date_grid);
        backbtn = findViewById(R.id.back_button);
        currentYear = findViewById(R.id.current_year);
        dates = new ArrayList<>();
        eventDates = new HashMap<>();
        calendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

        //floating button functionality added
        FloatingActionButton addEventFab = findViewById(R.id.add_event_fab);
        FloatingActionButton addReminderFab = findViewById(R.id.add_reminder_fab);
        FloatingActionButton HolidayListFab = findViewById(R.id.holiday_list_fab);
        TextView HolidayListText = findViewById(R.id.holiday_list_text);
        TextView addReminderText = findViewById(R.id.add_a_reminder);

        // back btn implementation

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_myCalender_card.this, MainActivity.class);
                startActivity(intent);

            }
        });
        addEventFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFabExpanded) {
                    // Hide the secondary FABs and text views
                    addReminderFab.setVisibility(View.GONE);
                    HolidayListFab.setVisibility(View.GONE);
                    addReminderText.setVisibility(View.GONE);
                    HolidayListText.setVisibility(View.GONE);

                } else {
                    // Show the secondary FABs and text views
                    addReminderFab.setVisibility(View.VISIBLE);
                    HolidayListFab.setVisibility(View.VISIBLE);
                    addReminderText.setVisibility(View.VISIBLE);
                    HolidayListText.setVisibility(View.VISIBLE);
                    HolidayListFab.setVisibility(View.VISIBLE);
                }
                isFabExpanded = !isFabExpanded;
            }
        });

        addReminderFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_myCalender_card.this, AddreminderPage.class);
                startActivity(intent);
            }
        });

        HolidayListFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_myCalender_card.this, holidayList.class);
                startActivity(intent);
            }
        });


        displayCurrentMonth();

        dateGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDate = getFullDate(position);
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

        dateGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDate = getFullDate(position);
                showEventsDialog(selectedDate);
                return true;
            }
        });


        // Load events from Firebase
        loadEventsFromFirebase();
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
            calendarAdapter = new CalenderAdapterForMYCalender(this, dates, eventDates, calendar);
            dateGrid.setAdapter(calendarAdapter);
        } else {
            calendarAdapter.notifyDataSetChanged();
        }

        // Load events for the displayed month
        loadEventsFromFirebase();
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
        Event newEvent = new Event(date, event);

        // Generate a unique key for the new event
        databaseReference.child(date).push().setValue(newEvent)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(activity_myCalender_card.this, "Event added", Toast.LENGTH_SHORT).show();
                        // Update event count for the date
                        if (eventDates.containsKey(date)) {
                            eventDates.put(date, eventDates.get(date) + 1);
                        } else {
                            eventDates.put(date, 1);
                        }
                        calendarAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(activity_myCalender_card.this, "Failed to add event", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void loadEventsFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventDates.clear();

                // Filter events for the current month
                Calendar currentCalendar = (Calendar) calendar.clone();
                SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
                String currentMonthYear = monthYearFormat.format(currentCalendar.getTime());

                for (DataSnapshot dateSnapshot : snapshot.getChildren()) {
                    String date = dateSnapshot.getKey();
                    for (DataSnapshot eventSnapshot : dateSnapshot.getChildren()) {
                        try {
                            Event event = eventSnapshot.getValue(Event.class);
                            if (event != null) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                                Calendar eventCalendar = Calendar.getInstance();
                                eventCalendar.setTime(dateFormat.parse(event.getDate()));
                                String eventMonthYear = monthYearFormat.format(eventCalendar.getTime());
                                if (eventMonthYear.equals(currentMonthYear)) {
                                    Log.d("FirebaseEvent", "Date: " + date + ", Event: " + event.getEvent());
                                    if (eventDates.containsKey(date)) {
                                        eventDates.put(date, eventDates.get(date) + 1);
                                    } else {
                                        eventDates.put(date, 1);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                calendarAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(activity_myCalender_card.this, "Failed to load events", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private String getFullDate(int position) {
        String day = dates.get(position);
        if (day.isEmpty()) return "";
        Calendar tempCalendar = (Calendar) calendar.clone();
        tempCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
        SimpleDateFormat fullSdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return fullSdf.format(tempCalendar.getTime());
    }

    private void showEventsDialog(String date) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_show_events);
        dialog.setTitle("Events for " + date);

        TextView dateTextView = dialog.findViewById(R.id.dialog_date_text);
        dateTextView.setText(date);

        ListView eventsListView = dialog.findViewById(R.id.dialog_events_list);
        ArrayList<String> events = new ArrayList<>();

        databaseReference.child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if (event != null) {
                        events.add(event.getEvent());
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(activity_myCalender_card.this, android.R.layout.simple_list_item_1, events);
                eventsListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(activity_myCalender_card.this, "Failed to load events", Toast.LENGTH_SHORT).show();
            }
        });

        Button closeButton = dialog.findViewById(R.id.dialog_close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}

class Event {
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

    public String getEvent() {
        return event;
    }
}

