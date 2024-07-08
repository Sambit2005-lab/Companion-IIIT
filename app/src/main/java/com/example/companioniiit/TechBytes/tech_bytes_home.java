package com.example.companioniiit.TechBytes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.companioniiit.MainActivity;
import com.example.companioniiit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class tech_bytes_home extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech_bytes_home);

        recyclerView = findViewById(R.id.recycler_view_announcements);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList);
        recyclerView.setAdapter(postAdapter);

        backBtn = findViewById(R.id.back_button);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(tech_bytes_home.this, MainActivity.class);
            startActivity(intent);
        });

        loadPostsFromFirebase();
    }

    private void loadPostsFromFirebase() {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("hosts").child("1").child("posts");

        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    if (post != null) {
                        post.setId(postSnapshot.getKey()); // Set the post ID
                        postList.add(post);
                    }
                }
                // Sort posts by timestamp in descending order
                Collections.sort(postList, new Comparator<Post>() {
                    @Override
                    public int compare(Post p1, Post p2) {
                        return Long.compare(p2.getTimestamp(), p1.getTimestamp());
                    }
                });
                postAdapter.setPostList(postList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });
    }
}
