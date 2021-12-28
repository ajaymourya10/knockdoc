package com.udit.testing.patient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;
import com.udit.testing.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppointmentDone extends AppCompatActivity {

    private Toolbar toolbar;
    private CircleImageView ptPicIV, drPicIV;
    private TextView ptNameTextBox, drNameTextBox, drTypeTextBox, date, sendText, ifCancel;
    private Button gotoHome;
    private String drName, ptName, docType, appointmentDate, drPic, ptPic,patient_mob,count,gender,des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_done);

        toolbar = findViewById(R.id.done_appointment_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Appointment Done!");
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
        ptName = intent.getStringExtra("pt_name");
        drName = intent.getStringExtra("dr_name");
        appointmentDate = intent.getStringExtra("appointment_date");
        docType = intent.getStringExtra("dr_type");
        drPic = intent.getStringExtra("dr_pic");
        ptPic = intent.getStringExtra("pt_pic");
        gender=intent.getStringExtra("gender");
       // String pos=intent.getStringExtra("position");
        String _timeSlot=intent.getStringExtra("timeslot");
        String _doctorMobileNo=intent.getStringExtra("drmobile");
        Log.d("timmmmmmmmm"," "+_timeSlot +" "+_doctorMobileNo);


//        DatabaseReference slot = FirebaseDatabase.getInstance().getReference()
//                .child("Appointments").child("TimeSlot").child(_doctorMobileNo).child(appointmentDate).child(pos);
//        Map<String,String > hash=new HashMap<>();
//        hash.put("time",_timeSlot);
//        hash.put("position",pos);
//        slot.setValue(hash);

        if(gender.equals("Male"))
            des="Mr.";
        else if(gender.equals("Female"))
            des="Mrs.";
        else
            des="";




        drNameTextBox.setText("Dr."+drName);
        ptNameTextBox.setText(des+ptName);
        date.setText("Appointment Date: "+appointmentDate);
        drTypeTextBox.setText(docType);
        Picasso.get().load(drPic).placeholder(R.drawable.doctor_pic).into(drPicIV);
        Picasso.get().load(ptPic).placeholder(R.drawable.doctor_pic).into(ptPicIV);

        sendText.setText("Your Appointment request for " + ptName + " has been sent to Dr. " + drName);
        ifCancel.setText("In case if Dr. "+drName+" cancelled your appointment request we will let you know!");

        gotoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PatientDashboard.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), PatientDashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}