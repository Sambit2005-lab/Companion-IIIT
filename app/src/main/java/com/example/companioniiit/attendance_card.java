package com.example.companioniiit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class attendance_card extends AppCompatActivity {

    Button save;
    Button cancel;

    EditText subject_name;
    EditText teacher_name;

    RecyclerView recyclerView;
    SubjectAdapter subjectAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Subject_Item> subject_items=new ArrayList<>();

    AppCompatImageButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_card);

        fab=findViewById(R.id.add_subject_fab);
        fab.setOnClickListener(v -> showDialog());

        recyclerView=findViewById(R.id.recyclerView_class);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        subjectAdapter=new SubjectAdapter(this,subject_items);
        recyclerView.setAdapter(subjectAdapter);
    }

    @SuppressLint("WrongViewCast")
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.class_dialog, null);
        builder.setView(view);
        AlertDialog dialog= builder.create();
        dialog.show();

        subject_name=view.findViewById(R.id.class_input_field);
        teacher_name=view.findViewById(R.id.teacher_input_field);

        save=view.findViewById(R.id.save_class_details_button);
        cancel=view.findViewById(R.id.cancel_class_details_button);

        cancel.setOnClickListener(v -> dialog.dismiss());
        save.setOnClickListener(v ->saveClass());
    }

    @SuppressLint("NotifyDataSetChanged")
    private void saveClass() {
        String subject_name_text=subject_name.getText().toString();
        String teacher_name_text=teacher_name.getText().toString();
        subject_items.add(new Subject_Item(subject_name_text,teacher_name_text));
        subjectAdapter.notifyDataSetChanged();
    }
}