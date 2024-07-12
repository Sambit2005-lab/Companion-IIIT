package com.codexnovas.companioniiit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class info_year extends AppCompatActivity {

    private TextView welcomeTextView;
    private AutoCompleteTextView autoCompleteTextViewCourse, autoCompleteTextViewYear;
    private ArrayAdapter<String> adapterItemsCourse, adapterItemsYear;

    private String selectedCourse, selectedYear;
    private AppCompatImageButton nextButton;
    private AppCompatImageButton backButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    String[] Courses = {"CSE", "IT", "CE", "ETC", "EEE"};
    String[] Year = {"1st year", "2nd year", "3rd year", "4th year"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_year);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        autoCompleteTextViewCourse = findViewById(R.id.autocomplete_text_course);
        autoCompleteTextViewYear = findViewById(R.id.autocomplete_text_year);
        backButton = findViewById(R.id.back);
        nextButton = findViewById(R.id.info_next);

        adapterItemsCourse = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Courses);
        autoCompleteTextViewCourse.setAdapter(adapterItemsCourse);

        adapterItemsYear = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Year);
        autoCompleteTextViewYear.setAdapter(adapterItemsYear);

        autoCompleteTextViewCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCourse = parent.getItemAtPosition(position).toString();
            }
        });

        autoCompleteTextViewYear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedYear = parent.getItemAtPosition(position).toString();
            }
        });

        welcomeTextView = findViewById(R.id.textView2);

        // Retrieve the username from the intent
        String userName = getIntent().getStringExtra("userName");

        // Set the welcome message with the username
        welcomeTextView.setText("Hey " + userName + "!");

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserDetailsAndProceed();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(info_year.this, login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void saveUserDetailsAndProceed() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && selectedCourse != null && selectedYear != null) {
            String userId = currentUser.getUid();

            // Update the user's course and year
            mDatabase.child("users").child(userId).child("course").setValue(selectedCourse);
            mDatabase.child("users").child(userId).child("year").setValue(selectedYear)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(info_year.this, "Details saved successfully", Toast.LENGTH_SHORT).show();
                            // Navigate to the next activity only if details are saved successfully
                            Intent intent = new Intent(info_year.this, upload_picture.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(info_year.this, "Failed to save details: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Please select both course and year", Toast.LENGTH_SHORT).show();
        }
    }
}










