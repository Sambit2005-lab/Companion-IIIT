package com.example.companioniiit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private AppCompatButton loginButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();


        emailEditText = findViewById(R.id.edittext1);
        passwordEditText = findViewById(R.id.edittext2);
        loginButton = findViewById(R.id.loginbutton);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
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

        // Proceed with login logic
        signIn(email, password);
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        Toast.makeText(login.this, "Login successful", Toast.LENGTH_SHORT).show();
                        // Redirect to another activity if needed
                         Intent intent = new Intent(login.this, MainActivity.class);
                         startActivity(intent);
                         finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(login.this, "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
