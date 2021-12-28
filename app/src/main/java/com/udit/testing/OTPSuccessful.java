package com.udit.testing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.udit.testing.doctor.DoctorSignIn;
import com.udit.testing.patient.PatientSignIn;

public class OTPSuccessful extends AppCompatActivity {

    private String key,name1;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_successful);

        name=findViewById(R.id.name);
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        name1=intent.getStringExtra("name");
    }

    public void gotoDoctorDashboard(View view) {

        if(key.equals("patient")){
            name.setText(name1);
            startActivity(new Intent(getApplicationContext(), PatientSignIn.class));
        }else {
            name.setText(name1);
            startActivity(new Intent(getApplicationContext(), DoctorSignIn.class));
        }
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
    }
}