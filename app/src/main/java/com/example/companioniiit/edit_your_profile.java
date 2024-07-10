package com.example.companioniiit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class edit_your_profile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView profileImageView;
    private EditText nameEditText, emailEditText, courseYearEditText, studentIdEditText;
    private Uri profileImageUri;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_your_profile);

        profileImageView = findViewById(R.id.profile_pic2);
        nameEditText = findViewById(R.id.change_name);
        emailEditText = findViewById(R.id.change_email);
        courseYearEditText = findViewById(R.id.change_course);
        studentIdEditText = findViewById(R.id.studentid);
        AppCompatButton saveButton = findViewById(R.id.save);
        AppCompatButton uploadButton = findViewById(R.id.upload_picture);
        AppCompatButton  removeButton = findViewById(R.id.remove_picture);
        ImageButton backButton = findViewById(R.id.imageButton);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        firebaseStorage = FirebaseStorage.getInstance();

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeProfileImage();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileInfo();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadProfileInfo();
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
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            profileImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), profileImageUri);
                profileImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeProfileImage() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            StorageReference profileImageRef = firebaseStorage.getReference("profile_images/" + user.getUid() + ".jpg");
            profileImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        profileImageView.setImageResource(R.drawable.profile_icon);
                        Toast.makeText(edit_your_profile.this, "Profile image removed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(edit_your_profile.this, "Failed to remove profile image: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void saveProfileInfo() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String course = courseYearEditText.getText().toString().trim();
        String studentId = studentIdEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(course) || TextUtils.isEmpty(studentId)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            Map<String, Object> userData = new HashMap<>();
            userData.put("name", name);
            userData.put("email", email);
            userData.put("year", course);
            userData.put("collegeId", studentId);

            databaseReference.child(userId).updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        if (profileImageUri != null) {
                            uploadProfileImage(userId);
                        } else {
                            Toast.makeText(edit_your_profile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(edit_your_profile.this, "Failed to update profile: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void uploadProfileImage(String userId) {
        StorageReference profileImageRef = firebaseStorage.getReference("profile_images/" + userId + ".jpg");
        profileImageRef.putFile(profileImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageUrl = uri.toString();
                        databaseReference.child(userId).child("profileImage").setValue(imageUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(edit_your_profile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(edit_your_profile.this, "Failed to update profile image URL: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(edit_your_profile.this, "Failed to upload profile image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProfileInfo() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String course = dataSnapshot.child("year").getValue(String.class);
                        String studentId = dataSnapshot.child("collegeId").getValue(String.class);
                        String profileImageUrl = dataSnapshot.child("profileImage").getValue(String.class);

                        nameEditText.setText(name);
                        emailEditText.setText(email);
                        courseYearEditText.setText(course);
                        studentIdEditText.setText(studentId);

                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            Glide.with(edit_your_profile.this).load(profileImageUrl).circleCrop().into(profileImageView);

                        } else {
                            profileImageView.setImageResource(R.drawable.profile_icon);
                        }
                    } else {
                        Toast.makeText(edit_your_profile.this, "Profile data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(edit_your_profile.this, "Failed to load profile data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
