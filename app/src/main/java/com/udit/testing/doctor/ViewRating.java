package com.udit.testing.doctor;

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
import com.udit.testing.Model.sessions.SessionManager;
import com.udit.testing.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewRating extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar, drProgressBar;
    private Context context;
    private ViewRatingAdapter imageAdapter;
    private Toolbar toolbar;
    private String _mobileNo;
    private DatabaseReference databaseReference;
    private ArrayList<HelperRating> mPendingAppointments;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_appointment_patient);

        SessionManager sessionManager = new SessionManager(this, SessionManager.USER_LOGIN_SESSION);
        HashMap<String, String> doctorDetails = sessionManager.getUserDetailFromSession();

        _mobileNo = doctorDetails.get(PatientSession.KEY_PHONE_NUMBER);

        toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rating and Reviews");
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
                .child("Reviews");
        ClearAll();

        GetDataFromFirebase();

    }

    private void filter(String text) {
        ArrayList<HelperRating> filteredList = new ArrayList<>();
        for (HelperRating item : mPendingAppointments) {
            if (item.getFeedback().toLowerCase().contains(text.toLowerCase())) {
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
                    HelperRating rating = new HelperRating();
                    //String degree = snapshot.child("degree").getValue().toString();
                    rating.setRating(snapshot.child("rating").getValue().toString());
                    rating.setFeedback(snapshot.child("feedback").getValue().toString());
                    rating.setMob(_mobileNo);



                        mPendingAppointments.add(rating);


                }
                imageAdapter = new ViewRatingAdapter(getApplicationContext(), mPendingAppointments);
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