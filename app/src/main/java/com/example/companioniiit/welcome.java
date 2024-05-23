package com.example.companioniiit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class welcome extends AppCompatActivity {
    Button btn1,btn2;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btn1 = findViewById(R.id.signup_btn);
        btn2 = findViewById(R.id.login_btn);

        btn1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent;
                                        intent = new Intent(welcome.this, signup.class);
                                        startActivity(intent);
                                       

                                    }
                                });
        btn2.setOnClickListener(new View.OnClickListener(){

                public void onClick(View v) {
                    Intent intent;
                    intent = new Intent(welcome.this, login.class);
                    startActivity(intent);


                }
        });

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
           //  User is signed in, redirect to mainactivity
            Intent intent = new Intent(welcome.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(welcome.this, "Hello!", Toast.LENGTH_SHORT).show();

       }




    }}