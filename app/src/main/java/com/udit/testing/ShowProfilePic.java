package com.udit.testing;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

public class ShowProfilePic extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imageView;
    private String key="none",name="none";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile_pic_patient);

        imageView = findViewById(R.id.userPic);

        toolbar = findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile Photo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        toolbar.setBackgroundColor(Color.BLACK);


        Intent intent = getIntent();
        String _profile_pic = intent.getStringExtra("key");
        String key = intent.getStringExtra("key1");
        String name = intent.getStringExtra("key2");

        if (key.equals("doctor")){
            getSupportActionBar().setTitle("Dr. " + name);
        }else {
            getSupportActionBar().setTitle("Profile Photo");
        }
        Picasso.get().load(_profile_pic).placeholder(R.drawable.doctor_pic).into(imageView);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}