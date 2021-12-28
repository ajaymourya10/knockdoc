package com.udit.testing.patient;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.santalu.maskara.widget.MaskEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.udit.testing.Model.sessions.PatientSession;
import com.udit.testing.R;
import com.udit.testing.patient.slotbooking.MyCalendarDoctorActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppointmentRequest extends AppCompatActivity {

    private CircleImageView profilePic;
    private EditText name, dob, appointmentDate, address;
    private AutoCompleteTextView gender;
    private MaskEditText aadharNo;
    private Button next, setDp;
    private StorageReference storageReference;
    private Uri filePath;
    private ImageView dobIcon, appointmentIcon;
    private String doctorMobileNo,token;
    private Toolbar toolbar;
    private String key = "xyz";
    private DatabaseReference databaseReference;
    private String _name, _mobileNo, _gender, _profile_pic,rating, _address, _dob, doc_type, _appointmentDate, _aadharNo, _day;
    private static final String[] GENDERS = new String[]{"Male", "Female", "Others", "Transgender"};
    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendar1 = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_request);

        Intent intent = getIntent();
        doctorMobileNo = intent.getStringExtra("mobile_no");
        rating = intent.getStringExtra("rating");


//        token=intent.getStringExtra("token");
        //Toast.makeText(this, doctorMobileNo, Toast.LENGTH_SHORT).show();

        PatientSession sessionManager = new PatientSession(this, PatientSession.PATIENT_LOGIN_SESSION);
        HashMap<String, String> doctorDetails = sessionManager.getUserDetailFromSession();


        _mobileNo = doctorDetails.get(PatientSession.KEY_PHONE_NUMBER);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Patient").child(_mobileNo);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                token=snapshot.child("token").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        settingViewByID();

        _name = name.getText().toString();

        settingOnClick();

    }

    private void getday() throws ParseException {

        String xxx = appointmentDate.getText().toString();
        Date inputDate = new SimpleDateFormat("dd/MM/yyyy").parse(xxx);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate);
        _day = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US).toUpperCase();


    }

    private void settingOnClick() {

        setDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key = "ajay";
                selectImage();
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key = "ajay";
                selectImage();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateName() | !validateGender() | !validateAddress() | !validateAadhar() | key.equals("xyz")) {

                    if (key.equals("xyz")) {
                        Toast.makeText(AppointmentRequest.this,
                                "Please Select a picture to upload", Toast.LENGTH_SHORT).show();
                    }

                    return;
                }

                _name = name.getText().toString();
                _dob = dob.getText().toString();
                _gender = gender.getText().toString();
                _appointmentDate = appointmentDate.getText().toString();
                _address = address.getText().toString();
                _aadharNo = aadharNo.getMasked();

                try {
                    getday();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                uploadImage();
            }
        });


    }

    private void settingViewByID() {

        name = findViewById(R.id.pt_name_appointment_req);
        dob = findViewById(R.id.pt_dob_appointment_req);
        gender = findViewById(R.id.pt_gender_appointment_req);
        appointmentDate = findViewById(R.id.appointment_date_appointment_req);
        address = findViewById(R.id.patient_address_appointment_req);
        aadharNo = findViewById(R.id.patient_aadhar_appointment_req);
        dobIcon = findViewById(R.id.pt_dob_btn_appointment_req);
        appointmentIcon = findViewById(R.id.appointment_date_btn_appointment_req);
        next = findViewById(R.id.next_btn_appointment_req);

        setDp = findViewById(R.id.select_photo);
        profilePic = findViewById(R.id.profile_photo);

        toolbar = findViewById(R.id.appointment_req);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Patient's Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        updateLabel(appointmentDate, myCalendar1);


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel(dob, myCalendar);
            }

        };

        DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view1, int year1, int monthOfYear1,
                                  int dayOfMonth1) {
                // TODO Auto-generated method stub
                myCalendar1.set(Calendar.YEAR, year1);
                myCalendar1.set(Calendar.MONTH, monthOfYear1);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth1);


                updateLabel(appointmentDate, myCalendar1);
            }

        };

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, GENDERS);
        gender.setAdapter(genderAdapter);


        dobIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AppointmentRequest.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        appointmentIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AppointmentRequest.this, date1, myCalendar1
                        .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();


            }
        });

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


            // Defining the child of storageReference

            StorageReference ref = FirebaseStorage.getInstance().getReference()
                    .child("appointment/patient").child(_mobileNo + _name + ".jpg");
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference()
                    .child("Appointment").child("pending").child(_mobileNo);


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
                                            String download_url = uri.toString();
                                            // _profile_pic = download_url;
                                            mDatabase.child("image").setValue(download_url)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(getApplicationContext(), "Pic Uploaded Successfully", Toast.LENGTH_LONG).show();
//
                                                                Intent intent = new Intent(getApplicationContext(), MyCalendarDoctorActivity.class);
                                                                intent.putExtra("name", _name);
                                                                intent.putExtra("dob", _dob);
                                                                intent.putExtra("gender", _gender);
                                                                intent.putExtra("appointment_date", _appointmentDate);
                                                                intent.putExtra("address", _address);
                                                                intent.putExtra("aadhar", _aadharNo);
                                                                intent.putExtra("doctor_mobile", doctorMobileNo);
                                                                intent.putExtra("day", _day);
                                                                intent.putExtra("mobile", _mobileNo);
                                                                intent.putExtra("rating", rating);
                                                                intent.putExtra("token", token);


                                                                startActivity(intent);
                                                                progressDialog.dismiss();


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

    private void selectImage() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(this);

    }

    private void updateLabel(EditText editText, Calendar myCalendar) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(myCalendar.getTime()));


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
                    profilePic.setImageBitmap(bitmap);
                } catch (IOException e) {
                    // Log the exception
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    private Boolean validateName() {
        String val = name.getText().toString();
        if (val.isEmpty()) {
            name.setError("Field can't be Empty!");
            return false;
        } else {
            name.setError(null);
            return true;
        }
    }

    private Boolean validateAddress() {
        String val = address.getText().toString();
        if (val.isEmpty()) {
            address.setError("Field can't be Empty!");
            return false;
        } else {
            address.setError(null);
            return true;
        }
    }

    private Boolean validateAadhar() {
        String val = aadharNo.getMasked();
        if (val.isEmpty()) {
            aadharNo.setError("Field can't be Empty!");
            return false;
        } else if (!aadharNo.isDone()) {
            aadharNo.setError("Some Error!");
            return false;
        } else {
            aadharNo.setError(null);
            return true;
        }
    }

    private Boolean validateGender() {
        String val = gender.getText().toString();
        List<String> nameList = new ArrayList<>(Arrays.asList(GENDERS));
        if (!nameList.contains(val)) {
            gender.setError("Wrong Gender");
            return false;
        } else {
            gender.setError(null);
            return true;
        }
    }


}