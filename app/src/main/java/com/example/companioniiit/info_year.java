package com.example.companioniiit;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;





public class info_year extends AppCompatActivity {

    String[] Courses={"CSE", "IT", "CE", "ETC","EEE"};

    String[] Year={"1st year", "2nd year", "3rd year","4th year"};



    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapteritems;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_year);
        autoCompleteTextView = findViewById(R.id.autocomplete_text_course);

        adapteritems = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Courses);
        autoCompleteTextView.setAdapter(adapteritems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             String courses=parent.getItemAtPosition(position).toString();
            }
        });

        autoCompleteTextView = findViewById(R.id.autocomplete_text_year);

        adapteritems = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Year);
        autoCompleteTextView.setAdapter(adapteritems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String year=parent.getItemAtPosition(position).toString();
            }
        });






    }
}