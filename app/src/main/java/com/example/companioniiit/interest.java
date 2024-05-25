package com.example.companioniiit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class interest extends AppCompatActivity {

    private TextView nameTextView;
    private TextView yearTextView;
    private TextView branchTextView;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private AppCompatButton nextButton;
    private AppCompatButton backButton;
    private FirebaseUser currentUser;
    private Map<String, Button> buttonMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize TextViews
        nameTextView = findViewById(R.id.name);
        yearTextView = findViewById(R.id.year);
        branchTextView = findViewById(R.id.branch);
        nextButton = findViewById(R.id.next);
        backButton = findViewById(R.id.back);

        // Retrieve and display user details
        displayUserDetails();

        // Initialize buttons and add them to the map
        initButton(R.id.technology, "Technology");
        initButton(R.id.sports, "Sports");
        initButton(R.id.cybersecurity, "Cybersecurity");
        initButton(R.id.music, "Music");

        // Add other buttons similarly...
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(interest.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(interest.this, welcome.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void displayUserDetails() {
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Retrieve user details
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String year = dataSnapshot.child("year").getValue(String.class);
                        String branch = dataSnapshot.child("course").getValue(String.class);

                        // Display user details
                        nameTextView.setText(name);
                        yearTextView.setText(year);
                        branchTextView.setText("B.Tech in " + branch);
                    } else {
                        Toast.makeText(interest.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(interest.this, "Failed to retrieve user data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(interest.this, "User not logged in", Toast.LENGTH_SHORT).show();
            // Handle user not logged in (redirect to login activity, show message, etc.)
        }
    }

    private void initButton(int buttonId, final String interest) {
        final Button button = findViewById(buttonId);
        buttonMap.put(interest, button);

        button.setOnClickListener(v -> {
            boolean isSelected = toggleButton(button);
            if (isSelected) {
                // Add interest to Firebase
                mDatabase.child("users").child(currentUser.getUid()).child("interests").child(interest).setValue(true);
            } else {
                // Remove interest from Firebase
                mDatabase.child("users").child(currentUser.getUid()).child("interests").child(interest).removeValue();
            }
        });
    }

    private boolean toggleButton(Button button) {
        String text = button.getText().toString();
        if (text.endsWith(" +   ")) {
            button.setText(text.replace(" +   ", " x   "));
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSelected));
            button.setTextColor(ContextCompat.getColor(this, R.color.colorTextSelected));
            return true;
        } else {
            button.setText(text.replace(" x   ", " +   "));
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.colorUnselected));
            button.setTextColor(ContextCompat.getColor(this, R.color.colorTextUnselected));
            return false;
        }
    }
}



