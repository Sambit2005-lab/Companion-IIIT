package com.codexnovas.companioniiit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.HashMap;
import java.util.Map;

public class interest extends AppCompatActivity {

    private TextView nameTextView;
    private TextView yearTextView;
    private TextView branchTextView;
    private ShapeableImageView profilePic;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ImageButton nextButton;
    private ImageButton backButton;
    private FirebaseUser currentUser;
    private Map<String, Button> buttonMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize TextViews and ShapeableImageView
        nameTextView = findViewById(R.id.name);
        yearTextView = findViewById(R.id.year);
        branchTextView = findViewById(R.id.branch);
        profilePic = findViewById(R.id.profile_pic);
        nextButton = findViewById(R.id.next);
        backButton = findViewById(R.id.back);

        // Retrieve and display user details
        displayUserDetails();
        loadProfileImage();

        // Initialize buttons and add them to the map
        initButton(R.id.technology, "Technology");
        initButton(R.id.sports, "Sports");
        initButton(R.id.cybersecurity, "Cyber Security");
        initButton(R.id.music, "Music");
        initButton(R.id.acting, "Acting");
        initButton(R.id.photography, "Photography");
        initButton(R.id.writing, "Writing");
        initButton(R.id.art, "Art");
        initButton(R.id.socialcause, "Social Cause");
        initButton(R.id.dance, "Dance");
        initButton(R.id.enterpreneurship, "Enterpreneurship");
        initButton(R.id.filmmedia, "Film Media");
        initButton(R.id.coding, "Coding");
        initButton(R.id.marketing, "Marketing");
        initButton(R.id.event, "Event Management");
        initButton(R.id.career, "Career Development");
        initButton(R.id.literature, "Literature");

        // Add other buttons similarly...
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(interest.this, Loading_page.class);
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

    private void loadProfileImage() {
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference profileImageRef = mDatabase.child("users").child(userId).child("profileImage");

            profileImageRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String profileImageUrl = dataSnapshot.getValue(String.class);
                        Glide.with(interest.this)
                                .load(profileImageUrl)
                                .placeholder(R.drawable.plswait) // Optional placeholder
                                .circleCrop()
                                .into(profilePic);
                    } else {
                        Toast.makeText(interest.this, "Profile image not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(interest.this, "Failed to load profile image: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(interest.this, "User not logged in", Toast.LENGTH_SHORT).show();
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