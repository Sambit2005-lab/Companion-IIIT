package com.example.companioniiit.SocietyFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.companioniiit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class about_tars extends Fragment {
    private RecyclerView eventsRecyclerView;
    private RecyclerView teamMembersRecyclerView;
    private List<Event> eventList;
    private List<TeamMember> teamMemberList;
    private EventAdapter eventAdapter;
    private TeamMemberAdapter teamMemberAdapter;
    private DatabaseReference databaseReference;



    public about_tars() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_about_tars, container, false);

        eventsRecyclerView = view.findViewById(R.id.our_events_recyclerview_sportssociety);
        teamMembersRecyclerView = view.findViewById(R.id.team_members_recyclerview_sportssociety);

        eventList = new ArrayList<>();
        teamMemberList = new ArrayList<>();

        eventAdapter = new EventAdapter(eventList);
        teamMemberAdapter = new TeamMemberAdapter(teamMemberList);

        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        eventsRecyclerView.setAdapter(eventAdapter);

        teamMembersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        teamMembersRecyclerView.setAdapter(teamMemberAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Fetching events data
        databaseReference.child("hosts").child("3").child("eventImages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String photoUrl = snapshot.getValue(String.class);
                    Event event = new Event(photoUrl);
                    eventList.add(event);
                }
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
                Log.e("Firebase Error", "Failed to fetch events: " + databaseError.getMessage());
                Toast.makeText(getContext(), "Failed to fetch events.", Toast.LENGTH_SHORT).show();
            }
        });

        // Fetching team members data
        databaseReference.child("hosts").child("3").child("teamMembers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TeamMember member = snapshot.getValue(TeamMember.class);
                    teamMemberList.add(member);
                }
                teamMemberAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });


        return view;
    }
}