package com.example.companioniiit.StudyFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.companioniiit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class PyqFragment extends Fragment {
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;

    private FirebaseDatabase database;
    private DatabaseReference notesRef;

    public PyqFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pyq, container, false);

        categoryRecyclerView = view.findViewById(R.id.noteRecyclerView);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        categoryRecyclerView.setAdapter(categoryAdapter);

        database = FirebaseDatabase.getInstance();
        String userYear = "1st year"; // Get this value based on your current user
        notesRef = database.getReference().child(userYear).child("Pyqs").child("Pyqs Category");

        fetchNotes();

        return view;
    }

    private void fetchNotes() {
        notesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    String categoryTitle = categorySnapshot.getKey();
                    List<Note> noteList = new ArrayList<>();
                    for (DataSnapshot noteSnapshot : categorySnapshot.getChildren()) {
                        if (noteSnapshot.exists()) {
                            for (DataSnapshot detailSnapshot : noteSnapshot.getChildren()) {
                                if (detailSnapshot.exists()) {
                                    String title = detailSnapshot.getKey();
                                    String link = detailSnapshot.child("Pyq link").getValue(String.class);
                                    Note note = new Note(title, link);
                                    noteList.add(note);
                                }
                            }
                        }
                    }
                    Category category = new Category(categoryTitle, noteList);
                    categoryList.add(category);
                }
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("NotesFragment", "Failed to read value.", error.toException());
                // Handle the error here, e.g., show a toast or log a message
            }
        });
    }}