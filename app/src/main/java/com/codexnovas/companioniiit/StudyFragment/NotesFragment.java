package com.codexnovas.companioniiit.StudyFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codexnovas.companioniiit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends Fragment {
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;

    private FirebaseDatabase database;
    private DatabaseReference notesRef;

    private ProgressBar progressBar; // Add progress bar

    public NotesFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        categoryRecyclerView = view.findViewById(R.id.noteRecyclerView);
        progressBar = view.findViewById(R.id.progressBar); // Initialize progress bar

        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        categoryRecyclerView.setAdapter(categoryAdapter);

        database = FirebaseDatabase.getInstance();

        // Show the progress bar before fetching data
        progressBar.setVisibility(View.VISIBLE);
        categoryRecyclerView.setVisibility(View.GONE);

        fetchUserYearAndNotes(); // Fetch the user year and notes data

        return view;
    }

    private void fetchUserYearAndNotes() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = database.getReference().child("users").child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String userYear = snapshot.child("year").getValue(String.class);
                        if (userYear != null) {
                            notesRef = database.getReference().child(userYear).child("Notes").child("Notes Category");
                            fetchNotes();
                        } else {
                            Log.e("NotesFragment", "User year not found");
                            hideProgressBar(); // Hide progress bar on failure
                        }
                    } else {
                        Log.e("NotesFragment", "User data not found");
                        hideProgressBar(); // Hide progress bar on failure
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("NotesFragment", "Failed to read user data.", error.toException());
                    hideProgressBar(); // Hide progress bar on failure
                }
            });
        } else {
            Log.e("NotesFragment", "User is not logged in");
            hideProgressBar(); // Hide progress bar if no user logged in
        }
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
                                    String link = detailSnapshot.child("Note link").getValue(String.class);
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

                hideProgressBar(); // Hide progress bar when data is loaded
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("NotesFragment", "Failed to read value.", error.toException());
                hideProgressBar(); // Hide progress bar on failure
            }
        });
    }

    // Method to hide progress bar and show the recycler view
    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        categoryRecyclerView.setVisibility(View.VISIBLE);
    }
}