package com.udit.testing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.udit.testing.Model.sessions.PatientSession;
import com.udit.testing.Model.sessions.SessionManager;
import com.udit.testing.doctor.DoctorDashboard;
import com.udit.testing.doctor.DoctorSignIn;
import com.udit.testing.patient.PatientDashboard;
import com.udit.testing.patient.PatientSignIn;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void gotoDoctorSignIn(View view) {
        startActivity(new Intent(getApplicationContext(), DoctorSignIn.class));
    }

    @Override
    protected void onStart() {
        super.onStart();

        SessionManager sessionManager = new SessionManager(this, SessionManager.USER_LOGIN_SESSION);
        PatientSession session = new PatientSession(this, PatientSession.PATIENT_LOGIN_SESSION);

        if (session.checkLogin()) {
            startActivity(new Intent(getApplicationContext(), PatientDashboard.class));
            finish();
        } else if (sessionManager.checkLogin()) {
            startActivity(new Intent(getApplicationContext(), DoctorDashboard.class));
            finish();
        }

    }

    public void gotoPatientSignIn(View view) {
        startActivity(new Intent(getApplicationContext(), PatientSignIn.class));
    }
}