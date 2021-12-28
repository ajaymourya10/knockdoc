package com.udit.testing.patient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.udit.testing.Model.CustomDialog;
import com.udit.testing.Model.sessions.PatientSession;
import com.udit.testing.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PatientSignIn extends AppCompatActivity {

    private TextInputLayout phoneNo, password;
    private Button login, newUser, forgetPwd;
    private CheckBox rememberMe;
    private CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_singn_in);

        phoneNo = (TextInputLayout) findViewById(R.id.patient_phone_no_login_page);
        password = (TextInputLayout) findViewById(R.id.patient_password_login_page);
        login = findViewById(R.id.patient_login_button);
        newUser = findViewById(R.id.new_user_patient_login);
        forgetPwd = findViewById(R.id.forget_pwd_patient);
        rememberMe = findViewById(R.id.patient_remember_me);
        dialog = new CustomDialog(PatientSignIn.this);

        PatientSession sessionManager = new PatientSession(this,PatientSession.PATIENT_REMEMBER_ME_SESSION);
        HashMap<String, String> doctorDetails = sessionManager.getRememberMeDetailFromSession();
        String _mobileNo = doctorDetails.get(PatientSession.KEY_PATIENT_SESSION_PHONE_NUMBER);
        String _password = doctorDetails.get(PatientSession.KEY_PATIENT_SESSION_PASSWORD);
        phoneNo.getEditText().setText(_mobileNo);
        password.getEditText().setText(_password);

        //dialog.dismissDialog();
        forgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ForgetPatientPassword.class));
            }
        });

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PatientRegistration.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateMobile() | !validatePassword()) {
                    return;
                }

                //dialog.startLoadingDialog();
                getDataFromDataBase();
            }
        });
    }

    private void getDataFromDataBase() {
        dialog.startLoadingDialog();
        final String mobileNo = "+91" + phoneNo.getEditText().getText().toString().trim();
        final String pwd = password.getEditText().getText().toString().trim();

        if (rememberMe.isChecked()) {
            PatientSession session = new PatientSession(this, PatientSession.PATIENT_REMEMBER_ME_SESSION);
            session.createRememberMESession(phoneNo.getEditText().getText().toString(),
                    password.getEditText().getText().toString());

        }

        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").child("Patient").
                orderByChild("mobile_no").equalTo(mobileNo);
        // Toast.makeText(getApplicationContext(), "Ajay1", Toast.LENGTH_LONG).show();
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //  Toast.makeText(getApplicationContext(), "Ajay2", Toast.LENGTH_LONG).show();
                if (snapshot.exists()) {
                    phoneNo.setError(null);
                    phoneNo.setErrorEnabled(false);
                    //  Toast.makeText(getApplicationContext(), "Ajay2", Toast.LENGTH_LONG).show();

                    String systemPassword = snapshot.child(mobileNo).child("password").getValue().toString();
                    if (systemPassword.equals(pwd)) {
                        password.setError(null);
                        password.setErrorEnabled(false);

                        String _name = snapshot.child(mobileNo).child("name").getValue().toString();
                        String _city = snapshot.child(mobileNo).child("city").getValue().toString();
                        String _password = snapshot.child(mobileNo).child("password").getValue().toString();
                        String _gender = snapshot.child(mobileNo).child("gender").getValue().toString();
                        String _dob = snapshot.child(mobileNo).child("dob").getValue().toString();
                        String _email = snapshot.child(mobileNo).child("email").getValue().toString();
                        String _phoneNo = snapshot.child(mobileNo).child("mobile_no").getValue().toString();
                        String _profile_pic = snapshot.child(mobileNo).child("profile_image").getValue().toString();
                        String _token=snapshot.child(mobileNo).child("token").getValue().toString();

                        Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_LONG).show();

                        PatientSession session = new PatientSession(PatientSignIn.this, PatientSession.PATIENT_LOGIN_SESSION);
                        session.createLoginSession(_name, _phoneNo, _city, _password, _gender, _dob, _email, _profile_pic,_token);

                        Intent intent = new Intent(PatientSignIn.this, PatientDashboard.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        dialog.dismissDialog();
                        password.setError("Incorrect Password!");
                        //Toast.makeText(getApplicationContext(), "Password does Not Matched!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    dialog.dismissDialog();
                    phoneNo.setError("Mobile Number is not Correct!");
                    //Toast.makeText(getApplicationContext(), "User does not Exists!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private Boolean validateMobile() {
        String _mobNo = phoneNo.getEditText().getText().toString();
        if (_mobNo.isEmpty()) {
            phoneNo.setError("Field can't be Empty!");
            return false;
        } else {
            phoneNo.setError(null);
            phoneNo.setErrorEnabled(false);
            return true;
        }
    }


    private Boolean validatePassword() {
        String _pwd = password.getEditText().getText().toString();
        if (_pwd.isEmpty()) {
            password.setError("Field can't be Empty!");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

}