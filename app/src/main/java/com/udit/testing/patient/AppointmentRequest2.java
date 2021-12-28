package com.udit.testing.patient;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.udit.testing.R;

public class AppointmentRequest2 extends AppCompatActivity {

    private Toolbar toolbar;
    private CardView cardView1,cardView2;
    private TextView date,morningTime,eveningTime;
    private Button next;
    private String keyValue = "card";
    private String _name, _mobileNo, _gender,rating,doc_type, _address, _dob,_day, token,_appointmentDate,_ptPic, _aadharNo,_doctorMobileNo,profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_request2);

        settingViewByID();
        next.setEnabled(false);
        gettingDataFromIntent();

        date.setText(_appointmentDate+" ("+_day+")");

        settingOnClick();

    }

    private void gettingDataFromIntent() {

        Intent intent = getIntent();
        _name = intent.getStringExtra("name");
        _dob = intent.getStringExtra("dob");
        _appointmentDate = intent.getStringExtra("appointment_date");
        _address = intent.getStringExtra("address");
        _aadharNo = intent.getStringExtra("aadhar");
        _doctorMobileNo = intent.getStringExtra("doctor_mobile");
        _gender = intent.getStringExtra("gender");
        _day = intent.getStringExtra("day");
        _mobileNo = intent.getStringExtra("mobile");
        rating = intent.getStringExtra("rating");
        token=intent.getStringExtra("token");



    }

    private void settingOnClick() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                keyValue = "card1";
                cardView2.setCardBackgroundColor(Color.WHITE);
                cardView1.setCardBackgroundColor(Color.GRAY);
                next.setEnabled(true);

            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                keyValue = "card2";
                cardView1.setCardBackgroundColor(Color.WHITE);
                cardView2.setCardBackgroundColor(Color.GRAY);
                next.setEnabled(true);

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String timeSlot;

                if(keyValue.equals("card1")){
                    timeSlot = "Morning";
                }else if(keyValue.equals("card2")){
                   timeSlot = "Evening";
                }else{
                    return;
                }

                Intent intent = new Intent(getApplicationContext(),AppointmentRequest3.class);
                intent.putExtra("name",_name);
                intent.putExtra("dob",_dob);
                intent.putExtra("gender",_gender);
                intent.putExtra("appointment_date",_appointmentDate);
                intent.putExtra("address",_address);
                intent.putExtra("aadhar",_aadharNo);
                intent.putExtra("doctor_mobile",_doctorMobileNo);
                intent.putExtra("day",_day);
                intent.putExtra("time_slot",timeSlot);
                intent.putExtra("mobile",_mobileNo);
                intent.putExtra("rating",rating);
                intent.putExtra("token",token);

                startActivity(intent);


            }
        });

    }

    private void settingViewByID() {

        cardView1 = findViewById(R.id.cardview1);
        cardView2 = findViewById(R.id.cardview2);
        next = findViewById(R.id.next_button_time_slot);
        morningTime = findViewById(R.id.morning_time_slot);
        eveningTime = findViewById(R.id.evening_time_slot);
        date = findViewById(R.id.appointment_date);

        toolbar = findViewById(R.id.time_slot_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Doctor's Schedule");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

}