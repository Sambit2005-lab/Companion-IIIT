package com.codexnovas.companioniiit.attendance;

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

import com.codexnovas.companioniiit.MainActivity;
import com.codexnovas.companioniiit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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
        DatabaseReference subjectsRef = databaseReference;

        subjectsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<SubjectWithKey> newSubjectItems = new ArrayList<>(); // Temporary list to avoid modifying subject_items directly
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    Subject_Item subjectItem = snapshot.getValue(Subject_Item.class);
                    if (key != null && subjectItem != null) {
                        boolean subjectExists = false;
                        for (SubjectWithKey item : newSubjectItems) {
                            if (item.getSubjectItem().getSubjectName().equalsIgnoreCase(subjectItem.getSubjectName()) &&
                                    item.getSubjectItem().getTeacherName().equalsIgnoreCase(subjectItem.getTeacherName())) {
                                subjectExists = true;
                                break;
                            }
                        }
                        if (!subjectExists) {
                            newSubjectItems.add(new SubjectWithKey(key, subjectItem));
                        }
                    }
                }
                subject_items.clear(); // Clear the old list
                subject_items.addAll(newSubjectItems); // Add the new, filtered list
                subjectAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(attendance_card.this, "Failed to load subjects", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void gotonewactivity(int position) {
        if (position >= 0 && position < subject_items.size()) {
            Intent intent = new Intent(this, student_attendance_activity.class);
            intent.putExtra("subject_key", subject_items.get(position).getKey()); // Pass the subject key
            intent.putExtra("subject_name", subject_items.get(position).getSubjectItem().getSubjectName());
            intent.putExtra("teacher_name", subject_items.get(position).getSubjectItem().getTeacherName());
            intent.putExtra("position", position);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Invalid subject position", Toast.LENGTH_SHORT).show();
        }
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

    private void saveClass() {
        String subject_name_text = subject_name.getText().toString();
        String teacher_name_text = teacher_name.getText().toString();
        if (!subject_name_text.isEmpty() && !teacher_name_text.isEmpty()) {
            DatabaseReference subjectsRef = databaseReference;
            // Check if the subject already exists
            boolean subjectExists = false;
            String existingKey = null;
            for (SubjectWithKey subjectWithKey : subject_items) {
                if (subjectWithKey.getSubjectItem().getSubjectName().equalsIgnoreCase(subject_name_text) &&
                        subjectWithKey.getSubjectItem().getTeacherName().equalsIgnoreCase(teacher_name_text)) {
                    subjectExists = true;
                    existingKey = subjectWithKey.getKey();
                    break;
                }
            }
            if (!subjectExists) {
                String key = subjectsRef.push().getKey();
                Subject_Item subjectItem = new Subject_Item(subject_name_text, teacher_name_text);
                if (key != null) {
                    // Save the subject item with the generated key
                    subjectsRef.child(key).setValue(subjectItem)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(attendance_card.this, "Class saved successfully", Toast.LENGTH_SHORT).show();
                                    subject_items.add(new SubjectWithKey(key, subjectItem)); // Add SubjectWithKey object
                                    subjectAdapter.notifyDataSetChanged(); // Notify adapter of data change
                                } else {
                                    Toast.makeText(attendance_card.this, "Failed to save class", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            } else {
                Toast.makeText(attendance_card.this, "Subject already exists", Toast.LENGTH_SHORT).show();
            }

            // Remove existing duplicate subject, if any
            if (existingKey != null) {
                subjectsRef.child(existingKey).removeValue(); // Remove the existing duplicate subject from Firebase
                // Remove the existing duplicate subject from the local list
                for (SubjectWithKey subjectWithKey : subject_items) {
                    if (subjectWithKey.getKey().equals(existingKey)) {
                        subject_items.remove(subjectWithKey);
                        break;
                    }
                }
            }
        }
    }



    private void showEditDeleteDialog(int position) {
        // Check if the position is valid
        if (position < 0 || position >= subject_items.size()) {
            Toast.makeText(this, "Invalid subject position", Toast.LENGTH_SHORT).show();
            return;
        }

        // Build and show the dialog
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
        Subject_Item currentItem = subject_items.get(position).getSubjectItem();
        editSubjectName.setText(currentItem.getSubjectName());
        editTeacherName.setText(currentItem.getTeacherName());

        updateButton.setOnClickListener(v -> {
            String newSubjectName = editSubjectName.getText().toString().trim();
            String newTeacherName = editTeacherName.getText().toString().trim();

            if (!newSubjectName.isEmpty() && !newTeacherName.isEmpty()) {
                String key = subject_items.get(position).getKey();
                DatabaseReference subjectRef = databaseReference.child(key); // Reference to the specific subject node

                Subject_Item updatedSubject = new Subject_Item(newSubjectName, newTeacherName);

                subjectRef.setValue(updatedSubject)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                subject_items.set(position, new SubjectWithKey(key, updatedSubject));
                                subjectAdapter.notifyItemChanged(position);
                                dialog.dismiss();
                                Toast.makeText(attendance_card.this, "Class updated successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(attendance_card.this, "Failed to update class", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        deleteButton.setOnClickListener(v -> {
            String key = subject_items.get(position).getKey();
            DatabaseReference subjectRef = databaseReference.child(key); // Reference to the specific subject node

            subjectRef.removeValue()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Delete related attendance data
                            DatabaseReference attendanceRef = FirebaseDatabase.getInstance().getReference("users")
                                    .child(currentUserId)
                                    .child(key).child("attendance"); // Reference to the attendance data for the subject

                            attendanceRef.removeValue()
                                    .addOnCompleteListener(attendanceTask -> {
                                        if (attendanceTask.isSuccessful()) {
                                            if (position >= 0 && position < subject_items.size()) {
                                                subject_items.remove(position);
                                                subjectAdapter.notifyItemRemoved(position);
                                            }
                                            dialog.dismiss();
                                            Toast.makeText(attendance_card.this, "Class deleted successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(attendance_card.this, "Failed to delete attendance data", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(attendance_card.this, "Failed to delete class", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}