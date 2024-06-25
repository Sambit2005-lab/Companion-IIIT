package com.example.companioniiit;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Loading_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_page);

        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(6000);

                }
                catch (Exception e){
                    e.printStackTrace();

                }
                finally {
                    Intent isplash=new Intent(Loading_page.this, MainActivity.class);
                    startActivity(isplash);

                }
            }
        };thread.start();


    }
}

