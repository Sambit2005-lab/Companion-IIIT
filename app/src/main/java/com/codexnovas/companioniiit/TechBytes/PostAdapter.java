
package com.codexnovas.companioniiit.TechBytes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codexnovas.companioniiit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> postList;

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_tech_bytes_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.postedBy.setText(post.getPostedBy());
        holder.postCaption.setText(post.getCaption());
        holder.postedTime.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date(post.getTimestamp())));

        holder.likeCount.setText(String.valueOf(post.getLikeCount()));
        holder.smileyCount.setText(String.valueOf(post.getSmileyCount()));
        holder.thumbsUpCount.setText(String.valueOf(post.getThumbsUpCount()));

        // Load the image with Glide
        Glide.with(holder.postImage.getContext())
                .load(post.getImageUrl())
                .override(1000, 600) // resize to desired width and height
                .centerInside()
                // scale type
                .placeholder(R.drawable.plswait) // add a placeholder image
                .error(R.drawable.erroricon) // add an error image
                .into(holder.postImage);

        // Handle button clicks
        holder.likeButton.setOnClickListener(v -> updateReaction(holder, post, "likeCount"));
        holder.smileyButton.setOnClickListener(v -> updateReaction(holder, post, "smileyCount"));
        holder.thumbsUpButton.setOnClickListener(v -> updateReaction(holder, post, "thumbsUpCount"));
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
        notifyDataSetChanged();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView postedBy;
        TextView postedTime;
        TextView postCaption;
        ImageView postImage;
        ImageView likeButton;
        TextView likeCount;
        ImageView smileyButton;
        TextView smileyCount;
        ImageView thumbsUpButton;
        TextView thumbsUpCount;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            postedBy = itemView.findViewById(R.id.posted_by);
            postedTime = itemView.findViewById(R.id.posted_time);
            postCaption = itemView.findViewById(R.id.post_caption);
            postImage = itemView.findViewById(R.id.post_image);
            likeButton = itemView.findViewById(R.id.like_button);
            likeCount = itemView.findViewById(R.id.like_count);
            smileyButton = itemView.findViewById(R.id.smiley_button);
            smileyCount = itemView.findViewById(R.id.smiley_count);
            thumbsUpButton = itemView.findViewById(R.id.thumbsup_button);
            thumbsUpCount = itemView.findViewById(R.id.thumbsup_count);
        }
    }

    private void updateReaction(PostViewHolder holder, Post post, String newReaction) {
        String currentUserId = getCurrentUserId();
        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("hosts").child("1").child("posts").child(post.getId());

        postRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Post p = mutableData.getValue(Post.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                Map<String, String> userReactions = p.getUserReactions();
                if (userReactions == null) {
                    userReactions = new HashMap<>();
                }

                // Get the previous reaction of the current user
                String previousReaction = userReactions.get(currentUserId);

                // Decrement the previous reaction count
                if (previousReaction != null) {
                    switch (previousReaction) {
                        case "likeCount":
                            p.setLikeCount(p.getLikeCount() - 1);
                            break;
                        case "smileyCount":
                            p.setSmileyCount(p.getSmileyCount() - 1);
                            break;
                        case "thumbsUpCount":
                            p.setThumbsUpCount(p.getThumbsUpCount() - 1);
                            break;
                    }
                }

                // If the new reaction is the same as the previous reaction, remove it
                if (newReaction.equals(previousReaction)) {
                    userReactions.remove(currentUserId);
                } else {
                    // Otherwise, set the new reaction count
                    switch (newReaction) {
                        case "likeCount":
                            p.setLikeCount(p.getLikeCount() + 1);
                            break;
                        case "smileyCount":
                            p.setSmileyCount(p.getSmileyCount() + 1);
                            break;
                        case "thumbsUpCount":
                            p.setThumbsUpCount(p.getThumbsUpCount() + 1);
                            break;
                    }
                    // Update the user's current reaction
                    userReactions.put(currentUserId, newReaction);
                }

                p.setUserReactions(userReactions);

                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot dataSnapshot) {
                if (databaseError == null && committed && dataSnapshot != null) {
                    Post updatedPost = dataSnapshot.getValue(Post.class);
                    if (updatedPost != null) {
                        holder.likeCount.setText(String.valueOf(updatedPost.getLikeCount()));
                        holder.smileyCount.setText(String.valueOf(updatedPost.getSmileyCount()));
                        holder.thumbsUpCount.setText(String.valueOf(updatedPost.getThumbsUpCount()));
                    }
                }
            }
        });
    }

    private String getCurrentUserId() {
        // Implement a method to retrieve the current user's ID
        // This can be based on Firebase Auth or any other user management system you are using
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}