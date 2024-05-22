package com.example.companioniiit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class welcome extends AppCompatActivity {
    Button btn1,btn2;

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
    }}