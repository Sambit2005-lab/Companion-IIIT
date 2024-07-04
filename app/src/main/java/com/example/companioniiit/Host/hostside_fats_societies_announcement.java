package com.example.companioniiit.Host;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.companioniiit.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class hostside_fats_societies_announcement extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView uploadImageView;
    private EditText captionEditText, postedByEditText;
    private Button uploadImageButton, savePostButton, cancelPostButton;
    private Uri imageUri;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private ProgressDialog progressDialog;

    private String hostEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostside_fats_societies_announcement);

        uploadImageView = findViewById(R.id.uplaod_image_for_announcement);
        captionEditText = findViewById(R.id.caption_announcement_techsociety);
        postedByEditText = findViewById(R.id.post_by_designation);
        uploadImageButton = findViewById(R.id.upload_image_announcement_techsociety);
        savePostButton = findViewById(R.id.save_post);
        cancelPostButton = findViewById(R.id.cancel_post);

        storageReference = FirebaseStorage.getInstance().getReference("announcements");
        databaseReference = FirebaseDatabase.getInstance().getReference("hosts");

        progressDialog = new ProgressDialog(this);

        // Retrieve the host email from intent
        hostEmail = getIntent().getStringExtra("host_email");

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        savePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadAnnouncement();
            }
        });

        cancelPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle cancel post logic
                finish();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadImageView.setImageURI(imageUri);
        }
    }

    private void uploadAnnouncement() {
        String caption = captionEditText.getText().toString().trim();
        String postedBy = postedByEditText.getText().toString().trim();

        if (!caption.isEmpty() && !postedBy.isEmpty()) {
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            if (imageUri != null) {
                StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");

                fileReference.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        saveAnnouncementData(imageUrl, caption, postedBy);
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText( hostside_fats_societies_announcement.this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                saveAnnouncementData(null, caption, postedBy);
            }
        } else {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveAnnouncementData(String imageUrl, String caption, String postedBy) {
        long timestamp = System.currentTimeMillis();

        Map<String, Object> announcementData = new HashMap<>();
        announcementData.put("imageUrl", imageUrl);
        announcementData.put("caption", caption);
        announcementData.put("postedBy", postedBy);
        announcementData.put("timestamp", timestamp);

        // Determine the host node based on the email
        DatabaseReference hostAnnouncementsRef;
        if (hostEmail.equals("fats@iiit-bh.ac.in")) {
            hostAnnouncementsRef = databaseReference.child("6").child("announcements");
        } else {
            Toast.makeText(this, "Invalid host email", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }

        String key = hostAnnouncementsRef.push().getKey();
        hostAnnouncementsRef.child(key).setValue(announcementData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText( hostside_fats_societies_announcement.this, "Announcement uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText( hostside_fats_societies_announcement.this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

