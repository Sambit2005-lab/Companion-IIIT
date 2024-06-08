package com.example.companioniiit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class attendance_card extends AppCompatActivity {

    private Button save;
    private Button cancel;

    private EditText subject_name;
    private EditText teacher_name;

    private RecyclerView recyclerView;
    private SubjectAdapter subjectAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Subject_Item> subject_items = new ArrayList<>();

    private AppCompatImageButton fab;

    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_card);

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


        //back button created
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }



    private void gotonewactivity(int position) {
        Intent intent = new Intent(this, student_attendance_activity.class);
        intent.putExtra("subject_name", subject_items.get(position).getSubjectName());
        intent.putExtra("teacher_name", subject_items.get(position).getTeacherName());
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
            subject_items.add(new Subject_Item(subject_name_text, teacher_name_text));
            subjectAdapter.notifyDataSetChanged();
        }
    }


}
