package com.example.companioniiit.Host;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.companioniiit.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class hostside_sportsociety_societies_event_images extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private AppCompatButton uploadPictureBtn;
    private AppCompatButton saveImageBtn;
    private AppCompatButton cancelPostBtn;
    private ImageView uploadEventsImages;

    private Uri imageUri;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private DatabaseReference hostEventsRef;

    @SuppressLint("MissingInflatedId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostside_sportsociety_societies_event_images);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("hosts");
        mStorageRef = FirebaseStorage.getInstance().getReference("event_images");

        uploadPictureBtn = findViewById(R.id.upload_image_events_techsociety);
        saveImageBtn = findViewById(R.id.save_image_techsociety);
        cancelPostBtn = findViewById(R.id.cancel_post);
        uploadEventsImages = findViewById(R.id.upload_events_images);

        // Retrieve the email from the Intent
        Intent intent = getIntent();
        String hostEmail = intent.getStringExtra("hostEmail");

        if (hostEmail != null) {
            Log.d("HostsideSocieties", "Current host email: " + hostEmail); // Log the host email
            if (hostEmail.equals("hostsportsociety@gmail.com")) {
                hostEventsRef = mDatabase.child("11").child("eventImages");
            } else {
                Toast.makeText(this, "Invalid host email", Toast.LENGTH_SHORT).show();
                Log.e("HostsideSocieties", "Invalid host email: " + hostEmail);
                return;
            }
        } else {
            Toast.makeText(this, "Host email is null", Toast.LENGTH_SHORT).show();
            Log.e("HostsideSocieties", "Host email is null");
            return;
        }

        uploadPictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        saveImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImageToFirebase();
            }
        });

        cancelPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancel and return to the previous activity or clear the fields
                finish();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                uploadEventsImages.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();
                StorageReference fileReference = mStorageRef.child(userId + ".jpg");

                fileReference.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        saveImageUrlToDatabase(imageUrl);
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText( hostside_sportsociety_societies_event_images.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    private void saveImageUrlToDatabase(String imageUrl) {
        String imageId = hostEventsRef.push().getKey();

        if (imageId != null) {
            hostEventsRef.child(imageId).setValue(imageUrl)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText( hostside_sportsociety_societies_event_images.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                            finish(); // Close the activity
                        } else {
                            Toast.makeText( hostside_sportsociety_societies_event_images.this, "Failed to save image URL", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}