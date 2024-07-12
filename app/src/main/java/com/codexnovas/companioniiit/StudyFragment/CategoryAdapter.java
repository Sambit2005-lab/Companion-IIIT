package com.codexnovas.companioniiit.StudyFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codexnovas.companioniiit.R;


import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private Context context;
    private List<Category> categoryList;


    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;

    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_home, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.categoryTitle.setText(category.getTitle());

        NoteAdapter noteAdapter = new NoteAdapter(context, category.getNoteList());
        holder.notesRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.notesRecyclerView.setAdapter(noteAdapter);


        // Add horizontal space item decoration only once
        if (holder.notesRecyclerView.getItemDecorationCount() == 0) {
            int spacing = context.getResources().getDimensionPixelSize(R.dimen.horizontal_spacing); // define this in your resources
            holder.notesRecyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(spacing));
        }

    }







    @Override
    public int getItemCount() {
        return categoryList.size();
    }







    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTitle;
        RecyclerView notesRecyclerView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.CategoryTitle);
            notesRecyclerView = itemView.findViewById(R.id.cardHomeRecyclerView);
        }
    }
}
