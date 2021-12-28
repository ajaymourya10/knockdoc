package com.udit.testing.doctor;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.udit.testing.ForgetPassword;
import com.udit.testing.Model.CustomDialog;
import com.udit.testing.Model.sessions.SessionManager;
import com.udit.testing.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class DoctorSignIn extends AppCompatActivity {


    private TextInputLayout emailOrMobileNo, password;
    private Button forgetPwd, newUser, login;
    private CustomDialog dialog;
    private TextView loginText, logintocontinue;
    private ImageView logo_doc_login;
    private CheckBox rememberMe;
     String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor_sign_in);
        emailOrMobileNo = (TextInputLayout) findViewById(R.id.doctor_login_mobno);
        password = (TextInputLayout) findViewById(R.id.doctor_login_password);

        forgetPwd = (Button) findViewById(R.id.forget_pwd_doctor);
        login = (Button) findViewById(R.id.doctor_login_button);
        newUser = (Button) findViewById(R.id.new_user_doctor_login);
        rememberMe = findViewById(R.id.doctor_remember_me);

        SessionManager sessionManager = new SessionManager(this,SessionManager.USER_REMEMBER_ME_SESSION);
        HashMap<String, String> doctorDetails = sessionManager.getRememberMeDetailFromSession();
        String _mobileNo = doctorDetails.get(SessionManager.KEY_SESSION_PHONE_NUMBER);
        String _password = doctorDetails.get(SessionManager.KEY_SESSION_PASSWORD);
       token=doctorDetails.get(SessionManager.KEY_TOKEN);
        emailOrMobileNo.getEditText().setText(_mobileNo);
        password.getEditText().setText(_password);



        dialog = new CustomDialog(DoctorSignIn.this);

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DoctorRegistration_1.class));
            }
        });

        forgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgetPassword.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if (!isInternetConnected(LoginDoctor.this)) {
                //  showCustomDialog();
                // }
                if (!validateMobile() | !validatePassword()) {
                    return;
                }
                getDataFromDataBase();
            }
        });
    }


    //    private boolean isInternetConnected(LoginDoctor loginDoctor) {
//
//        ConnectivityManager connectivityManager = (ConnectivityManager) loginDoctor.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo wifiConnection = connectivityManager.getNetworkCapabilities(connectivityManager.TYPE_WIFI);
//        NetworkInfo mobileConnection = connectivityManager.getNetworkCapabilities(connectivityManager.TYPE_MOBILE);
//
//        if ((wifiConnection != null && wifiConnection.isConnected()) || (mobileConnection != null && mobileConnection.isConnected())) {
//            return true;
//        }
//        else{
//            return false;
//        }
//
//    }


    private void getDataFromDataBase() {
        dialog.startLoadingDialog();
        final String mobileNo = "+91" + emailOrMobileNo.getEditText().getText().toString().trim();
        final String pwd = password.getEditText().getText().toString().trim();

        if(rememberMe.isChecked()){
            SessionManager sessionManager = new SessionManager(this,SessionManager.USER_REMEMBER_ME_SESSION);
            sessionManager.createRememberMESession(emailOrMobileNo.getEditText().getText().toString(),
                    password.getEditText().getText().toString());

        }

        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").child("Doctor").
                orderByChild("mobile_no").equalTo(mobileNo);
        // Toast.makeText(getApplicationContext(), "Ajay1", Toast.LENGTH_LONG).show();
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //  Toast.makeText(getApplicationContext(), "Ajay2", Toast.LENGTH_LONG).show();
                if (snapshot.exists()) {
                    emailOrMobileNo.setError(null);
                    emailOrMobileNo.setErrorEnabled(false);
                    //  Toast.makeText(getApplicationContext(), "Ajay2", Toast.LENGTH_LONG).show();

                    String systemPassword = snapshot.child(mobileNo).child("password").getValue().toString();
                    if (systemPassword.equals(pwd)) {
                        password.setError(null);
                        password.setErrorEnabled(false);

                        String _name = snapshot.child(mobileNo).child("name").getValue().toString();
                        String _registrationNo = snapshot.child(mobileNo).child("registration_no").getValue().toString();
                        String _password = snapshot.child(mobileNo).child("password").getValue().toString();
                        String _gender = snapshot.child(mobileNo).child("gender").getValue().toString();
                        String _dob = snapshot.child(mobileNo).child("dob").getValue().toString();
                        String _email = snapshot.child(mobileNo).child("email").getValue().toString();
                        String _phoneNo = snapshot.child(mobileNo).child("mobile_no").getValue().toString();
                        String _profile_pic = snapshot.child(mobileNo).child("profile_image").getValue().toString();
                        String _token=snapshot.child(mobileNo).child("token").getValue().toString();

                        Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_LONG).show();

                        SessionManager sessionManager = new SessionManager(DoctorSignIn.this, SessionManager.USER_LOGIN_SESSION);
                        sessionManager.createLoginSession(_name, _phoneNo, _registrationNo, _password, _gender, _dob, _email, _profile_pic,_token);

                        Intent intent = new Intent(DoctorSignIn.this, DoctorDashboard.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        dialog.dismissDialog();
                        password.setError("Incorrect Password!");
                        //Toast.makeText(getApplicationContext(), "Password does Not Matched!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    dialog.dismissDialog();
                    emailOrMobileNo.setError("Mobile Number is not Correct!");
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
        String _mobNo = emailOrMobileNo.getEditText().getText().toString();
        if (_mobNo.isEmpty()) {
            emailOrMobileNo.setError("Field can't be Empty!");
            return false;
        } else {
            emailOrMobileNo.setError(null);
            emailOrMobileNo.setErrorEnabled(false);
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
