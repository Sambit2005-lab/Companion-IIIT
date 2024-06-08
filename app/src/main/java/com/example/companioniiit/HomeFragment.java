package com.example.companioniiit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private TextView userIdTextView;
    private TextView greetingsTextView;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private String userId;

    private AppCompatButton attendenceButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        userIdTextView = view.findViewById(R.id.userid);
        greetingsTextView = view.findViewById(R.id.greetings_user);
        attendenceButton = view.findViewById(R.id.attendance_card);  // Initialize the button

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

        // Always show the dialog box
        showCollegeIdDialog();

        // Retrieve the user's name from Firebase
        getUserNameFromFirebase();

        onAttendenceClick();

        return view;
    }

    private void onAttendenceClick() {
        attendenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the AttendenceActivity
                Intent intent = new Intent(getActivity(), attendance_card.class);
                startActivity(intent);
            }
        });
    }

    private void showCollegeIdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_input, null);
        builder.setView(dialogView)
                .setTitle("Please enter your College ID")
                .setPositiveButton("Submit", (dialog, which) -> {
                    EditText editTextCollegeId = dialogView.findViewById(R.id.editTextCollegeId);
                    String collegeId = editTextCollegeId.getText().toString();

                    if (!collegeId.isEmpty()) {
                        userIdTextView.setText("ID: " + collegeId);
                        saveCollegeIdToFirebase(collegeId);
                    }
                })
                .setNeutralButton("Skip", (dialog, which) -> {
                    // Retrieve the college ID from Firebase
                    getCollegeIdFromFirebase();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void saveCollegeIdToFirebase(String collegeId) {
        databaseReference.child("collegeId").setValue(collegeId);
    }

    private void getCollegeIdFromFirebase() {
        databaseReference.child("collegeId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String collegeId = snapshot.getValue(String.class);
                    userIdTextView.setText("ID: " + collegeId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });
    }

    private void getUserNameFromFirebase() {
        databaseReference.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.getValue(String.class);
                    greetingsTextView.setText("Welcome, " + name + "!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });
    }
}
