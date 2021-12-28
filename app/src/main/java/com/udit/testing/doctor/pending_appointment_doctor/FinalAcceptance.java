package com.udit.testing.doctor.pending_appointment_doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;
import com.udit.testing.R;
import com.udit.testing.doctor.DoctorDashboard;
import com.udit.testing.messaging.FcmNotificationsSender;

import de.hdodenhof.circleimageview.CircleImageView;

public class FinalAcceptance extends AppCompatActivity {

    private Toolbar toolbar;
    private CircleImageView ptPicIV, drPicIV;
    private TextView ptNameTextBox, drNameTextBox, drTypeTextBox, date, sendText, ifCancel;
    private Button gotoHome;
    private String drName, ptName, docType, appointmentDate, drPic, ptPic,token,pt_dob,pt_gen,pt_mob,count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_acceptance);

        toolbar = findViewById(R.id.done_appointment_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Appointment Approved!");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gotoHome = findViewById(R.id.go_to_home);
        ptPicIV = findViewById(R.id.pt_pic_done_appointment);
        drPicIV = findViewById(R.id.dr_pic_done_appointment);

        ptNameTextBox = findViewById(R.id.pt_name_done_appointment);
        drNameTextBox = findViewById(R.id.dr_name_done_appointment);
        drTypeTextBox = findViewById(R.id.dr_type_done_appointment);
        date = findViewById(R.id.date_done_appointment);
        sendText = findViewById(R.id.done_appointment_text);
        ifCancel = findViewById(R.id.if_doc_cancel);

        Intent intent = getIntent();
        ptName = intent.getStringExtra("ptname");
        drName = intent.getStringExtra("docname");
        appointmentDate = intent.getStringExtra("ad");
        docType = intent.getStringExtra("doctype");
        drPic = intent.getStringExtra("docpic");
        ptPic = intent.getStringExtra("ptpic");
        token=intent.getStringExtra("token");
        pt_mob=intent.getStringExtra("ptmobile");
        pt_dob=intent.getStringExtra("ptdob");
        pt_gen=intent.getStringExtra("ptgender");
        count=intent.getStringExtra("count");


        drNameTextBox.setText("Dr."+drName);
        ptNameTextBox.setText("Mr."+ptName);
        date.setText("Appointment Date: "+appointmentDate);
        drTypeTextBox.setText(docType);
        Picasso.get().load(drPic).placeholder(R.drawable.doctor_pic).into(drPicIV);
        Picasso.get().load(ptPic).placeholder(R.drawable.doctor_pic).into(ptPicIV);

        String title="Appointment";
        String message="Dr."+drName+" has accepted your appointment request";

        FcmNotificationsSender notificationsSender=new FcmNotificationsSender(token,
                title,message,getApplicationContext(), FinalAcceptance.this);
        notificationsSender.SendNotifications();

        sendText.setText("You have accepted an appointment request for " + ptName + " and the notification has been sent to "+ptName+" in regard of this.");
        gotoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DoctorDashboard.class));
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), PendingAppointmentDoctorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}