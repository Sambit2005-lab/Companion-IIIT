package com.example.companioniiit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import com.example.companioniiit.Subject_Item;
import com.example.companioniiit.SubjectWithKey;
import com.example.companioniiit.SubjectAdapter;


public class attendance_card extends AppCompatActivity {

    private Button save;
    private Button cancel;
    private EditText subject_name;
    private EditText teacher_name;
    private RecyclerView recyclerView;
    private SubjectAdapter subjectAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<SubjectWithKey> subject_items = new ArrayList<>();
    private AppCompatImageButton fab;
    private ImageButton back;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_card);

        // Initialize Firebase reference
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        } else {
            // Handle the case where the user is not logged in
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(currentUserId).child("subjects");

        fab = findViewById(R.id.add_subject_fab);
        back = findViewById(R.id.back_button);
        fab.setOnClickListener(v -> showDialog());

        recyclerView = findViewById(R.id.recyclerView_class);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        subjectAdapter = new SubjectAdapter(this, subject_items);
        recyclerView.setAdapter(subjectAdapter);
        subjectAdapter.setOnItemClickListener(position -> gotonewactivity(position));

        // Load data from Firebase
        loadSubjectsFromFirebase();

        // Back button click listener

        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        // Handle long press for renaming or deleting subject
        subjectAdapter.setOnItemLongClickListener(position -> showEditDeleteDialog(position));
    }

    private void loadSubjectsFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subject_items.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    Subject_Item subjectItem = snapshot.getValue(Subject_Item.class);
                    if (key != null && subjectItem != null) {
                        subject_items.add(new SubjectWithKey(key, subjectItem));
                    }
                }
                subjectAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
                Toast.makeText(attendance_card.this, "Failed to load subjects", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void gotonewactivity(int position) {
        Intent intent = new Intent(this, student_attendance_activity.class);
        intent.putExtra("subject_name", subject_items.get(position).getSubjectItem().getSubjectName());
        intent.putExtra("teacher_name", subject_items.get(position).getSubjectItem().getTeacherName());
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @SuppressLint("InflateParams")
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.class_dialog, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        subject_name = view.findViewById(R.id.class_input_field);
        teacher_name = view.findViewById(R.id.teacher_input_field);

        save = view.findViewById(R.id.save_class_details_button);
        cancel = view.findViewById(R.id.cancel_class_details_button);

        cancel.setOnClickListener(v -> dialog.dismiss());
        save.setOnClickListener(v -> {
            saveClass();
            dialog.dismiss(); // Dismiss the dialog after saving the class
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void saveClass() {
        String subject_name_text = subject_name.getText().toString();
        String teacher_name_text = teacher_name.getText().toString();
        if (!subject_name_text.isEmpty() && !teacher_name_text.isEmpty()) {
            String key = databaseReference.push().getKey();
            Subject_Item subjectItem = new Subject_Item(subject_name_text, teacher_name_text);
            if (key != null) {
                databaseReference.child(key).setValue(subjectItem)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(attendance_card.this, "Class saved successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(attendance_card.this, "Failed to save class", Toast.LENGTH_SHORT).show();
                            }
                        });
                subject_items.add(new SubjectWithKey(key, subjectItem));
                subjectAdapter.notifyDataSetChanged();
            }
        }
    }

    @SuppressLint("InflateParams")
    private void showEditDeleteDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.edit_delete_dialog, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        EditText editSubjectName = view.findViewById(R.id.edit_subject_name);
        EditText editTeacherName = view.findViewById(R.id.edit_teacher_name);
        Button updateButton = view.findViewById(R.id.update_button);
        Button deleteButton = view.findViewById(R.id.delete_button);

        // Pre-fill with existing subject data
        editSubjectName.setText(subject_items.get(position).getSubjectItem().getSubjectName());
        editTeacherName.setText(subject_items.get(position).getSubjectItem().getTeacherName());

        updateButton.setOnClickListener(v -> {
            String newSubjectName = editSubjectName.getText().toString();
            String newTeacherName = editTeacherName.getText().toString();

            if (!newSubjectName.isEmpty() && !newTeacherName.isEmpty()) {
                Subject_Item updatedSubject = new Subject_Item(newSubjectName, newTeacherName);
                String key = subject_items.get(position).getKey();

                if (key != null) {
                    databaseReference.child(key).setValue(updatedSubject)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    subject_items.set(position, new SubjectWithKey(key, updatedSubject));
                                    subjectAdapter.notifyDataSetChanged();
                                    Toast.makeText(attendance_card.this, "Class updated successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(attendance_card.this, "Failed to update class", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
            dialog.dismiss();
        });

        deleteButton.setOnClickListener(v -> {
            if (!subject_items.isEmpty() && position >= 0 && position < subject_items.size()) {
                String key = subject_items.get(position).getKey();
                if (key != null) {
                    databaseReference.child(key).removeValue()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    subjectAdapter.removeItem(position);
                                    Toast.makeText(attendance_card.this, "Class deleted successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(attendance_card.this, "Failed to delete class", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            } else {
                Toast.makeText(attendance_card.this, "Invalid position or list is empty", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });


    }





}






