package com.udit.testing.patient.doctors_info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.udit.testing.Model.Doctor;
import com.udit.testing.R;

import java.util.ArrayList;

public class DoctorsList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProgressBar progressBar, drProgressBar;
    private Context context;
    private DoctorsListAdapter imageAdapter;
    private Toolbar toolbar;
    private DatabaseReference databaseReference;
    private ArrayList<Doctor> mDoctors;
    private EditText editText;
    private String key="none";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_list);

        toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("List of Doctors");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        editText = findViewById(R.id.search_box_doc);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        Intent intent=getIntent();
         key=intent.getStringExtra("key");

        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.Progress_circle);
        drProgressBar = findViewById(R.id.dr_progress_bar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDoctors = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        ClearAll();
        GetDataFromFirebase();

    }


    private void filter(String text) {
        ArrayList<Doctor> filteredList = new ArrayList<>();
        for (Doctor item : mDoctors) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        imageAdapter.filterList(filteredList);
    }


    private void GetDataFromFirebase() {

        Query query = databaseReference.child("Doctor");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearAll();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Doctor doctor = new Doctor();
                    String degree = snapshot.child("degree").getValue().toString();

                    String doctor_type= snapshot.child("doctor_type").getValue().toString();

                    Log.d("tooooooo","msg "+doctor_type+" "+key);

                    if (!degree.equals("none")) {

                        if(doctor_type.equals(key)) {
                            doctor.setProfile_image(snapshot.child("profile_image").getValue().toString());
                            doctor.setName(snapshot.child("name").getValue().toString());
                            doctor.setDoctor_type(snapshot.child("doctor_type").getValue().toString());
                            doctor.setDegree(snapshot.child("degree").getValue().toString());
                            doctor.setMobile_no(snapshot.child("mobile_no").getValue().toString());
                            doctor.setRating(snapshot.child("rating").getValue().toString());
                            doctor.setFee(snapshot.child("fee").getValue().toString());
                            mDoctors.add(doctor);
                        }
                        else if(key.equals("all"))
                        {
                            doctor.setProfile_image(snapshot.child("profile_image").getValue().toString());
                            doctor.setName(snapshot.child("name").getValue().toString());
                            doctor.setDoctor_type(snapshot.child("doctor_type").getValue().toString());
                            doctor.setDegree(snapshot.child("degree").getValue().toString());
                            doctor.setMobile_no(snapshot.child("mobile_no").getValue().toString());
                            doctor.setRating(snapshot.child("rating").getValue().toString());
                            doctor.setFee(snapshot.child("fee").getValue().toString());
                            mDoctors.add(doctor);
                        }
                    }


                }
                imageAdapter = new DoctorsListAdapter(getApplicationContext(), mDoctors);
                recyclerView.setAdapter(imageAdapter);
                imageAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void ClearAll() {
        if (mDoctors != null) {
            mDoctors.clear();
            if (imageAdapter != null)
                imageAdapter.notifyDataSetChanged();
        }
        mDoctors = new ArrayList<>();
    }

    public void searchData(View view) {
        drProgressBar.setVisibility(View.VISIBLE);
        filter(editText.getText().toString());
        drProgressBar.setVisibility(View.GONE);
    }
}