package com.udit.testing;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.udit.testing.Model.Doctor;
import com.udit.testing.Model.Patient;

public class PasswordSetup extends AppCompatActivity {

    private String name, registrationNo, dob, gender, mobileNo, email, key = "xyz", city = "xyz",tok;
    private TextInputLayout password, confirm_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_password);

        password = findViewById(R.id.doctor_pwd);
        confirm_pwd = findViewById(R.id.doctor_confirm_pwd);


                        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
                    if (!TextUtils.isEmpty(token)) {
                        Log.d("toooooooo", "retrieve token successful : " + token);
                        tok=token;
                    } else{
                        Log.w("foooooooo", "token should not be null...");
                    }
                }).addOnFailureListener(e -> {
                    //handle e
                }).addOnCanceledListener(() -> {
                    //handle cancel
                }).addOnCompleteListener(task -> Log.v("sttttttt", "This is the token : " + task.getResult()));



        Intent intent = getIntent();
        name = intent.getStringExtra("full_name");
        registrationNo = intent.getStringExtra("reg_no");
        dob = intent.getStringExtra("dob");
        mobileNo = intent.getStringExtra("mobile_no");
        gender = intent.getStringExtra("gender");
        email = "Not mentioned";
        key = intent.getStringExtra("key");
        city = intent.getStringExtra("city");





    }

    public void gotoVerifiedPage(View view) {

        if (!validatePassword() | !validateConfirmPassword()) {
            return;
        }
        String _password = password.getEditText().getText().toString().trim();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database.getReference("Users").child("Patient").child(mobileNo);
        DatabaseReference myRef = database.getReference("Users").child("Doctor").child(mobileNo);
        if (key.equals("ajay")) {
            myRef.child("password").setValue(_password);

            // startActivity(new Intent(getApplicationContext(),UpdatedPassword.class));
            Intent intent = new Intent(getApplicationContext(), com.udit.testing.UpdatedPassword.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else if(key.equals("udit"))
        {
            myRef1.child("password").setValue(_password);

            // startActivity(new Intent(getApplicationContext(),UpdatedPassword.class));
            Intent intent = new Intent(getApplicationContext(), com.udit.testing.UpdatedPassword.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else {
            if (key.equals("patient")) {
//                FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                Patient patient = new Patient(name, _password, dob, gender, city, "none");
                patient.setMobile_no(mobileNo);
                patient.setEmail(email);
                patient.setToken(tok);
                myRef1.setValue(patient);
                Intent intent = new Intent(this, OTPSuccessful.class);
                intent.putExtra("key", key);
                intent.putExtra("name",name);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            } else if(key.equals("doctor")){
                String rate="5";
                Doctor doctor = new Doctor(name, registrationNo, _password, dob, gender);
                doctor.setMobile_no(mobileNo);
                doctor.setEmail(email);
                doctor.setToken(tok);
                doctor.setRating(rate);
                doctor.setProfile_image("Not Available");
                doctor.setDoctor_type("Not mentioned");
                doctor.setFee("Not available");
                doctor.setDegree("Not available");
                myRef.setValue(doctor);
                Intent intent = new Intent(this, OTPSuccessful.class);
                intent.putExtra("key", key);
                intent.putExtra("name",name);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //startActivity(new Intent(getApplicationContext(), OTPSuccessful.class));
            }


        }


    }

    private Boolean validatePassword() {
        String _pwd = password.getEditText().getText().toString();
        if (_pwd.isEmpty()) {
            password.setError("Password Can't be Empty!");
            return false;
        } else if (!_pwd.isEmpty() && _pwd.length() < 4) {
            password.setError("Password Should contains atleast 6 Character");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateConfirmPassword() {
        String _pwd = password.getEditText().getText().toString();
        String _confirmPwd = confirm_pwd.getEditText().getText().toString();
        if (!_pwd.equals(_confirmPwd)) {
            confirm_pwd.setError("Password doesn't Match");
            return false;
        } else {
            confirm_pwd.setError(null);
            confirm_pwd.setErrorEnabled(false);
            return true;
        }
    }


}