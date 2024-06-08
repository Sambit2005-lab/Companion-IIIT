package com.example.companioniiit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {
    private Context context;
    private ArrayList<Subject_Item> subjectItems;
    private OnItemClickListener onItemClickListener;

    public SubjectAdapter(Context context, ArrayList<Subject_Item> subjectItems) {
        this.context = context;
        this.subjectItems = subjectItems;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_class_item, parent, false);
        return new SubjectViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject_Item currentItem = subjectItems.get(position);
        holder.subjectName.setText(currentItem.getSubjectName());
        holder.teacherName.setText(currentItem.getTeacherName());
    }

    @Override
    public int getItemCount() {
        return subjectItems.size();
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName;
        TextView teacherName;

        public SubjectViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subject_name);
            teacherName = itemView.findViewById(R.id.teacher_name);
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onClick(position);
                    }
                }
            });
        }
    }
}
