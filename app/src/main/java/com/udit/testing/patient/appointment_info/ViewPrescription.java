package com.udit.testing.patient.appointment_info;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.udit.testing.Model.sessions.PatientSession;
import com.udit.testing.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ViewPrescription extends AppCompatActivity {

    private String mobileNo, count;
    private TextView textView;
    private ImageView imageView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_prescription);

        Intent intent = getIntent();
       // mobileNo = intent.getStringExtra("mobile_no");
        count = intent.getStringExtra("count");

        PatientSession sessionManager = new PatientSession(this, PatientSession.PATIENT_LOGIN_SESSION);
        HashMap<String, String> doctorDetails = sessionManager.getUserDetailFromSession();

        mobileNo = doctorDetails.get(PatientSession.KEY_PHONE_NUMBER);


        imageView = findViewById(R.id.pres_pic);
        textView = findViewById(R.id.pres_desc);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Prescription");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ProgressDialog dialog = new ProgressDialog(ViewPrescription.this);
        dialog.setTitle("Please Wait...");
        dialog.setCancelable(false);
        dialog.show();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference()
                .child("Appointments").child("pending").child("patient")
                .child(mobileNo).child(count);
//
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    textView.setText(snapshot.child("prescription").child("description").getValue().toString());
                    String image = snapshot.child("prescription").child("image").getValue().toString();

                    Picasso.get().load(image).placeholder(R.drawable.doctor_pic).into(imageView);



                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                dialog.dismiss();

            }
        });


    }
}