package com.example.companioniiit.attendance;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.companioniiit.R;
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

    private String subjectKey;
    private String subjectName;
    private TextView textViewSubjectName;
    private TextView textViewClassPresent;
    private TextView textViewClassAbsent;
    private TextView textViewClassCancelled;

    private AppCompatImageButton backbtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);

        backbtn = findViewById(R.id.attendaceBackbtn);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(student_attendance_activity.this, attendance_card.class);
                startActivity(intent);
            }
        });



        subjectName = getIntent().getStringExtra("subject_name");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();

        } else {
            // Handle the case where the user is not logged in
        }

        textViewSubjectName = findViewById(R.id.textview_subject_name);
        textViewSubjectName.setText(subjectName);

        // Retrieve the subject key from the intent
        String subjectKey = getIntent().getStringExtra("subject_key");


        databaseReference = FirebaseDatabase.getInstance().getReference("users")
                .child(currentUserId)
                .child(subjectKey)
                .child("attendance");

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

        textViewClassPresent = findViewById(R.id.class_present_text);
        textViewClassAbsent = findViewById(R.id.class_absent_text);
        textViewClassCancelled = findViewById(R.id.class_cancelled_text);
    }

    private void displayCurrentMonth() {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String currentMonth = sdf.format(calendar.getTime());
        currentYear.setText(currentMonth);

        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        dates.clear();

        int offset = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;

        for (int i = 0; i < offset; i++) {
            dates.add("");
        }

        for (int i = 1; i <= maxDays; i++) {
            dates.add(String.valueOf(i));
        }

        if (calendarAdapter == null) {
            calendarAdapter = new CalendarAdapter(this, dates);
            dateGrid.setAdapter(calendarAdapter);
        } else {
            calendarAdapter.notifyDataSetChanged();
        }

        updateAttendanceColors();
        updateAttendanceCounts();
    }



    private void updateAttendanceColors() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Attendance attendance = snapshot.getValue(Attendance.class);
                    if (attendance != null && attendance.getDate() != null) {
                        String[] dateParts = attendance.getDate().split(" ");
                        if (dateParts.length > 0) {
                            String day = dateParts[0];
                            String monthYear = dateParts[1] + " " + dateParts[2];
                            if (sdf.format(calendar.getTime()).equals(monthYear)) {
                                int index = dates.indexOf(day);
                                if (index != -1) {
                                    View view = dateGrid.getChildAt(index);
                                    updateDateColor(view, attendance.getStatus());
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }

    private void updateAttendanceCounts() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int presentCount = 0;
                int absentCount = 0;
                int cancelledCount = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Attendance attendance = snapshot.getValue(Attendance.class);
                    if (attendance != null) {
                        String[] dateParts = attendance.getDate().split(" ");
                        String monthYear = dateParts[1] + " " + dateParts[2];
                        if (sdf.format(calendar.getTime()).equals(monthYear)) {
                            switch (attendance.getStatus()) {
                                case "present":
                                    presentCount++;
                                    break;
                                case "absent":
                                    absentCount++;
                                    break;
                                case "cancelled":
                                    cancelledCount++;
                                    break;
                            }
                        }
                    }
                }
                textViewClassPresent.setText("Class attended: " + presentCount);
                textViewClassAbsent.setText("Class not attended: " + absentCount);
                textViewClassCancelled.setText("Class cancelled: " + cancelledCount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }

    private void showPopupMenu(final View view, final String date) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_attendance_mark, popup.getMenu());

        popup.getMenu().findItem(R.id.present_mark).setIcon(R.drawable.present_mark_popup).setTitle("Present - ");
        popup.getMenu().findItem(R.id.absent_mark).setIcon(R.drawable.absent_attendance_mark).setTitle("Absent - ");
        popup.getMenu().findItem(R.id.cancelled_mark).setIcon(R.drawable.cancelled_attendance_mark).setTitle("Cancelled - ");
        popup.getMenu().findItem(R.id.clear_mark).setIcon(R.drawable.clear_mark).setTitle("Clear - ");

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
        DatabaseReference attendanceRef = databaseReference.child(date);

        // Create a new Attendance object with the subject name and status
        Attendance attendance = new Attendance(date, subjectName, status);
        // Set the attendance details in the database
        attendanceRef.setValue(attendance)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(student_attendance_activity.this, "Attendance marked successfully", Toast.LENGTH_SHORT).show();
                        updateDateColor(view, status);
                        updateAttendanceCounts();
                    } else {
                        Toast.makeText(student_attendance_activity.this, "Failed to mark attendance", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearAttendance(String date, View view) {
        databaseReference.child(date).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(student_attendance_activity.this, "Attendance cleared", Toast.LENGTH_SHORT).show();
                        updateDateColor(view, null);
                        updateAttendanceCounts();
                    }
                    else {
                        Toast.makeText(student_attendance_activity.this, "Failed to clear attendance", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateDateColor(View view, @Nullable String status) {
        if (view != null) {
            TextView dateTextView = view.findViewById(R.id.calendar_day);
            if (status == null) {
                dateTextView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            } else if (status.equals("present")) {
                dateTextView.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            } else if (status.equals("absent")) {
                dateTextView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            } else if (status.equals("cancelled")) {
                dateTextView.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            }
        }
    }

    private static class Attendance {
        private String date;
        private String subjectName;
        private String status;

        public Attendance() {
            // Default constructor required for calls to DataSnapshot.getValue(Attendance.class)
        }

        public Attendance(String date, String subjectName, String status) {
            this.date = date;
            this.subjectName = subjectName;
            this.status = status;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}

