package com.example.companioniiit.HomeFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;


import com.example.companioniiit.Announcement.AnnouncementActivity;
import com.example.companioniiit.R;
import com.example.companioniiit.MyCalendar.activity_myCalender_card;
import com.example.companioniiit.attendance.attendance_card;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView userIdTextView;
    private TextView greetingsTextView;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private String userId;


    private CardView attendenceButton;
    private CardView myCalenderButton;

    private CardView AnnouncementBtn;

    private boolean newAnnouncementFlag = false;
    private DatabaseReference hostAnnouncementsRef;
    private ValueEventListener announcementsListener;


    private View view;
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        userIdTextView = view.findViewById(R.id.userid);
        greetingsTextView = view.findViewById(R.id.greetings_user);
        attendenceButton = view.findViewById(R.id.attendance_card); // Initialize the button
        myCalenderButton = view.findViewById(R.id.myCalendar_card);
        AnnouncementBtn = view.findViewById(R.id.announcement_card);

        // Initialize ImageSlider
        ImageSlider imageSlider = view.findViewById(R.id.imageSlider);


        // Creating a list of SlideModel
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.iiitphoto1, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.iiitphoto2, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.iiitphoto3, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.iiitphoto4, ScaleTypes.CENTER_CROP));


        // Add images to the ImageSlider
        imageSlider.setImageList(slideModels);



        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

        // Check if the College ID is already saved in Firebase
        checkIfCollegeIdExists();

        // Retrieve the user's name from Firebase
        getUserNameFromFirebase();

        onAttendenceClick();

        onMyCalenderClick();

        onAnnouncementClick();


        return view;
    }



    private void onAnnouncementClick() {
        AnnouncementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the AnnouncementActivity
                Intent intent = new Intent(getActivity(), AnnouncementActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onMyCalenderClick() {
        myCalenderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the MyCalenderActivity
                Intent intent = new Intent(getActivity(), activity_myCalender_card.class);
                startActivity(intent);
            }
        });

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

    private void checkIfCollegeIdExists() {
        databaseReference.child("collegeId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // College ID exists, retrieve and display it
                    String collegeId = snapshot.getValue(String.class);
                    userIdTextView.setText("ID: " + collegeId);
                } else {
                    // College ID does not exist, show the dialog
                    showCollegeIdDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
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
                    // Do nothing, user chose to skip
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void saveCollegeIdToFirebase(String collegeId) {
        databaseReference.child("collegeId").setValue(collegeId);
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

