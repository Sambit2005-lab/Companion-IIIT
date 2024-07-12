package com.codexnovas.companioniiit.Host;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.codexnovas.companioniiit.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class hostside_vedantasamiti_societies_calendar extends AppCompatActivity {
    private DatePicker datePicker;
    private EditText eventDescription;
    private AppCompatButton saveEventButton;
    private AppCompatButton cancelEventButton;
    private DatabaseReference databaseReference;
    private DatabaseReference hostEventsRef;
    private String hostEmail;

    @SuppressLint("MissingInflatedId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostside_vedantasamiti_societies_calendar);

        datePicker = findViewById(R.id.datepicker_techsociety);
        eventDescription = findViewById(R.id.event_description_techsociety);
        saveEventButton = findViewById(R.id.save_event_techsociety);
        cancelEventButton = findViewById(R.id.cancel_post_techsociety);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("hosts");

        // Retrieve the email from the Intent
        Intent intent = getIntent();
        hostEmail = intent.getStringExtra("hostEmail");

        if (hostEmail != null) {
            if (hostEmail.equals("vedantasamiti@iiit-bh.ac.in")) {
                hostEventsRef = databaseReference.child("12").child("events");
            } else {
                Toast.makeText(this, "Invalid host email", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(this, "Host email is null", Toast.LENGTH_SHORT).show();
            return;
        }

        saveEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEventToFirebase();
            }
        });

        cancelEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveEventToFirebase() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        String description = eventDescription.getText().toString().trim();

        if (description.isEmpty()) {
            eventDescription.setError("Event description is required");
            eventDescription.requestFocus();
            return;
        }

        String date = day + "-" + (month + 1) + "-" + year;
        String eventId = hostEventsRef.push().getKey();

        if (eventId != null) {
            hostside_vedantasamiti_societies_calendar.Event event = new hostside_vedantasamiti_societies_calendar.Event(date, description);
            hostEventsRef.child(eventId).setValue(event)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(hostside_vedantasamiti_societies_calendar.this, "Event saved successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(hostside_vedantasamiti_societies_calendar.this, "Failed to save event", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public static class Event {
        public String date;
        public String description;

        public Event() {
            // Default constructor required for calls to DataSnapshot.getValue(Event.class)
        }

        public Event(String date, String description) {
            this.date = date;
            this.description = description;
        }
    }
}