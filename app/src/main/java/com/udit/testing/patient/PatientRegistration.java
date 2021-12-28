package com.udit.testing.patient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.santalu.maskara.widget.MaskEditText;
import com.udit.testing.MobileRegistration;
import com.udit.testing.R;

public class PatientRegistration extends AppCompatActivity {

    private TextInputLayout name;
    private MaskEditText dob;
    private AutoCompleteTextView gen, city;
    private Button next;
    private static final String[] GENDERS = new String[]{"MALE", "FEMALE", "OTHERS", "TRANSGENDER"};
    private static final String[] CITIES = new String[]{"Bareilly", "Sonipat", "Rishikesh", "Gorakhpur", "Kolkata", "Noida",
            "Jaipur", "Udaipur", "New Delhi", "Faridabad", "Meerut"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_patient_registration);


        name = findViewById(R.id.patient_fullname);
        dob = findViewById(R.id.patient_dob);
        city = findViewById(R.id.patient_city);
        gen = findViewById(R.id.patient_gender);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, GENDERS);
        gen.setAdapter(adapter);


        ArrayAdapter<String> adapterCity = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, CITIES);
        city.setAdapter(adapterCity);


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

    private Boolean validateGender() {
        String val = gen.getText().toString().trim();
        if (val.isEmpty()) {
            gen.setError("Field cannot be empty");
            return false;
        }
        gen.setError(null);
        return true;
    }

    private Boolean validateCity() {
        String val = city.getText().toString().trim();
        if (val.isEmpty()) {
            city.setError("Field cannot be empty");
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


    public void gotoPatientSignIn(View view) {
        onBackPressed();
    }

    public void gotoPhoneVerification(View view) {
        if (!validateName() | !validateDOB() | !validateGender() | !validateCity()) {
            return;
        }
        String _name = name.getEditText().getText().toString();
        String _dob = dob.getMasked().trim();
        String _city  = city.getText().toString();
        String _gender  = gen.getText().toString();
        String key = "patient";

        Intent intent = new Intent(getApplicationContext(), MobileRegistration.class);
        intent.putExtra("full_name", _name);
        intent.putExtra("dob", _dob);
        intent.putExtra("gender", _gender);
        intent.putExtra("city", _city);
        intent.putExtra("key", key);
        startActivity(intent);

    }
}