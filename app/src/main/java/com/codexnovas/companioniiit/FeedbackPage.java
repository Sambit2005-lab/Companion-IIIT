package com.codexnovas.companioniiit;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import com.codexnovas.companioniiit.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackPage extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private String selectedRating = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_page);

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize UI elements
        AppCompatButton publishButton = findViewById(R.id.publish_btn);
        EditText emailEditText = findViewById(R.id.email_text);
        EditText commentEditText = findViewById(R.id.say_something_text);

        AppCompatImageButton veryBadEmoji = findViewById(R.id.very_bad_emoji);
        AppCompatImageButton badEmoji = findViewById(R.id.bad_emoji);
        AppCompatImageButton neutralEmoji = findViewById(R.id.neutral_emoji);
        AppCompatImageButton goodEmoji = findViewById(R.id.good_emoji);
        AppCompatImageButton veryGoodEmoji = findViewById(R.id.very_good_emoji);

        // Set up rating selection
        veryBadEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRating = "very_bad";
            }
        });

        badEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRating = "bad";
            }
        });

        neutralEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRating = "neutral";
            }
        });

        goodEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRating = "good";
            }
        });

        veryGoodEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRating = "very_good";
            }
        });

        // Handle feedback submission
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String comment = commentEditText.getText().toString();

                submitFeedback(email, selectedRating, comment);
            }
        });
    }

    private void submitFeedback(String email, String rating, String comment) {
        // Create a new feedback entry
        Feedback feedback = new Feedback(email, rating, comment);

        // Get a unique key for the new feedback
        String feedbackId = mDatabase.child("feedback").push().getKey();

        // Write the feedback to the database
        mDatabase.child("feedback").child(feedbackId).setValue(feedback);

        // Show a toast message
        Toast.makeText(FeedbackPage.this, "Feedback submitted successfully!", Toast.LENGTH_SHORT).show();
    }

    public static class Feedback {
        public String email;
        public String rating;
        public String comment;

        public Feedback() {
            // Default constructor required for calls to DataSnapshot.getValue(Feedback.class)
        }

        public Feedback(String email, String rating, String comment) {
            this.email = email;
            this.rating = rating;
            this.comment = comment;
        }
    }
}
