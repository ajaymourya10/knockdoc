package com.udit.testing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.udit.testing.Model.sessions.PatientSession;
import com.udit.testing.Model.sessions.SessionManager;
import com.udit.testing.doctor.DoctorDashboard;
import com.udit.testing.patient.PatientDashboard;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ChangePassword extends AppCompatActivity {
    private Toolbar toolbar;
    private TextInputLayout oldPwd,newPwd,confirmPwd;
    private String _name, _email, _dob, _city, _gender, _mobileNo, _password, _profile_pic,_reg_no, key="",_token,_token2;
    private Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_patient);

        SessionManager sessionManager1 = new SessionManager(this, SessionManager.USER_LOGIN_SESSION);
        PatientSession session1 = new PatientSession(this, PatientSession.PATIENT_LOGIN_SESSION);

        if (session1.checkLogin()) {
            key = "patient";
        } else if (sessionManager1.checkLogin()) {
            key = "doctor";
        }

        oldPwd = findViewById(R.id.old_password_patient);
        newPwd = findViewById(R.id.new_password_patient);
        confirmPwd = findViewById(R.id.new_password_patient_re_enter);

        change = findViewById(R.id.change_password_btn);

        toolbar = findViewById(R.id.patient_toolbar_change_pwd);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(key.equals("patient")){
            PatientSession sessionManager = new PatientSession(this, PatientSession.PATIENT_LOGIN_SESSION);
            HashMap<String, String> doctorDetails = sessionManager.getUserDetailFromSession();

            _name = doctorDetails.get(PatientSession.KEY_NAME);
            _city = doctorDetails.get(PatientSession.KEY_CITY);
            _dob = doctorDetails.get(PatientSession.KEY_DOB);
            _gender = doctorDetails.get(PatientSession.KEY_GENDER);
            _mobileNo = doctorDetails.get(PatientSession.KEY_PHONE_NUMBER);
            _password = doctorDetails.get(PatientSession.KEY_PASSWORD);
            _profile_pic = doctorDetails.get(PatientSession.KEY_LOGIN_STATUS);
            _email = doctorDetails.get(PatientSession.KEY_EMAIL);
            _token=doctorDetails.get(PatientSession.KEY_TOKEN);



            change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!validatePassword() | !validateConfirmPassword() ) {
                        return;
                    }

                    if(!oldPwd.getEditText().getText().toString().equals(_password+"")){
                        oldPwd.setError("Password doesn't Match");
                    } else {
                        ProgressDialog dialog = new ProgressDialog(ChangePassword.this);
                        dialog.setCancelable(false);
                        dialog.setTitle("Changing Password");
                        dialog.setMessage("Please wait...");
                        dialog.show();
                        oldPwd.setError(null);
                        oldPwd.setErrorEnabled(false);
                        confirmPwd.setError(null);
                        confirmPwd.setErrorEnabled(false);
                        String newPassword = newPwd.getEditText().getText().toString();
                        FirebaseDatabase.getInstance().getReference()
                                .child("Users").child("Patient").child(_mobileNo).child("password")
                                .setValue(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                PatientSession session = new PatientSession(ChangePassword.this, PatientSession.PATIENT_LOGIN_SESSION);
                                session.logout();
                                session.createLoginSession(_name, _mobileNo, _city, newPassword, _gender, _dob, _email, "login",_token);
                                Toast.makeText(getApplicationContext(),"Password Changed",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), PatientDashboard.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });


                    }
                }
            });

        }
        else if(key.equals("doctor")){
            SessionManager sessionManager = new SessionManager(this, SessionManager.USER_LOGIN_SESSION);
            HashMap<String, String> doctorDetails = sessionManager.getUserDetailFromSession();

            _name = doctorDetails.get(SessionManager.KEY_NAME);
            _reg_no = doctorDetails.get(SessionManager.KEY_REGISTRATION_NO);
            _dob = doctorDetails.get(SessionManager.KEY_DOB);
            _gender = doctorDetails.get(SessionManager.KEY_GENDER);
            _mobileNo = doctorDetails.get(SessionManager.KEY_PHONE_NUMBER);
            _password = doctorDetails.get(SessionManager.KEY_PASSWORD);
            _profile_pic = doctorDetails.get(SessionManager.KEY_LOGIN_STATUS);
            _email = doctorDetails.get(SessionManager.KEY_EMAIL);
            _token2=doctorDetails.get(SessionManager.KEY_TOKEN);


            change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!validatePassword() | !validateConfirmPassword() ) {
                        return;
                    }

                    if(!oldPwd.getEditText().getText().toString().equals(_password+"")){
                        oldPwd.setError("Password doesn't Match");
                    } else {
                        ProgressDialog dialog = new ProgressDialog(ChangePassword.this);
                        dialog.setCancelable(false);
                        dialog.setTitle("Changing Password");
                        dialog.setMessage("Please wait...");
                        dialog.show();

                        confirmPwd.setError(null);
                        confirmPwd.setErrorEnabled(false);
                        oldPwd.setError(null);
                        oldPwd.setErrorEnabled(false);
                        final String newPassword = newPwd.getEditText().getText().toString();
                        FirebaseDatabase.getInstance().getReference()
                                .child("Users").child("Doctor").child(_mobileNo).child("password")
                                .setValue(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                SessionManager session = new SessionManager(ChangePassword.this, SessionManager.USER_LOGIN_SESSION);
                                session.logout();
                                session.createLoginSession(_name,_mobileNo,_reg_no,newPassword,_gender,_dob,_email, _profile_pic,_token2);
                                Toast.makeText(getApplicationContext(),"Password Changed",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(),DoctorDashboard.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });


                    }



                }
            });

        }

    }


    private Boolean validatePassword() {
        String _pwd = newPwd.getEditText().getText().toString();
        if (_pwd.isEmpty()) {
            newPwd.setError("Password Can't be Empty!");
            return false;
        } else if (!_pwd.isEmpty() && _pwd.length() < 6) {
            newPwd.setError("Password Should contains atleast 6 Character");
            return false;
        } else {
            newPwd.setError(null);
            newPwd.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateConfirmPassword() {
        String _pwd = newPwd.getEditText().getText().toString();
        String _confirmPwd = confirmPwd.getEditText().getText().toString();
        if (!_pwd.equals(_confirmPwd)) {
            confirmPwd.setError("Password doesn't Match");
            return false;
        } else {
            confirmPwd.setError(null);
            confirmPwd.setErrorEnabled(false);
            return true;
        }
    }

}