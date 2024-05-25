package com.example.companioniiit;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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

       /*  mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
           //  User is signed in, redirect to mainactivity
            Intent intent = new Intent(welcome.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(welcome.this, "Hello!", Toast.LENGTH_SHORT).show();

       } */

        // we add the blue line under the terms and conditions and add the link to give wave view of term and conditions
        TextView termsTextView = findViewById(R.id.textview5);
        String text = "By logging in you will be accepting our Terms and Conditions.";

        SpannableString spannableString = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                // Open the Terms and Conditions link
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/document/d/1SIggIlQ5n1Il5-RpMVJGmogVvvwSH06p8W2wHuls9cA/edit?usp=sharing"));
                startActivity(browserIntent);
            }
        };

        int start = text.indexOf("Terms and Conditions");
        int end = start + "Terms and Conditions".length();

        // Set the clickable span
        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Set the underline span
        spannableString.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Set the color span
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        termsTextView.setText(spannableString);
        termsTextView.setMovementMethod(LinkMovementMethod.getInstance());





    }}