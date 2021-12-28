package com.udit.testing.doctor;

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
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.udit.testing.ChangePassword;
import com.udit.testing.Home;
import com.udit.testing.Model.sessions.SessionManager;
import com.udit.testing.R;
import com.udit.testing.doctor.pending_appointment_doctor.PendingAppointmentDoctorActivity;
import com.udit.testing.doctor.prescription.DoctorPrescription;
import com.udit.testing.doctor.prescription.RequestedPres;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class  DoctorDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String firstName, secondName, degree;
    private TextView name, welcome;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menuIcon;
    CardView appointment;
    String _name , _registrationNo, _dob ,_gender,_mobileNo, _password, _profile_pic,_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        appointment=findViewById(R.id.appoint);

        navigationView.setNavigationItemSelectedListener(this);

        //logout = findViewById(R.id.doctor_logout_btn);
        name = findViewById(R.id.doctor_name_dashboard);
        welcome = findViewById(R.id.welcome);
        //degreeText = findViewById(R.id.doctorDegreeOnDashBoard);
        menuIcon = findViewById(R.id.dashboard_menu_doctor);

        //getIntentFromLogin();
        setDataOnDashboard();
        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoctorDashboard.this, PendingAppointmentDoctorActivity.class));
            }
        });


        SessionManager sessionManager = new SessionManager(this, SessionManager.USER_LOGIN_SESSION);
        HashMap<String, String> doctorDetails = sessionManager.getUserDetailFromSession();

        _name = doctorDetails.get(SessionManager.KEY_NAME);
        _registrationNo = doctorDetails.get(SessionManager.KEY_REGISTRATION_NO);
        _dob = doctorDetails.get(SessionManager.KEY_DOB);
        _gender = doctorDetails.get(SessionManager.KEY_GENDER);
        _mobileNo = doctorDetails.get(SessionManager.KEY_PHONE_NUMBER);
        _password = doctorDetails.get(SessionManager.KEY_PASSWORD);
        _profile_pic = doctorDetails.get(SessionManager.KEY_LOGIN_STATUS);
        _email = doctorDetails.get(SessionManager.KEY_EMAIL);

        name.setText("Dr. " + _name);

        navigationDrawer();


    }

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.username);
        TextView navUserBio = (TextView) headerView.findViewById(R.id.userbio);
        CircleImageView imageView = headerView.findViewById(R.id.userDp);
        navUsername.setText("Dr."+_name);
        navUserBio.setText("Reg No: "+ _registrationNo);
        navUsername.setTextSize(16);
        navUserBio.setTextSize(13);

        if(!_profile_pic.equals("none")){
            Picasso.get().load(_profile_pic).placeholder(R.drawable.doctor_pic).into(imageView);
        }else{
            Picasso.get().load(R.drawable.doctor_pic).into(imageView);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),EditProfileDoctor.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });


        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_home_doc: {
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            }

            case R.id.nav_edit_profile_doc: {
                startActivity(new Intent(getApplicationContext(), EditProfileDoctor.class));
                break;
            }
            case R.id.nav_edit_profile_clinic: {

                startActivity(new Intent(getApplicationContext(), EditDoctorClinic.class));
                break;
            }

            case R.id.nav_change_password_doc: {
                startActivity(new Intent(getApplicationContext(), ChangePassword.class));
                break;
            }

            case R.id.nav_appointment_requests_doc: {
                startActivity(new Intent(getApplicationContext(),PendingAppointmentDoctorActivity.class));
                //do something
                break;
            }


            case R.id.nav_view_appointments_doc: {
                startActivity(new Intent(getApplicationContext(), RequestedPres.class));
                //do something
                break;
            }


            case R.id.nav_log_out_doc: {
                logoutFun();
                break;
            }

        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    private void logoutFun() {
        SessionManager sessionManager = new SessionManager(this, SessionManager.USER_LOGIN_SESSION);

        AlertDialog.Builder builder = new AlertDialog.Builder(DoctorDashboard.this);
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

    private void setDataOnDashboard() {

    }
    public void gotoprofile(View view)
    {
        startActivity(new Intent(getApplicationContext(),ViewProfileDoc.class));
    }
    public void gotoEditClinic(View view)
    {
        startActivity(new Intent(getApplicationContext(),ViewRating.class));
    }
    public void gotoPrescription(View view)
    {
     startActivity(new Intent(getApplicationContext(), DoctorPrescription.class));
    }

    public void gotoAppointments(View view) {
        startActivity(new Intent(getApplicationContext(), PendingAppointmentDoctorActivity.class));
    }
}