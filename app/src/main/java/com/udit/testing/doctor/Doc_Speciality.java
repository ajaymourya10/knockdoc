package com.udit.testing.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.udit.testing.R;
import com.udit.testing.patient.doctors_info.DoctorsList;

public class Doc_Speciality extends AppCompatActivity {

    private Toolbar toolbar;
    //private String doctor_type;
    private String key = "xyz";
    CardView cardView1, cardView2, cardView3, cardView4, cardView5, cardView6, cardView7, cardView8, cardView9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_special);

        cardView1 = findViewById(R.id.cardview1);
        cardView2 = findViewById(R.id.cardView2);
        cardView3 = findViewById(R.id.cardView3);
        cardView4 = findViewById(R.id.cardView4);
        cardView5 = findViewById(R.id.cardView5);
        cardView6 = findViewById(R.id.cardView6);
        cardView7 = findViewById(R.id.cardView7);
        cardView8 = findViewById(R.id.cardView8);
        cardView9 = findViewById(R.id.cardView9);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select DoctorType");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key="Heart Surgeon";
                Intent intent=new Intent(getApplicationContext(),DoctorsList.class);
                intent.putExtra("key",key);
                startActivity(intent);
            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key="Orthopedic";
               Intent intent= new Intent(getApplicationContext(),DoctorsList.class);
               intent.putExtra("key",key);
                startActivity(intent);
            }
        });

        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key="Paediatrician";
                Intent intent=new Intent(getApplicationContext(),DoctorsList.class);
                intent.putExtra("key",key);
                startActivity(intent);
            }
        });

        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key="Dentist";
                Intent intent=new Intent(getApplicationContext(),DoctorsList.class);
                intent.putExtra("key",key);
                startActivity(intent);
            }
        });

        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key="Psychiatrist";
                Intent intent=new Intent(getApplicationContext(),DoctorsList.class);
                intent.putExtra("key",key);
                startActivity(intent);
            }
        });

        cardView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key="Neurologist";
                Intent intent=new Intent(getApplicationContext(),DoctorsList.class);
                intent.putExtra("key",key);
                startActivity(intent);
            }
        });

        cardView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key="Gynecologist";
                Intent intent=new Intent(getApplicationContext(),DoctorsList.class);
                intent.putExtra("key",key);
                startActivity(intent);
            }
        });

        cardView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key="ENT Specialist";
                Intent intent=new Intent(getApplicationContext(),DoctorsList.class);
                intent.putExtra("key",key);
                startActivity(intent);
            }
        });

        cardView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key="Physiotherapist";
                Intent intent=new Intent(getApplicationContext(),DoctorsList.class);
                intent.putExtra("key",key);
                startActivity(intent);
            }
        });

    }


}
