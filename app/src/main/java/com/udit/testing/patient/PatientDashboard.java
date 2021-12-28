package com.udit.testing.patient;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.udit.testing.ChangePassword;
import com.udit.testing.Home;
import com.udit.testing.Model.sessions.PatientSession;
import com.udit.testing.R;
import com.udit.testing.doctor.Doc_Speciality;
import com.udit.testing.patient.appointment_info.PendingAppointmentPatient;
import com.udit.testing.patient.doctors_info.DoctorsList;
import com.udit.testing.patient.prescription.PrescriptionView;
import com.udit.testing.patient.prescription.RequestPrescription;
import com.udit.testing.patient.rating.Approved;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView name;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menuIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);
       // FirebaseMessaging.getInstance().subscribeToTopic("all");
        drawerLayout = findViewById(R.id.patient_drawer_layout);
        navigationView = findViewById(R.id.patient_nav_view);


        name = findViewById(R.id.patient_name_dashboard);
        //logout = findViewById(R.id.patientLogout);
        menuIcon = findViewById(R.id.dashboard_menu_patient);

        PatientSession sessionManager = new PatientSession(this, PatientSession.PATIENT_LOGIN_SESSION);
        HashMap<String, String> doctorDetails = sessionManager.getUserDetailFromSession();

        String _name = doctorDetails.get(PatientSession.KEY_NAME);
        String _city = doctorDetails.get(PatientSession.KEY_CITY);
        String _dob = doctorDetails.get(PatientSession.KEY_DOB);
        String _gender = doctorDetails.get(PatientSession.KEY_GENDER);
        String _mobileNo = doctorDetails.get(PatientSession.KEY_PHONE_NUMBER);
        String _password = doctorDetails.get(PatientSession.KEY_PASSWORD);
        String _profile_pic = doctorDetails.get(PatientSession.KEY_LOGIN_STATUS);
        String _email = doctorDetails.get(PatientSession.KEY_EMAIL);

        name.setText(_name);

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.username);
        TextView navUserBio = (TextView) headerView.findViewById(R.id.userbio);
        CircleImageView imageView = headerView.findViewById(R.id.userDp);
        navUsername.setText(_name);
        navUserBio.setText("City: "+ _city);


        if(!_profile_pic.equals("none")){
            Picasso.get().load(_profile_pic).placeholder(R.drawable.doctor_pic).into(imageView);
        }else{
            Picasso.get().load(R.drawable.doctor_pic).into(imageView);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),EditProfilePatient.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else
                    drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else
                    drawerLayout.openDrawer(GravityCompat.START);
            }
        });



    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();

    }

    private void setDataOnDashboard() {

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_home: {
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            }

            case R.id.nav_edit_profile: {
                startActivity(new Intent(getApplicationContext(), EditProfilePatient.class));
                break;
            }

            case R.id.nav_edit_profile_clinic: {
                startActivity(new Intent(getApplicationContext(), EditProfilePatient.class));
                break;
            }

            case R.id.nav_change_password: {
                startActivity(new Intent(getApplicationContext(), ChangePassword.class));
                break;
            }

            case R.id.nav_appointment_requests: {
                startActivity(new Intent(this, Approved.class));
                //do something
                break;
            }

            case R.id.nav_accepted_requests: {
                startActivity(new Intent(this, RequestPrescription.class));
                //do something
                break;
            }

            case R.id.nav_view_appointments: {
                //do something
                startActivity(new Intent(PatientDashboard.this, PrescriptionView.class));
                break;
            }

//            case R.id.nav_share: {
//               startActivity(new Intent(PatientDashboard.this, Approved.class));
//                break;
//            }

            case R.id.nav_log_out: {
                logoutFun();
                break;
            }

        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutFun() {

        PatientSession sessionManager = new PatientSession(this, PatientSession.PATIENT_LOGIN_SESSION);

        AlertDialog.Builder builder = new AlertDialog.Builder(PatientDashboard.this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure want to LOGOUT?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sessionManager.logout();
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void gotoTest(View view) {
        Intent intent=new Intent(getApplicationContext(),DoctorsList.class);
        intent.putExtra("key","all");
        startActivity(intent);
//        startActivity(new Intent(getApplicationContext(), DoctorsList.class));
    }

    public void gotoDocDetail(View view) {
        startActivity(new Intent(getApplicationContext(), Doc_Speciality.class));
    }

    public void gotoAppointments(View view) {
        startActivity(new Intent(getApplicationContext(), PendingAppointmentPatient.class));
    }
}