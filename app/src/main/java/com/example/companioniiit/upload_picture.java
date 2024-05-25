package com.example.companioniiit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class upload_picture extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 1;
    private AppCompatButton upload_picture_btn;
    private AppCompatButton skip_btn;
    private  AppCompatButton backButton;
    private ImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_picture);

        upload_picture_btn = findViewById(R.id.upload_now);
        skip_btn = findViewById(R.id.skip_for_now);
        profile_image = findViewById(R.id.pfp_preview);
        backButton = findViewById(R.id.back);

        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(upload_picture.this, interest.class);
                startActivity(intent);
            }
        });

        upload_picture_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile_image = new Intent(Intent.ACTION_PICK);
                profile_image.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(profile_image, PICK_IMAGE_REQUEST);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(upload_picture.this, welcome.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri imageUri = data.getData();
            profile_image.setImageURI(imageUri);

        }
    }}