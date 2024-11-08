package com.codexnovas.companioniiit.Host;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.codexnovas.companioniiit.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HostCompanionIIIT extends AppCompatActivity {

    private EditText joiningLinkEditText;
    private AppCompatButton saveJoiningLinkButton;
    private DatabaseReference databaseReference;
    private DatabaseReference hostJoiningLinksRef;
    private String hostEmail = "code.x.novas@gmail.com"; // Hardcoded host email

    private AppCompatButton AddEventbtn;
    private AppCompatButton AddTeamMemberbtn;
    private AppCompatButton AddEventimgbtn;
    private AppCompatButton Addannouncementbtn;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_companion_iiit);


        joiningLinkEditText = findViewById(R.id.joining_link_techsociety);
        saveJoiningLinkButton = findViewById(R.id.save_joining_link_techsociety);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("hosts");

        // Setting up the reference for joining links
        if (hostEmail.equals("code.x.novas@gmail.com")) {
            hostJoiningLinksRef = databaseReference.child("1").child("joining_links");
        } else {
            Toast.makeText(this, "Invalid host email", Toast.LENGTH_SHORT).show();
            return;
        }

        saveJoiningLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveJoiningLinkToFirebase();
            }
        });

        AddEventbtn = findViewById(R.id.add_event_techsociety);
        AddTeamMemberbtn = findViewById(R.id.upload_team_members);
        AddEventimgbtn = findViewById(R.id.upload_event_images_techsociety);
        Addannouncementbtn = findViewById(R.id.add_announcement_techsociety);

        OnclickAddEventbtn();
        OnclickAddTeamMemberbtn();
        OnclickAddEventimgbtn();
        OnclickAddannouncementbtn();
    }

    private void saveJoiningLinkToFirebase() {
        String joiningLink = joiningLinkEditText.getText().toString().trim();

        if (joiningLink.isEmpty()) {
            joiningLinkEditText.setError("Joining link is required");
            joiningLinkEditText.requestFocus();
            return;
        }

        String linkId = hostJoiningLinksRef.push().getKey();

        if (linkId != null) {
            hostJoiningLinksRef.child(linkId).setValue(joiningLink)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText( HostCompanionIIIT.this, "Joining link saved successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText( HostCompanionIIIT.this, "Failed to save joining link", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void OnclickAddannouncementbtn() {
        Addannouncementbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( HostCompanionIIIT.this, hostside_codexnova_societies_announcement.class);
                intent.putExtra("host_email", hostEmail);
                startActivity(intent);
            }
        });
    }

    private void OnclickAddEventimgbtn() {
        AddEventimgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( HostCompanionIIIT.this, hostside_codexnova_societies_event_images.class);
                intent.putExtra("hostEmail", hostEmail);
                startActivity(intent);
            }
        });
    }

    private void OnclickAddTeamMemberbtn() {
        AddTeamMemberbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( HostCompanionIIIT.this, hostside_codexnova_societies_teammembers.class);
                intent.putExtra("hostEmail", hostEmail);
                startActivity(intent);
            }
        });
    }

    private void OnclickAddEventbtn() {
        AddEventbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( HostCompanionIIIT.this, hostside_codexnova_societies_calendar.class);
                intent.putExtra("hostEmail", hostEmail);
                startActivity(intent);
            }
        });
    }
}
