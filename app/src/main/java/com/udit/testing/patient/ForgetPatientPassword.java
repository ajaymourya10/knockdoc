package com.udit.testing.patient;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.udit.testing.ForgetPassword;
import com.udit.testing.Model.CustomDialog;
import com.udit.testing.PasswordSetup;
import com.udit.testing.R;

import java.util.concurrent.TimeUnit;

public class ForgetPatientPassword extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputLayout edtPhone, edtOTP;
    private ProgressBar progressBar;
    private Button verifyOTPBtn, generateOTPBtn, didNotGetOTP;
    private String verificationId;
    private String name, registrationNo, dob, gender;
    private CustomDialog dialog;
    private TextView textView,enterPhoneText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        mAuth = FirebaseAuth.getInstance();

        // initializing variables for button and Edittext.
        edtPhone = findViewById(R.id.doctor_mobile_no_forget_pwd);
        edtOTP = findViewById(R.id.doctor_mobile_no_otp_forget_pwd);
        verifyOTPBtn = findViewById(R.id.doctor_mobile_no_otp_verify_btn_forget_pwd);
        generateOTPBtn = findViewById(R.id.doctor_mobile_no_verify_btn_forget_pwd);
        progressBar = findViewById(R.id.progress_bar_doctor_otp_forget_pwd);
        textView = findViewById(R.id.searching_button_forget_pwd);
        enterPhoneText = findViewById(R.id.enter_otp_text);

        didNotGetOTP = findViewById(R.id.did_not_get_otp_forget_pwd);

        dialog = new CustomDialog(ForgetPatientPassword.this);

        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        verifyOTPBtn.setVisibility(View.GONE);
        edtOTP.setVisibility(View.GONE);
        didNotGetOTP.setVisibility(View.GONE);
        enterPhoneText.setVisibility(View.GONE);


        generateOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtPhone.getEditText().getText().toString())) {
                    edtPhone.setError("Invalid Phone No");
                    edtPhone.setErrorEnabled(true);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    //String phone = "+91" + edtPhone.getEditText().getText().toString().trim();
                    final String mobileNo = "+91" + edtPhone.getEditText().getText().toString().trim();
                    //final String pwd = password.getEditText().getText().toString().trim();


                    Query checkUser = FirebaseDatabase.getInstance().getReference("Users").child("Patient").
                            orderByChild("mobile_no").equalTo(mobileNo);
                    // Toast.makeText(getApplicationContext(), "Ajay1", Toast.LENGTH_LONG).show();
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //  Toast.makeText(getApplicationContext(), "Ajay2", Toast.LENGTH_LONG).show();
                            if (snapshot.exists()) {
                                edtPhone.setError(null);
                                edtPhone.setErrorEnabled(false);
                                progressBar.setVisibility(View.GONE);
                                textView.setVisibility(View.GONE);
                                sendVerificationCode(mobileNo);
                                //  Toast.makeText(getApplicationContext(), "Ajay2", Toast.LENGTH_LONG).show();


                            } else {
                                edtPhone.setError("No User with this Mobile No");
                                progressBar.setVisibility(View.GONE);
                                textView.setVisibility(View.GONE);
                                //Toast.makeText(getApplicationContext(), "User does not Exists!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
        });


        verifyOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validating if the OTP text field is empty or not.
                if (TextUtils.isEmpty(edtOTP.getEditText().getText().toString())) {
                    // if the OTP text field is empty display
                    // a message to user to enter OTP
                    Toast.makeText(getApplicationContext(), "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    // if OTP field is not empty calling
                    // method to verify the OTP.
                    dialog.startLoadingDialog();
                    verifyCode(edtOTP.getEditText().getText().toString());
                }
            }
        });
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
                            Intent intent = new Intent(getApplicationContext(), PasswordSetup.class);
                            intent.putExtra("key", "udit");
                            intent.putExtra("mobile_no", "+91"+edtPhone.getEditText().getText().toString());
                            dialog.dismissDialog();
                            startActivity(intent);
                            finish();
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void sendVerificationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        enterPhoneText.setVisibility(View.GONE);
        verifyOTPBtn.setVisibility(View.GONE);
        edtOTP.setVisibility(View.GONE);
        didNotGetOTP.setVisibility(View.GONE);
        generateOTPBtn.setEnabled(false);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                this, // this task will be excuted on Main thread.
                mCallBack // we are calling callback method when we recieve OTP for
                // auto verification of user.
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            progressBar.setVisibility(View.GONE);
            enterPhoneText.setVisibility(View.VISIBLE);

            String myText = "+91"+edtPhone.getEditText().getText().toString();
            String my = enterPhoneText.getText().toString();
            enterPhoneText.setTypeface(null, Typeface.BOLD);
            enterPhoneText.setText(my+myText);
            myText = "";
            //enterPhoneText.setText(enterPhoneText.getText()+"+91"+edtPhone.getEditText().getText().toString());
            verifyOTPBtn.setVisibility(View.VISIBLE);
            didNotGetOTP.setVisibility(View.VISIBLE);
            edtOTP.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            //generateOTPBtn.setVisibility(View.VISIBLE);
            generateOTPBtn.setEnabled(true);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();

            if (code != null) {
                edtOTP.getEditText().setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
            generateOTPBtn.setVisibility(View.VISIBLE);
            generateOTPBtn.setEnabled(true);
            didNotGetOTP.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            enterPhoneText.setVisibility(View.GONE);
        }
    };

    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        signInWithCredential(credential);
    }

    @Override
    protected void onStop() {
        mAuth.signOut();
        super.onStop();
    }


}
