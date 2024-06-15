package com.example.companioniiit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private AppCompatButton loginButton;
    private AppCompatButton backButton;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    private DatabaseReference hostsRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        hostsRef = FirebaseDatabase.getInstance().getReference("hosts");

        emailEditText = findViewById(R.id.edittext1);
        passwordEditText = findViewById(R.id.edittext2);
        loginButton = findViewById(R.id.loginbutton);
        backButton = findViewById(R.id.button1);
        progressBar = findViewById(R.id.progressBar);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, welcome.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void handleLogin() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Basic validation
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(login.this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the user is a host
        checkHostCredentials(email, password);
    }

    private void checkHostCredentials(final String email, final String password) {
        progressBar.setVisibility(View.VISIBLE);
        hostsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                boolean hostFound = false;

                for (DataSnapshot hostSnapshot : dataSnapshot.getChildren()) {
                    if (hostSnapshot != null) {
                        String hostEmail = hostSnapshot.child("email").getValue(String.class);
                        String hostPassword = hostSnapshot.child("password").getValue(String.class);
                        String activityClassName = hostSnapshot.child("activity").getValue(String.class);

                        if (hostEmail != null && hostEmail.equals(email) && hostPassword != null && hostPassword.equals(password)) {
                            hostFound = true;
                            Toast.makeText(login.this, "Host login successful", Toast.LENGTH_SHORT).show();
                            try {
                                Class<?> activityClass = Class.forName(activityClassName);
                                Intent intent = new Intent(login.this, activityClass);
                                startActivity(intent);
                                finish();
                            } catch (ClassNotFoundException e) {
                                Toast.makeText(login.this, "Host activity not found: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                    }
                }

                if (!hostFound) {
                    signIn(email, password); // Proceed with normal user login
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(login.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signIn(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(login.this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(login.this, MainActivity.class); // Redirect to main activity
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(login.this, "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}