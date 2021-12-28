package com.udit.testing.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.udit.testing.R;

public class DoctorEmailRegistration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_email_registration);
    }

    public void gotoOTPPage(View view) {
        startActivity(new Intent(getApplicationContext(),DoctorOTPPage.class));
    }
}