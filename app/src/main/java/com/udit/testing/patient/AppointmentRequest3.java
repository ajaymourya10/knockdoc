package com.udit.testing.patient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;
import com.udit.testing.R;
import com.udit.testing.messaging.FcmNotificationsSender;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppointmentRequest3 extends AppCompatActivity {

    private TextView docName, ptName, degree, type, fee, ptDob, ptGender, appointmentDate;
    private CheckBox verifyDetails;
    public CircleImageView docImage, ptImage;
    private Toolbar toolbar;
    private Button bookAppointment;
    DatabaseReference current;
    private String _docName, _degree, _type, _docDp, _fee,doc_token;
    private int x;
    private String _name, _mobileNo, _gender,tok, _ptPic,rating, _address, _dob, _day, _appointmentDate, _aadharNo, _doctorMobileNo, _timeSlot,pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_request3);
        FirebaseMessaging.getInstance().subscribeToTopic("all");

        settingFindViewByID();
        gettingDataFromIntent();

        bookAppointment.setClickable(verifyDetails.isChecked());

        settingData();


        settingOnClick();

    }

    public void settingData() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ptName.setText(_name);
        ptDob.setText("DOB: " + _dob);
        ptGender.setText("Gender: " + _gender);
        appointmentDate.setText("Appointment Date: " + _appointmentDate);

        current = FirebaseDatabase.getInstance().getReference()
                .child("Users").child("Patient").child(_mobileNo);


        current.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String count = snapshot.child("count").getValue().toString();
                    x = Integer.parseInt(count);
                    x = x + 1;
                    //Toast.makeText(AppointmentRequest3.this, "count " + x, Toast.LENGTH_SHORT).show();

                } else {
                    x = 0;
                    Toast.makeText(AppointmentRequest3.this, "count " + x, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference checkUser = FirebaseDatabase.getInstance().getReference().child("Users").child("Doctor")
                .child(_doctorMobileNo);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    _docName = snapshot.child("name").getValue().toString();
                    _degree = snapshot.child("degree").getValue().toString();
                    _type = snapshot.child("doctor_type").getValue().toString();
                    _docDp = snapshot.child("profile_image").getValue().toString();
                    _fee = snapshot.child("fee").getValue().toString();
                    doc_token=snapshot.child("token").getValue().toString();

                    docName.setText("Dr. " + _docName);
                    degree.setText(_degree);
                    type.setText(_type);
                    fee.setText("Doctor's Fee: " + _fee + " \u20B9");

                    if (!_docDp.equals("none")) {
                        Picasso.get().load(_docDp).placeholder(R.drawable.doctor_pic).into(docImage);
                    } else {
                        Picasso.get().load(R.drawable.doctor_pic).into(docImage);
                    }

                    DatabaseReference setDp = FirebaseDatabase.getInstance().getReference().child("Appointment")
                            .child("pending").child(_mobileNo);

                    setDp.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                _ptPic = snapshot.child("image").getValue().toString();
                                Picasso.get().load(_ptPic).placeholder(R.drawable.doctor_pic).into(ptImage);
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });


    }


    private void gettingDataFromIntent() {

        Intent intent = getIntent();
        _name = intent.getStringExtra("name");
        _dob = intent.getStringExtra("dob");
        _appointmentDate = intent.getStringExtra("appointment_date");
        _address = intent.getStringExtra("address");
        _aadharNo = intent.getStringExtra("aadhar");
        _doctorMobileNo = intent.getStringExtra("doctor_mobile");
        _gender = intent.getStringExtra("gender");
        _day = intent.getStringExtra("day");
        _timeSlot = intent.getStringExtra("time_slot");
        _mobileNo = intent.getStringExtra("mobile");
        rating = intent.getStringExtra("rating");
        tok=intent.getStringExtra("token");
        pos=intent.getStringExtra("position");
        Log.d("timmmmm","time "+_timeSlot);



    }

    private void settingOnClick() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        bookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title="Knockdoc";
                String message=_name+" has send you an appointment request ";

                FcmNotificationsSender notificationsSender=new FcmNotificationsSender(doc_token,
                        title,message,getApplicationContext(),AppointmentRequest3.this);
                notificationsSender.SendNotifications();

                ProgressDialog dialog = new ProgressDialog(AppointmentRequest3.this);
                dialog.setTitle("Please Wait...");
                dialog.setCancelable(false);
                dialog.show();



                DatabaseReference doctorRef = FirebaseDatabase.getInstance().getReference()
                        .child("Appointments").child("pending").child("doctor").child(_doctorMobileNo).child(_mobileNo + "_" + x);

                DatabaseReference patientRef = FirebaseDatabase.getInstance().getReference()
                        .child("Appointments").child("pending").child("patient").child(_mobileNo).child(x + "");


                //doctorRef.child(_mobileNo + _name);
                Map<String, String> hasMap = new HashMap<>();
                hasMap.put("patient_name", _name);
                hasMap.put("patient_image", _ptPic);
                hasMap.put("patient_dob", _dob);
                hasMap.put("appointment_date", _appointmentDate);
                hasMap.put("appointment_day", _day);
                hasMap.put("patient_mobile", _mobileNo);
                hasMap.put("count", x + "");
                hasMap.put("patient_gender", _gender);
                hasMap.put("patient_address", _address);
                hasMap.put("patient_aadhar", _aadharNo);
                hasMap.put("time_slot", _timeSlot);
                hasMap.put("status", "received");
               hasMap.put("token",tok);


                doctorRef.setValue(hasMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //patientRef.child(_doctorMobileNo);
                        Map<String, String> val = new HashMap<>();
                        val.put("doctor_name", _docName);
                        val.put("degree", _degree);
                        val.put("rating", rating);
                        val.put("appointment_date", _appointmentDate);
                        val.put("appointment_day", _day);
                        val.put("doctor_type", _type);
                        val.put("doctor_mobile", _doctorMobileNo);
                        val.put("doctor_fee", _fee);
                        val.put("count", x + "");
                        val.put("patient_name", _name);
                        val.put("time_slot", _timeSlot);
                        val.put("gender", _gender);
                        val.put("patient_dob", _dob);
                        val.put("doctor_dp", _docDp);
                        val.put("patient_dp", _ptPic);
                        val.put("token",tok);
                        val.put("doc_token",doc_token);
                        val.put("status", "sent");


                        patientRef.setValue(val).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                current.child("count").setValue(String.valueOf(x)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        FirebaseDatabase.getInstance().getReference().child("Appointment").removeValue()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        dialog.dismiss();
                                                        Intent intent = new Intent(getApplicationContext(), AppointmentDone.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        intent.putExtra("dr_name",_docName);
                                                        intent.putExtra("pt_name",_name);
                                                        intent.putExtra("appointment_date",_appointmentDate);
                                                        intent.putExtra("dr_type",_type);
                                                        intent.putExtra("dr_pic",_docDp);
                                                        intent.putExtra("drmobile",_doctorMobileNo);
                                                        intent.putExtra("pt_pic",_ptPic);
                                                        intent.putExtra("patient_mob",_mobileNo);
                                                        intent.putExtra("count",x);
                                                        intent.putExtra("gender",_gender);
                                                        intent.putExtra("position",pos);
                                                        intent.putExtra("timeslot",_timeSlot);

                                                        startActivity(intent);

                                                    }
                                                });


                                    }
                                });


                            }
                        });


                    }
                });



            }

        });

    }

    private void settingFindViewByID() {

        docName = findViewById(R.id.doc_name);
        ptName = findViewById(R.id.pt_name);
        degree = findViewById(R.id.doc_degree);
        type = findViewById(R.id.doc_type);
        fee = findViewById(R.id.doc_fee);
        ptDob = findViewById(R.id.pt_dob);
        ptGender = findViewById(R.id.pt_gender);
        appointmentDate = findViewById(R.id.pt_appointment_date);
        verifyDetails = findViewById(R.id.verify_details);
        bookAppointment = findViewById(R.id.book_appointment_btn);
        docImage = findViewById(R.id.doctor_pic);
        ptImage = findViewById(R.id.pt_pic);

        toolbar = findViewById(R.id.book_appointment_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Verify Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


}