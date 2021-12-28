package com.udit.testing.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.udit.testing.R;
import com.udit.testing.PasswordSetup;

public class DoctorOTPPage extends AppCompatActivity {

    private String name,registrationNo,dob,gender,mobileNo,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_otp_page);



        Intent intent = getIntent();
        name = intent.getStringExtra("full_name");
        registrationNo = intent.getStringExtra("reg_no");
        dob = intent.getStringExtra("dob");
        mobileNo = intent.getStringExtra("mobile_no");
        gender = intent.getStringExtra("gender");
      //  email = "none";

    }

    public void gotoVerifiedPage(View view) {
       // startActivity(new Intent(getApplicationContext(), DoctorPassword.class));
        Intent intent = new Intent(getApplicationContext(), PasswordSetup.class);
        intent.putExtra("full_name", name);
        intent.putExtra("reg_no", registrationNo);
        intent.putExtra("dob", dob);
        intent.putExtra("gender", gender);
        intent.putExtra("mobile_no",mobileNo);
       // intent.putExtra("email",email);
        startActivity(intent);
        finish();


    }
}