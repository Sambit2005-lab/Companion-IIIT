package com.example.companioniiit;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

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
    private AppCompatImageButton backButton;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    private DatabaseReference hostsRef;

    private TextView forgotPasswordTextView;

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
        backButton = findViewById(R.id.back_button);
        progressBar = findViewById(R.id.progressBar);
        forgotPasswordTextView=findViewById(R.id.forgot_password);


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

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotPasswordDialog();
            }
        });
    }

    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Password");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setHint("Enter your email"); // Set hint for the email input
        builder.setView(input);


        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = input.getText().toString().trim();
                if (!email.isEmpty()) {
                    resetPassword(email);
                } else {
                    Toast.makeText(login.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void resetPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(login.this, "Reset link sent to your email", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(login.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
                        Intent intent = new Intent(login.this, Loading_page.class); // Redirect to main activity
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(login.this, "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}