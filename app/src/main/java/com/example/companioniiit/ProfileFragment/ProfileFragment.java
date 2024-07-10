package com.example.companioniiit.ProfileFragment;


import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.companioniiit.R;
import com.example.companioniiit.change_your_password;
import com.example.companioniiit.edit_your_profile;
import com.example.companioniiit.login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private TextView nameTextView, branchTextView, yearTextView, studentIdTextView;
    private ImageView profileImageView;
    private AppCompatButton editProfileButton, changePasswordButton, myReportsButton, logoutButton;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize Firebase Auth, Database, and Storage
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        firebaseStorage = FirebaseStorage.getInstance();

        // Initialize UI elements
        nameTextView = view.findViewById(R.id.name);
        branchTextView = view.findViewById(R.id.branch);
        yearTextView = view.findViewById(R.id.year);
        studentIdTextView = view.findViewById(R.id.student_id);
        profileImageView = view.findViewById(R.id.profile_pic);
        editProfileButton = view.findViewById(R.id.edit_profile);
        changePasswordButton = view.findViewById(R.id.change_password);
        myReportsButton = view.findViewById(R.id.my_reports);
        logoutButton = view.findViewById(R.id.logout);

        // Load profile information
        loadProfileInfo();

        // Set button listeners
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), edit_your_profile.class);
                startActivity(intent);

            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotPasswordDialog();
            }
        });


        myReportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getActivity(), login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Reset Password");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setHint("Enter your email"); // Set hint for the email input
        builder.setView(input);


        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = input.getText().toString().trim();
                if (!email.isEmpty()) {
                    resetPassword(email);
                } else {
                    Toast.makeText(getActivity(), "Please enter your email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void resetPassword(String email) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Reset link sent to your email", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

        private void loadProfileInfo () {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String name = dataSnapshot.child("name").getValue(String.class);
                            String branch = dataSnapshot.child("course").getValue(String.class);
                            String year = dataSnapshot.child("year").getValue(String.class);
                            String studentId = dataSnapshot.child("collegeId").getValue(String.class);
                            String profileImageUrl = dataSnapshot.child("profileImage").getValue(String.class);

                            nameTextView.setText(name);
                            branchTextView.setText(branch);
                            yearTextView.setText(year);
                            studentIdTextView.setText(studentId);

                            if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                                Glide.with(ProfileFragment.this).load(profileImageUrl).into(profileImageView);
                            } else {
                                profileImageView.setImageResource(R.drawable.profile_icon); // Set a default image if needed
                            }
                        } else {
                            Toast.makeText(getActivity(), "Profile data not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "Failed to load profile data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }


