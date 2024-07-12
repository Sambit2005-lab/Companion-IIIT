package com.codexnovas.companioniiit.Host;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.codexnovas.companioniiit.R;
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
import java.util.HashMap;
import java.util.Map;

public class hostside_photogeeks_societies_teammembers extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private AppCompatButton uploadImageBtn, saveMemberBtn, cancelBtn;
    private ImageView memberImageView;
    private EditText memberNameEditText, memberDesignationEditText;

    private Uri imageUri;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private DatabaseReference hostMembersRef;

    @SuppressLint("MissingInflatedId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostside_photogeeks_societies_teammembers);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("hosts");
        mStorageRef = FirebaseStorage.getInstance().getReference("member_images");

        uploadImageBtn = findViewById(R.id.uplaod_image_members_techsociety);
        saveMemberBtn = findViewById(R.id.save_member_techsociety);
        cancelBtn = findViewById(R.id.cancel_member_techsociety);
        memberImageView = findViewById(R.id.upload_members_images_techsociety);
        memberNameEditText = findViewById(R.id.member_name_techsociety);
        memberDesignationEditText = findViewById(R.id.member_designation_techsociety);

        // Retrieve the email from the Intent
        Intent intent = getIntent();
        String hostEmail = intent.getStringExtra("hostEmail");

        if (hostEmail != null) {
            Log.d("HostsideSocieties", "Current host email: " + hostEmail); // Log the host email
            if (hostEmail.equals("photogeeks1@iiit-bh.ac.in")) {
                hostMembersRef = mDatabase.child("10").child("teamMembers");
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

        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        saveMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImageAndSaveMember();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the activity
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
                memberImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void uploadImageAndSaveMember() {
        final String memberName = memberNameEditText.getText().toString().trim();
        final String memberDesignation = memberDesignationEditText.getText().toString().trim();

        if (memberName.isEmpty() || memberDesignation.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            StorageReference fileReference = mStorageRef.child(userId + "/" + memberName + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    saveMemberToDatabase(memberName, memberDesignation, imageUrl);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(hostside_photogeeks_societies_teammembers.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void saveMemberToDatabase(String name, String designation, String imageUrl) {
        String memberId = hostMembersRef.push().getKey();

        if (memberId != null) {
            Map<String, Object> memberData = new HashMap<>();
            memberData.put("name", name);
            memberData.put("designation", designation);
            memberData.put("imageUrl", imageUrl);

            hostMembersRef.child(memberId).setValue(memberData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(hostside_photogeeks_societies_teammembers.this, "Member added successfully", Toast.LENGTH_SHORT).show();
                            finish(); // Close the activity
                        } else {
                            Toast.makeText(hostside_photogeeks_societies_teammembers.this, "Failed to add member", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


}