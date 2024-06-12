package com.example.companioniiit;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class add_reminder_page extends AppCompatActivity {

    private EditText titleEditText, descriptionEditText;
    private Button datePickerButton, timePickerButton, saveReminderButton;
    private int year, month, day, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder_page);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        datePickerButton = findViewById(R.id.datePickerButton);
        timePickerButton = findViewById(R.id.timePickerButton);
        saveReminderButton = findViewById(R.id.saveReminderButton);

        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(add_reminder_page.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                add_reminder_page.this.year = year;
                                add_reminder_page.this.month = monthOfYear;
                                add_reminder_page.this.day = dayOfMonth;
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        timePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(add_reminder_page.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                add_reminder_page.this.hour = hourOfDay;
                                add_reminder_page.this.minute = minute;
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        saveReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();

                if (title.isEmpty() || description.isEmpty()) {
                    Toast.makeText(add_reminder_page.this, "Title and Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day, hour, minute);

                saveReminderToFirebase(title, description, calendar.getTimeInMillis());
                scheduleNotification(title, description, calendar.getTimeInMillis());
            }
        });
    }

    private void saveReminderToFirebase(String title, String description, long time) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("reminders");
        String reminderId = databaseReference.push().getKey();

        Reminder reminder = new Reminder(reminderId, title, description, time);
        databaseReference.child(reminderId).setValue(reminder).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(add_reminder_page.this, "Reminder saved", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(add_reminder_page.this, "Failed to save reminder", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void scheduleNotification(String title, String description, long time) {
        Intent intent = new Intent(this, ReminderBroadcastReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("description", description);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        }
    }
}