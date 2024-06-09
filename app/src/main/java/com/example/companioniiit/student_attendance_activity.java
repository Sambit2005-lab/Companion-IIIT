package com.example.companioniiit;

import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class student_attendance_activity extends AppCompatActivity {

    private GridView dateGrid;
    private CalendarAdapter calendarAdapter;
    private ArrayList<String> dates;
    private TextView currentYear;
    private Calendar calendar;
    private SimpleDateFormat sdf;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String currentUserId;

    private String subjectName; // Variable to store the subject name

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);

        // Retrieve the subject name from the intent
        subjectName = getIntent().getStringExtra("subject_name");




        // Initialize Firebase reference
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        } else {
            // Handle the case where the user is not logged in
        }

        DatabaseReference subjectRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId).child("subject_name");

        subjectRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String subjectName = dataSnapshot.getValue(String.class);
                    if (subjectName != null) {
                        TextView textViewSubjectName = findViewById(R.id.textview_subject_name);
                        textViewSubjectName.setText(subjectName);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors here
            }
        });



        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(currentUserId).child("attendance");

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
                showPopupMenu(view, selectedDate);
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

    private void showPopupMenu(final View view, final String date) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_attendance_mark, popup.getMenu());

        // Set the title of the popup menu
        popup.getMenu().findItem(R.id.present_mark).setTitle("Present - " + subjectName);
        popup.getMenu().findItem(R.id.absent_mark).setTitle("Absent - " + subjectName);
        popup.getMenu().findItem(R.id.cancelled_mark).setTitle("Cancelled - " + subjectName);
        popup.getMenu().findItem(R.id.clear_mark).setTitle("Clear - " + subjectName);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.present_mark) {
                    markAttendance(date, "present", view);
                    return true;
                } else if (itemId == R.id.absent_mark) {
                    markAttendance(date, "absent", view);
                    return true;
                } else if (itemId == R.id.cancelled_mark) {
                    markAttendance(date, "cancelled", view);
                    return true;
                } else if (itemId == R.id.clear_mark) {
                    clearAttendance(date, view);
                    return true;
                } else {
                    return false;
                }
            }
        });
        popup.show();
    }

    private void markAttendance(String date, String status, View view) {
        String key = databaseReference.push().getKey();
        Attendance attendance = new Attendance(date, status);
        if (key != null) {
            databaseReference.child(key).setValue(attendance)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(student_attendance_activity.this, "Attendance marked successfully", Toast.LENGTH_SHORT).show();
                            updateDateColor(view, status);
                        } else {
                            Toast.makeText(student_attendance_activity.this, "Failed to mark attendance", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void clearAttendance(String date, View view) {
        // Implement the logic to clear attendance for the selected date
        // You can remove the attendance entry from Firebase and reset the color of the date cell
        // Here is a basic implementation:
        databaseReference.orderByChild("date").equalTo(date).getRef().removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(student_attendance_activity.this, "Attendance cleared", Toast.LENGTH_SHORT).show();
                        view.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    } else {
                        Toast.makeText(student_attendance_activity.this, "Failed to clear attendance", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateDateColor(View view, String status) {
        switch (status) {
            case "present":
                view.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                break;
            case "absent":
                view.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                break;
            case "cancelled":
                view.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                break;
        }
    }

    public static class Attendance {
        public String date;
        public String status;

        public Attendance() {
            // Default constructor required for calls to DataSnapshot.getValue(Attendance.class)
        }

        public Attendance(String date, String status) {
            this.date = date;
            this.status = status;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}




