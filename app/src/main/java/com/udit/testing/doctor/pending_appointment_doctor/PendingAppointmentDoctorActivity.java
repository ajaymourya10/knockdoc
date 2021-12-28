package com.udit.testing.doctor.pending_appointment_doctor;

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
import com.udit.testing.Model.sessions.SessionManager;
import com.udit.testing.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PendingAppointmentDoctorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar, drProgressBar;
    private Context context;
    private PendingAppointmentDoctorAdapter imageAdapter;
    private Toolbar toolbar;
    private String _mobileNo,patient_mob,count;
    private DatabaseReference databaseReference;
    private ArrayList<AppointmentDoctor> mAppointmentDoctor;
    private EditText editText;
    private PendingAppointmentDoctorAdapter.ImageViewHolder imageViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_appointment_doctor);

        SessionManager sessionManager = new SessionManager(this, SessionManager.USER_LOGIN_SESSION);
        HashMap<String, String> doctorDetails = sessionManager.getUserDetailFromSession();

        _mobileNo = doctorDetails.get(SessionManager.KEY_PHONE_NUMBER);
        toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("List of Appointments");
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

        mAppointmentDoctor = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Appointments")
                .child("pending").child("doctor");
        ClearAll();
        GetDataFromFirebase();

    }

    private void filter(String text) {
        ArrayList<AppointmentDoctor> filteredList = new ArrayList<>();
        for (AppointmentDoctor item : mAppointmentDoctor) {
            if (item.getPatient_name().toLowerCase().contains(text.toLowerCase())) {
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
                    AppointmentDoctor pendingAppointment = new AppointmentDoctor();
                    //String degree = snapshot.child("degree").getValue().toString();


                        pendingAppointment.setCount(snapshot.child("count").getValue().toString());
                        pendingAppointment.setPatient_name(snapshot.child("patient_name").getValue().toString());
                        pendingAppointment.setAppointment_date(snapshot.child("appointment_date").getValue().toString());
                        pendingAppointment.setAppointment_day(snapshot.child("appointment_day").getValue().toString());
                        pendingAppointment.setPatient_dob(snapshot.child("patient_dob").getValue().toString());
                        pendingAppointment.setPatient_gender(snapshot.child("patient_gender").getValue().toString());
                        pendingAppointment.setTime_slot(snapshot.child("time_slot").getValue().toString());
                        pendingAppointment.setPatient_image(snapshot.child("patient_image").getValue().toString());
                        pendingAppointment.setPatient_mobile(snapshot.child("patient_mobile").getValue().toString());
                        pendingAppointment.setCount(snapshot.child("count").getValue().toString());
                        pendingAppointment.setToken(snapshot.child("token").getValue().toString());
                        pendingAppointment.setDoc_mob(_mobileNo);
                        // pendingAppointment.setDoc_img(snapshot.child());

                        mAppointmentDoctor.add(pendingAppointment);


                }
                imageAdapter = new PendingAppointmentDoctorAdapter(getApplicationContext(), mAppointmentDoctor);
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
        if (mAppointmentDoctor != null) {
            mAppointmentDoctor.clear();
            if (imageAdapter != null)
                imageAdapter.notifyDataSetChanged();
        }
        mAppointmentDoctor = new ArrayList<>();
    }

    public void searchData(View view) {
        drProgressBar.setVisibility(View.VISIBLE);
        filter(editText.getText().toString());
        drProgressBar.setVisibility(View.GONE);
    }
}