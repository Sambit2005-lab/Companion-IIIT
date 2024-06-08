package com.example.companioniiit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {
    private final Context context;
    ArrayList<Subject_Item> subjectItems;

    public SubjectAdapter(Context context, ArrayList<Subject_Item> subjectItems) {
        this.subjectItems = subjectItems;
        this.context = context;
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName;
        TextView teacherName;

        public SubjectViewHolder(@NonNull ViewGroup itemView) {

            super(itemView);
            subjectName = itemView.findViewById(R.id.subject_name);
            teacherName = itemView.findViewById(R.id.teacher_name);
        }
    }
    @NonNull
    @Override
    public SubjectAdapter.SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup itemView = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_class_item, parent, false);
        return new SubjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectAdapter.SubjectViewHolder holder, int position) {
        holder.subjectName.setText(subjectItems.get(position).getSubjectName());
        holder.teacherName.setText(subjectItems.get(position).getTeacherName());

    }

    @Override
    public int getItemCount() {
        return 0;
    }



}
