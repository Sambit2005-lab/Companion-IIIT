package com.example.companioniiit;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(3000);

                }
                catch (Exception e){
                    e.printStackTrace();

                }
                finally {
                    Intent isplash=new Intent(splash.this, welcome.class);
                    startActivity(isplash);

                }
            }
        };thread.start();


    }
}
