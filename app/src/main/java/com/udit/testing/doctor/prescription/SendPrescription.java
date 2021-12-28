package com.udit.testing.doctor.prescription;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.udit.testing.Model.sessions.SessionManager;
import com.udit.testing.R;
import com.udit.testing.messaging.FcmNotificationsSender;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SendPrescription extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editText;
    private Button addImage, viewImage, send;
    private ImageView imageView;
    private Uri filePath;
    private String key = "xyz",download_url, patientName, patientPic, patientMobile, count, patientDob, appointmentdate, patientGender, _mobileNo,token,doc_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_prescription);

        Intent intent = getIntent();
        patientName = intent.getStringExtra("name");
        patientMobile = intent.getStringExtra("mobile_no");

        patientPic = intent.getStringExtra("image");
        count = intent.getStringExtra("count");
        patientDob = intent.getStringExtra("dob");
        appointmentdate = intent.getStringExtra("date");
        patientGender = intent.getStringExtra("gender");
        token=intent.getStringExtra("token");
       // Log.d("toooooo","hello"+patientMobile+" "+patientName+patientDob+patientGender+token);

        SessionManager sessionManager = new SessionManager(this, SessionManager.USER_LOGIN_SESSION);
        HashMap<String, String> doctorDetails = sessionManager.getUserDetailFromSession();

        _mobileNo = doctorDetails.get(SessionManager.KEY_PHONE_NUMBER);
        doc_name=doctorDetails.get(SessionManager.KEY_NAME);

        send = findViewById(R.id.send);
        addImage = findViewById(R.id.add_pic);
        viewImage = findViewById(R.id.view_pic);
        viewImage.setClickable(false);

        editText = findViewById(R.id.edtInput);
        imageView = findViewById(R.id.image);
        imageView.setVisibility(View.GONE);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Send Prescription");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        viewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (key.equals("abc")) {
                    if (imageView.getVisibility() == View.VISIBLE) {
                        imageView.setVisibility(View.GONE);
                        viewImage.setText("View Picture");
                    } else {
                        // hiden
                        imageView.setVisibility(View.VISIBLE);
                        viewImage.setText("Hide Picture");
                    }
                }
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewImage.setClickable(true);
                key = "abc";
                selectImage();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(key.equals("abc")) {
                    uploadImage();
                    String title="Prescription";
                    String message="Dr."+doc_name+" has send you some prescriptions.";

                    FcmNotificationsSender notificationsSender=new FcmNotificationsSender(token,
                            title,message,getApplicationContext(), SendPrescription.this);
                    notificationsSender.SendNotifications();
                    startActivity(new Intent(SendPrescription.this,DoctorPrescription.class));
                }
                else{
                    Toast.makeText(SendPrescription.this, "Please attach a picture", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void selectImage() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);

    }

    private void uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Please Wait...");
            progressDialog.setMessage("while your image is uploading into database...");
            progressDialog.setCancelable(false);
            progressDialog.show();
         //   Log.d("tooooooooooooo","hello"+patientMobile);

            // Defining the child of storageReference
            StorageReference ref = FirebaseStorage.getInstance().getReference()
                    .child("prescription/patient").child(patientMobile + count + ".jpg");
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference()
                    .child("Appointments").child("pending").child("patient").child(patientMobile).child(count);

            DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference()
                    .child("Appointments").child("successful").child("doctor").child(_mobileNo).child(patientMobile + "_" + count);


            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            download_url = uri.toString();
                                            // _profile_pic = download_url;

                                            Map<String, String> hasMap = new HashMap<>();
                                            hasMap.put("description", editText.getText().toString());
                                            hasMap.put("image", download_url);


                                            mDatabase.child("prescription")
                                                    .setValue(hasMap)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                mDatabaseRef.child("prescription").setValue(hasMap)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                progressDialog.dismiss();
                                                                                Toast.makeText(getApplicationContext(), "Prescription Sent Successfully", Toast.LENGTH_LONG).show();

                                                                            }
                                                                        });




                                                            } else {
                                                                Toast.makeText(getApplicationContext(), "Pic upload Fail", Toast.LENGTH_LONG).show();
                                                            }
                                                            progressDialog.dismiss();
                                                        }
                                                    });
                                        }
                                    });

                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(getApplicationContext(),
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                filePath = result.getUri();
                try {

                    // Setting image on image view using Bitmap
                    Bitmap bitmap = MediaStore
                            .Images
                            .Media
                            .getBitmap(
                                    getContentResolver(),
                                    filePath);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    // Log the exception
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}