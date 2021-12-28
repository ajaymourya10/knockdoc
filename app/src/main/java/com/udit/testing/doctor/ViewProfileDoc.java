package com.udit.testing.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.udit.testing.Model.sessions.SessionManager;
import com.udit.testing.R;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfileDoc extends AppCompatActivity {

    Toolbar toolbar;
    Button editprofile , editclinic;
    TextView doctor_name,doctor_gender,doctor_dob,doc_EmailId,doc_clinic,doc_degree,practiceyear,doc_type,
            doc_fees,doc_address,doc_city;
    CircleImageView doctor_profile_pic;
    DatabaseReference reference;
    private String mobile_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile_doc);

        editprofile=findViewById(R.id.EditProfile);
        editclinic=findViewById(R.id.EditClinic);
        doctor_name=findViewById(R.id.doctor_name);
        doctor_gender=findViewById(R.id.doctor_gender);
        doctor_dob=findViewById(R.id.doctor_dob);
        doc_EmailId=findViewById(R.id.Doc_EmailId);
        doc_clinic=findViewById(R.id.doc_clinic);
        doc_degree=findViewById(R.id.doc_degree);
        practiceyear=findViewById(R.id.practiceyear);
        doc_type=findViewById(R.id.doc_type);
        doc_fees=findViewById(R.id.doc_fees);
        doc_address=findViewById(R.id.doc_address);
        doc_city=findViewById(R.id.Doc_city);
        doctor_profile_pic=findViewById(R.id.doctor_profile_pic);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Doctor Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        SessionManager sessionManager = new SessionManager(this, SessionManager.USER_LOGIN_SESSION);
        HashMap<String, String> doctorDetails = sessionManager.getUserDetailFromSession();
        mobile_no = doctorDetails.get(SessionManager.KEY_PHONE_NUMBER);

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),EditProfileDoctor.class));
            }
        });

        editclinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),EditDoctorClinic.class));
            }
        });
        getData();
    }
    private void getData()
    {

            reference = FirebaseDatabase.getInstance().getReference().child("Users").child("Doctor");
            Query query = reference.child(mobile_no);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                    //if(snapshot.exists())
                    try {
                    doctor_name.setText(snapshot.child("name").getValue().toString().toUpperCase());
                    doctor_gender.setText(snapshot.child("gender").getValue().toString().toUpperCase());
                    doctor_dob.setText(snapshot.child("dob").getValue().toString());
                    doc_EmailId.setText(snapshot.child("email").getValue().toString().toUpperCase());
                    if (snapshot.child("clinic").child("title").getValue().toString().equals(null))
                        doc_clinic.setText("Not Available");
                    else
                        doc_clinic.setText(snapshot.child("clinic").child("title").getValue().toString().toUpperCase());

                    doc_degree.setText(snapshot.child("degree").getValue().toString().toUpperCase());
                    if (snapshot.child("clinic").child("starting_year").getValue().toString().equals(null))
                        practiceyear.setText("Not Available");
                    else
                        practiceyear.setText(snapshot.child("clinic").child("starting_year").getValue().toString());
                    doc_type.setText(snapshot.child("doctor_type").getValue().toString().toUpperCase());
                    doc_fees.setText(snapshot.child("fee").getValue().toString());
                    if (snapshot.child("clinic").child("address").getValue().toString().equals(null))
                        doc_address.setText("Not Available");
                    else
                        doc_address.setText(snapshot.child("clinic").child("address").getValue().toString().toUpperCase());
                    if (snapshot.child("clinic").child("city").getValue().toString().equals(null))
                        doc_city.setText("Not Available");
                    else
                        doc_city.setText(snapshot.child("clinic").child("city").getValue().toString().toUpperCase());
                    String profile_pic = snapshot.child("profile_image").getValue().toString();
                    Picasso.get().load(profile_pic).placeholder(R.drawable.doctor).into(doctor_profile_pic);


                    }catch (Exception e)
                    {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    Toast.makeText(ViewProfileDoc.this, "Error fetching data", Toast.LENGTH_LONG).show();
                }
            });

    }


}