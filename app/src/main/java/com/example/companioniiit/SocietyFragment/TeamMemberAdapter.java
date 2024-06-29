package com.example.companioniiit.SocietyFragment;

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

public class TeamMemberAdapter extends RecyclerView.Adapter<TeamMemberAdapter.TeamMemberViewHolder> {
    private List<TeamMember> teamMemberList;

    public TeamMemberAdapter(List<TeamMember> teamMemberList) {
        this.teamMemberList = teamMemberList;
    }

    @NonNull
    @Override
    public TeamMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team_member, parent, false);
        return new TeamMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamMemberViewHolder holder, int position) {
        TeamMember member = teamMemberList.get(position);
        holder.nameTextView.setText(member.getName());
        holder.designationTextView.setText(member.getDesignation());
        Picasso.get().load(member.getImageUrl()).placeholder(R.drawable.erroricon) // Optional placeholder
                .error(R.drawable.erroricon) // Optional error image
                .into(holder.photoImageView);
    }

    @Override
    public int getItemCount() {
        return teamMemberList.size();
    }

    static class TeamMemberViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, designationTextView;
        ImageView photoImageView;

        TeamMemberViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.team_member_name);
            designationTextView = itemView.findViewById(R.id.team_member_designation);
            photoImageView = itemView.findViewById(R.id.team_member_photo);
        }
    }
}


