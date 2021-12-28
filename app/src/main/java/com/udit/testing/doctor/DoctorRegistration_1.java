package com.udit.testing.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.santalu.maskara.widget.MaskEditText;
import com.udit.testing.MobileRegistration;
import com.udit.testing.R;

public class DoctorRegistration_1 extends AppCompatActivity {

    private TextInputLayout name, registrationNo;
    private MaskEditText dob;
    private String gender,tok;
    private AutoCompleteTextView gen;
    private Button next, alreadyAccount;
    private static final String[] GENDERS = new String[]{"MALE", "FEMALE", "OTHERS", "TRANSGENDER"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration_1);

        name = findViewById(R.id.doctor_fullname);
        registrationNo = findViewById(R.id.doctor_registration_no);
        dob = findViewById(R.id.doctor_dob);

        gen = findViewById(R.id.actv);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,GENDERS);
        gen.setAdapter(adapter);



    }

    public void gotoDoctorRegistrationPage2(View view) {

        String _name, _regNo, _dob;
        _name = name.getEditText().getText().toString().trim();
        _regNo = registrationNo.getEditText().getText().toString().trim();
        _dob = dob.getMasked().trim();//   .getText().toString().trim();

        if (!validateName() | !validateRegNo() | !validateGender() | !validateDOB()) {
            return;
        }

        gender = gen.getText().toString();
        ///Toast.makeText(getApplicationContext(),"Gender is: "+gender,Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),"Date of Birth is: "+_dob,Toast.LENGTH_LONG).show();
        String key="doctor";
        Intent intent = new Intent(getApplicationContext(), MobileRegistration.class);
        intent.putExtra("full_name", _name);
        intent.putExtra("reg_no", _regNo);
        intent.putExtra("dob", _dob);
        intent.putExtra("gender", gender);
        intent.putExtra("key", key);
        startActivity(intent);
    }


    private Boolean validateName() {
        String val = name.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            name.setErrorEnabled(true);
            name.setError("Field cannot be empty");
            return false;
        }
        name.setError(null);
        name.setErrorEnabled(false);
        return true;
    }

    private Boolean validateRegNo() {
        String val = registrationNo.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            registrationNo.setErrorEnabled(true);
            registrationNo.setError("Field cannot be empty");
            return false;
        }
        registrationNo.setError(null);
        registrationNo.setErrorEnabled(false);
        return true;
    }

    private Boolean validateGender() {
        String val = gen.getText().toString().trim();
        if (val.isEmpty()) {
            gen.setError("Field cannot be empty");
            return false;
        }
        gen.setError(null);
        return true;
    }

    private Boolean validateDOB() {
        String val = dob.getText().toString().trim();
        if (val.isEmpty()) {
            dob.setError("Field cannot be empty");
            return false;
        }
        dob.setError(null);
        return true;
    }

    public void gotoDoctorSignIn(View view) {
        onBackPressed();
    }
}