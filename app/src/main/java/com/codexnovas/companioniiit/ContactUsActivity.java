
package com.codexnovas.companioniiit;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;


import com.codexnovas.companioniiit.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContactUsActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize UI elements
        EditText nameEditText = findViewById(R.id.name);
        EditText emailEditText = findViewById(R.id.email);
        EditText phoneEditText = findViewById(R.id.phone);
        EditText messageEditText = findViewById(R.id.message);
         CheckBox agreeCheckBox = findViewById(R.id.agree_checkbox);
        AppCompatButton sendButton = findViewById(R.id.send_message_button);

        // Handle form submission
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String message = messageEditText.getText().toString();

                if (agreeCheckBox.isChecked()) {
                    submitContactUsForm(name, email, phone, message);
                } else {
                    Toast.makeText(ContactUsActivity.this, "Please agree to the terms", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void submitContactUsForm(String name, String email, String phone, String message) {
        // Create a new contact entry
        ContactUs contactUs = new ContactUs(name, email, phone, message);

        // Get a unique key for the new contact
        String contactId = mDatabase.child("contact_us").push().getKey();

        // Write the contact to the database
        if (contactId != null) {
            mDatabase.child("contact_us").child(contactId).setValue(contactUs)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ContactUsActivity.this, "Message sent successfully!", Toast.LENGTH_SHORT).show();
                            clearForm();
                        } else {
                            Toast.makeText(ContactUsActivity.this, "Failed to send message. Try again!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void clearForm() {
        EditText nameEditText = findViewById(R.id.name);
        EditText emailEditText = findViewById(R.id.email);
        EditText phoneEditText = findViewById(R.id.phone);
        EditText messageEditText = findViewById(R.id.message);
        CheckBox agreeCheckBox = findViewById(R.id.agree_checkbox);

        nameEditText.setText("");
        emailEditText.setText("");
        phoneEditText.setText("");
        messageEditText.setText("");
        agreeCheckBox.setChecked(false);
    }

    public static class ContactUs {
        private String name;
        private String email;
        private String phone;
        private String message;

        public ContactUs() {
            // Default constructor required for calls to DataSnapshot.getValue(ContactUs.class)
        }

        public ContactUs(String name, String email, String phone, String message) {
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.message = message;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
