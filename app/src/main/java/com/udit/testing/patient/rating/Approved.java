package com.udit.testing.patient.rating;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.udit.testing.Model.sessions.PatientSession;
import com.udit.testing.R;
import com.udit.testing.patient.appointment_info.PendingAppointment;

import java.util.ArrayList;
import java.util.HashMap;

public class Approved extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar, drProgressBar;
    private Context context;
    private ApprovedAdapter imageAdapter;
    private Toolbar toolbar;
    private String _mobileNo;
    private DatabaseReference databaseReference,reference;
    private ArrayList<PendingAppointment> mPendingAppointments;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_appointment_patient);

        PatientSession sessionManager = new PatientSession(this, PatientSession.PATIENT_LOGIN_SESSION);
        HashMap<String, String> doctorDetails = sessionManager.getUserDetailFromSession();

        _mobileNo = doctorDetails.get(PatientSession.KEY_PHONE_NUMBER);

        toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Accepted Appointments");
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

        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.Progress_circle);
        drProgressBar = findViewById(R.id.dr_progress_bar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mPendingAppointments = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Appointments")
                .child("pending").child("patient");


        ClearAll();

            GetDataFromFirebase();

    }

    private void filter(String text) {
        ArrayList<PendingAppointment> filteredList = new ArrayList<>();
        for (PendingAppointment item : mPendingAppointments) {
            if (item.getDoctor_name().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        imageAdapter.filterList(filteredList);
    }


    private void GetDataFromFirebase() {

        Query query = databaseReference.child(_mobileNo);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearAll();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PendingAppointment pendingAppointment = new PendingAppointment();
                    //String degree = snapshot.child("degree").getValue().toString();
                            if(snapshot.child("status").getValue().toString().equals("approved")) {
                                pendingAppointment.setCount(snapshot.child("count").getValue().toString());
                                pendingAppointment.setPatient_name(snapshot.child("patient_name").getValue().toString());
                                pendingAppointment.setDoctor_name(snapshot.child("doctor_name").getValue().toString());
                                pendingAppointment.setDoctor_type(snapshot.child("doctor_type").getValue().toString());
                                pendingAppointment.setDegree(snapshot.child("degree").getValue().toString());
                                pendingAppointment.setDoctor_dp(snapshot.child("doctor_dp").getValue().toString());
                                pendingAppointment.setDoctor_fee(snapshot.child("doctor_fee").getValue().toString());
                                pendingAppointment.setTime_slot(snapshot.child("time_slot").getValue().toString());
                                pendingAppointment.setDoctor_mobile(snapshot.child("doctor_mobile").getValue().toString());
                                pendingAppointment.setRating(snapshot.child("rating").getValue().toString());
                                pendingAppointment.setAppointment_date(snapshot.child("appointment_date").getValue().toString());
                                pendingAppointment.setAppointment_day(snapshot.child("appointment_day").getValue().toString());
                                pendingAppointment.setStatus(snapshot.child("status").getValue().toString());
                                pendingAppointment.setPatient_dob(snapshot.child("patient_dob").getValue().toString());
                                pendingAppointment.setGender(snapshot.child("gender").getValue().toString());
                                pendingAppointment.setPatient_dp(snapshot.child("patient_dp").getValue().toString());


                                mPendingAppointments.add(pendingAppointment);
                            }

                }
                imageAdapter = new ApprovedAdapter(getApplicationContext(), mPendingAppointments);
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
        if (mPendingAppointments != null) {
            mPendingAppointments.clear();
            if (imageAdapter != null)
                imageAdapter.notifyDataSetChanged();
        }
        mPendingAppointments = new ArrayList<>();
    }

    public void searchData(View view) {
        drProgressBar.setVisibility(View.VISIBLE);
        filter(editText.getText().toString());
        drProgressBar.setVisibility(View.GONE);
    }




}
