package com.udit.testing.patient.rating;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udit.testing.Model.datainfo;
import com.udit.testing.R;

import org.jetbrains.annotations.NotNull;

public class HomeActivity extends AppCompatActivity {

    private Button button;
    private Button nextbtn;
    private EditText feedback;
    private Toolbar toolbar;
    private TextView ratetxt;
    String pat_name,dr_mob;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    com.udit.testing.Model.datainfo datainfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        RatingBar ratingBar =  findViewById(R.id.ratingBar);
        button =  findViewById(R.id.button);
        feedback =  findViewById(R.id.feedback);

        Intent intent=getIntent();
        pat_name=intent.getStringExtra("ptname");
        dr_mob=intent.getStringExtra("drmob");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rating and Review");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Appointments").child("Reviews").child(dr_mob).child(pat_name);
        datainfo = new datainfo();

//        float rating=ratingBar.getRating();
//        Log.d("tagggggg"," "+rating);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = ratingBar.getRating();
                String feed = feedback.getText().toString();
                Toast.makeText(getApplicationContext(),"You have rated the doctor",Toast.LENGTH_SHORT).show();

                addData(rating,feed);
                startActivity(new Intent(HomeActivity.this, Approved.class));
            }
        });

    }

    private void addData(float rating, String feed){

        datainfo.setRating(rating);
        datainfo.setFeedback(feed);
        //Map<String,String>

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                databaseReference.setValue(datainfo);
                //Toast.makeText(HomeActivity.this, "Data Added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
