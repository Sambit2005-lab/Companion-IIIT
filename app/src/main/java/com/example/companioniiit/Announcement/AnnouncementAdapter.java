package com.example.companioniiit.Announcement;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.companioniiit.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder> {

    private List<Announcement> announcementList;

    public AnnouncementAdapter(List<Announcement> announcementList) {
        this.announcementList = announcementList;
    }

    @NonNull
    @Override
    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_card, parent, false);
        return new AnnouncementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {
        Announcement announcement = announcementList.get(position);
        holder.captionTextView.setText(announcement.getCaption());
        holder.postedByTextView.setText(announcement.getPostedBy());

        if (announcement.getImageUrl() != null && !announcement.getImageUrl().isEmpty()) {
            holder.imageView.setVisibility(View.VISIBLE);
            Picasso.get().load(announcement.getImageUrl()).into(holder.imageView);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return announcementList.size();
    }

    public static class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView captionTextView;
        public TextView postedByTextView;

        public AnnouncementViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_announcement);
            captionTextView = itemView.findViewById(R.id.text_view_caption);
            postedByTextView = itemView.findViewById(R.id.text_view_posted_by);
        }
    }
}
