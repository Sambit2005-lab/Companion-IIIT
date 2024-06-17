package com.example.companioniiit.SocietyFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.companioniiit.R;
import com.example.companioniiit.StudyFragment.StudyFragment;


public class SocietyFragment extends Fragment {

    private AppCompatButton btn_techsociety;

    private AppCompatButton btn_ecell;

    private AppCompatButton btn_tars;

    private AppCompatButton btn_maegahertz;

    private AppCompatButton btn_sportsociety;
    private AppCompatButton btn_vedanatsamiti;
    private AppCompatButton btn_naps;
    private AppCompatButton btn_fats;
    private AppCompatButton btn_paracosm;
    private AppCompatButton btn_photogeeks;
    private AppCompatButton btn_culturalsociety;




    public SocietyFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_society, container, false);

        btn_techsociety=view.findViewById(R.id.techsociety_card);
        btn_ecell=view.findViewById(R.id.ECELL_card);
        btn_maegahertz=view.findViewById(R.id.megaheartz_card);
        btn_culturalsociety=view.findViewById(R.id.cult_society_card);
        btn_fats=view.findViewById(R.id.FATS_card);
        btn_paracosm=view.findViewById(R.id.paracosm_card);
        btn_naps=view.findViewById(R.id.naps_card);
        btn_photogeeks=view.findViewById(R.id.photogeeks_card);
        btn_sportsociety=view.findViewById(R.id.sportssociety_card);
        btn_tars=view.findViewById(R.id.tars_card);
        btn_vedanatsamiti=view.findViewById(R.id.vedanta_samiti_card);

        btn_techsociety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), TechSociety.class);
                startActivity(intent);
            }
        });

        btn_ecell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Ecell.class);
                startActivity(intent);
            }
        });

        btn_maegahertz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), megahertz.class);
                startActivity(intent);
            }
        });

        btn_culturalsociety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), cultural_society.class);
                startActivity(intent);
            }
        });

        btn_fats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), fats.class);
                startActivity(intent);
            }
        });

        btn_paracosm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), paracosm.class);
                startActivity(intent);
            }
        });

        btn_naps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), naps.class);
                startActivity(intent);
            }
        });

        btn_photogeeks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), photogeeks.class);
                startActivity(intent);
            }
        });

        btn_sportsociety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), sportsociety.class);
                startActivity(intent);
            }
        });

        btn_tars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Tars.class);
                startActivity(intent);
            }
        });

        btn_vedanatsamiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), vedantasamiti.class);
                startActivity(intent);
            }
        });

        return view;
    }
}