package com.example.companioniiit.Host;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.companioniiit.R;

public class techsociety_host_home extends AppCompatActivity {

    private String hostEmail;
    private AppCompatButton AddEventbtn;
    private AppCompatButton AddTeamMemberbtn;
    private AppCompatButton AddEventimgbtn;
    private AppCompatButton Addannouncementbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_techsociety_host_home);

        AddEventbtn = findViewById(R.id.add_event_techsociety);
        AddTeamMemberbtn = findViewById(R.id.upload_team_members);
        AddEventimgbtn = findViewById(R.id.upload_event_images_techsociety);
        Addannouncementbtn = findViewById(R.id.add_announcement_techsociety);

        OnclickAddEventbtn();
        OnclickAddTeamMemberbtn();
        OnclickAddEventimgbtn();
        OnclickAddannouncementbtn();

    }

    private void OnclickAddannouncementbtn() {

      Addannouncementbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(techsociety_host_home.this, hostside_societies_announcement.class);

                intent.putExtra("host_email", "hosttechsociety@gmail.com");
                startActivity(intent);
            }
        });
    }

    private void OnclickAddEventimgbtn() {
        AddEventimgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(techsociety_host_home.this, hostside_societies_event_images.class);
                startActivity(intent);
            }
        });
    }

    private void OnclickAddTeamMemberbtn() {
        AddTeamMemberbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(techsociety_host_home.this, hostside_societies_teammembers.class);
                startActivity(intent);
            }
        });
    }

    private void OnclickAddEventbtn() {
        AddEventbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(techsociety_host_home.this, hostside_societies_calendar.class);
                startActivity(intent);
            }
        });

    }
}
