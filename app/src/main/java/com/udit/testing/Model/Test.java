package com.udit.testing.Model;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.udit.testing.R;
import com.squareup.picasso.Picasso;

public class Test extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        imageView = findViewById(R.id.image_see);
        textView = findViewById(R.id.textview);

        Intent intent = getIntent();
        String  name = intent.getStringExtra("name");
        String  image = intent.getStringExtra("profile_image");
        String  mobileNo = intent.getStringExtra("mobile_no");

        textView.setText(name+" \n"+mobileNo);
        Picasso.get().load(image).placeholder(R.drawable.doctor_pic).into(imageView);

    }

}
