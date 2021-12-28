package com.udit.testing.doctor;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.udit.testing.Model.sessions.SessionManager;
import com.udit.testing.R;
import com.udit.testing.ShowProfilePic;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.santalu.maskara.widget.MaskEditText;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileDoctor extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView nameText, dobText, cityText, genderText, emailText;
    private EditText nameEditText, emailEditText, password;
    private AutoCompleteTextView cityEditText, genderEditText;
    private TextInputEditText passwordEditText;
    private MaskEditText dobEditText;
    private ImageView nameIcon, dobIcon, cityIcon, genderIcon, emailIcon;
    private Button setName, setDob, setCity, setGender, setEmail, save, uploadImage;
    private CircleImageView profile_pic;
    private Uri filePath;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String _name, _email, _dob, _reg_no, _gender, _mobileNo, key = "xxx", _password, _profile_pic,_token;

    private static final String[] GENDERS = new String[]{"MALE", "FEMALE", "OTHERS", "TRANSGENDER"};
    private static final String[] CITIES = new String[]{"BAREILLY", "SONIPAT", "RISHIKESH", "GORAKHPUR", "KOLKATA", "NOIDA",
            "JAIPUR", "UDAYPUR", "NEW DELHI", "FARIDABAD", "MEERUT"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_doctor);

        nameIcon = findViewById(R.id.doctor_name_edit_profile_icon);
        nameText = findViewById(R.id.doctor_name_edit_profile);
        nameEditText = findViewById(R.id.edit_text_doctor_name);
        setName = findViewById(R.id.btn_set_doctor_name);

        dobIcon = findViewById(R.id.doctor_dob_edit_profile_icon);
        dobText = findViewById(R.id.doctor_dob_edit_profile);
        dobEditText = findViewById(R.id.edit_text_doctor_dob);
        setDob = findViewById(R.id.btn_set_doctor_dob);


        genderIcon = findViewById(R.id.doctor_gender_edit_profile_icon);
        genderText = findViewById(R.id.doctor_gender_edit_profile);
        genderEditText = findViewById(R.id.edit_text_doctor_gender);
        setGender = findViewById(R.id.btn_set_doctor_gender);

        emailIcon = findViewById(R.id.doctor_email_edit_profile_icon);
        emailText = findViewById(R.id.doctor_email_edit_profile);
        emailEditText = findViewById(R.id.edit_text_doctor_email);
        setEmail = findViewById(R.id.btn_set_doctor_email);

        save = findViewById(R.id.saveEditProfiledoctor);
        uploadImage = findViewById(R.id.edit_profile_pic_doctor);
        profile_pic = findViewById(R.id.doctor_profile_pic);

        //passwordEditText = findViewById(R.id.patient_password_edit_profile);

        nameEditText.setVisibility(View.GONE);
        setName.setVisibility(View.GONE);

        dobEditText.setVisibility(View.GONE);
        setDob.setVisibility(View.GONE);


        setGender.setVisibility(View.GONE);
        genderEditText.setVisibility(View.GONE);

        emailEditText.setVisibility(View.GONE);
        setEmail.setVisibility(View.GONE);


        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, GENDERS);
        genderEditText.setAdapter(genderAdapter);

        toolbar = findViewById(R.id.doctor_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        SessionManager sessionManager = new SessionManager(this, SessionManager.USER_LOGIN_SESSION);
        HashMap<String, String> doctorDetails = sessionManager.getUserDetailFromSession();

        _name = doctorDetails.get(SessionManager.KEY_NAME);
        _reg_no = doctorDetails.get(SessionManager.KEY_REGISTRATION_NO);
        _dob = doctorDetails.get(SessionManager.KEY_DOB);
        _gender = doctorDetails.get(SessionManager.KEY_GENDER);
        _mobileNo = doctorDetails.get(SessionManager.KEY_PHONE_NUMBER);
        _password = doctorDetails.get(SessionManager.KEY_PASSWORD);
        _profile_pic = doctorDetails.get(SessionManager.KEY_LOGIN_STATUS);
        _email = doctorDetails.get(SessionManager.KEY_EMAIL);
        _token=doctorDetails.get(SessionManager.KEY_TOKEN);

        if(!_profile_pic.equals("none")){
            Picasso.get().load(_profile_pic).placeholder(R.drawable.doctor_pic).into(profile_pic);
        }else{
            Picasso.get().load(R.drawable.doctor_pic).into(profile_pic);
        }



        nameText.setText(_name);
        dobText.setText(_dob);
        genderText.setText(_gender);
        if (_email.equals("none")) {
            emailText.setText("Does Not Exist");
        } else {
            emailText.setText(_email);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!_profile_pic.equals("none")) {
                    Intent intent = new Intent(getApplicationContext(), ShowProfilePic.class);
                    intent.putExtra("key", _profile_pic);
                    intent.putExtra("key1","doctor1");
                    intent.putExtra("key2",_name);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });

        nameIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameEditText.setText(nameText.getText());
                nameText.setVisibility(View.GONE);
                nameEditText.setVisibility(View.VISIBLE);
                save.setEnabled(false);
                setName.setVisibility(View.VISIBLE);
                nameIcon.setVisibility(View.GONE);

                setName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        save.setEnabled(true);
                        nameText.setText(nameEditText.getText());
                        setName.setVisibility(View.GONE);
                        nameEditText.setVisibility(View.GONE);
                        nameText.setVisibility(View.VISIBLE);
                        nameIcon.setVisibility(View.VISIBLE);

                    }
                });
            }
        });

        dobIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save.setEnabled(false);
                dobEditText.setText(dobText.getText());
                dobText.setVisibility(View.GONE);
                dobEditText.setVisibility(View.VISIBLE);
                setDob.setVisibility(View.VISIBLE);
                dobIcon.setVisibility(View.GONE);

                setDob.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        save.setEnabled(true);
                        dobText.setText(dobEditText.getText());
                        setDob.setVisibility(View.GONE);
                        dobEditText.setVisibility(View.GONE);
                        dobText.setVisibility(View.VISIBLE);
                        dobIcon.setVisibility(View.VISIBLE);

                    }
                });
            }
        });

        genderIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save.setEnabled(false);
                genderEditText.setText(genderText.getText());
                genderText.setVisibility(View.GONE);
                genderEditText.setVisibility(View.VISIBLE);
                setGender.setVisibility(View.VISIBLE);
                genderIcon.setVisibility(View.GONE);

                setGender.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        save.setEnabled(true);
                        genderText.setText(genderEditText.getText());
                        setGender.setVisibility(View.GONE);
                        genderEditText.setVisibility(View.GONE);
                        genderText.setVisibility(View.VISIBLE);
                        genderIcon.setVisibility(View.VISIBLE);

                    }
                });
            }
        });

        emailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (_email.equals("none")) {
                    emailEditText.setText("");
                } else
                    emailEditText.setText(emailText.getText());

                save.setEnabled(false);
                emailText.setVisibility(View.GONE);
                emailEditText.setVisibility(View.VISIBLE);
                setEmail.setVisibility(View.VISIBLE);
                emailIcon.setVisibility(View.GONE);

                setEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (emailEditText.getText().toString().equals("")) {
                            emailText.setText("Does Not Exist");
                        } else
                            emailText.setText(emailEditText.getText());
                        save.setEnabled(true);
                        setEmail.setVisibility(View.GONE);
                        emailEditText.setVisibility(View.GONE);
                        emailText.setVisibility(View.VISIBLE);
                        emailIcon.setVisibility(View.VISIBLE);

                    }
                });
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//                LayoutInflater inflater = getLayoutInflater();
//                View myLayout = inflater.inflate(R.layout.check_password, null);

                builder.setTitle("Save User Details");
                builder.setMessage("Are you sure want to Save the data?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (_name.equals(nameText.getText().toString()) &&
                                _dob.equals(dobText.getText().toString()) &&
                                _email.equals(emailText.getText().toString()) &&
                                _gender.equals(genderText.getText().toString()) &&
                                key.equals("xxx")) {
                            Toast.makeText(getApplicationContext(), "Data Saved", Toast.LENGTH_LONG).show();
                            return;
                        } else if ((!_name.equals(nameText.getText().toString()) ||
                                !_dob.equals(dobText.getText().toString()) ||
                                !_email.equals(emailText.getText().toString()) ||
                                !_gender.equals(genderText.getText().toString())) &&
                                key.equals("xxx")) {
                            fun1();
                            Toast.makeText(getApplicationContext(), "Data Uploaded Successfully", Toast.LENGTH_LONG).show();
                            //progressDialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(), DoctorDashboard.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            return;
                        }
                        uploadImage();

                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                //password = myLayout.findViewById(R.id.set_password_user);
                alertDialog.show();

            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key = "ajay";
                selectImage();

            }
        });


    }


    private void selectImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(this);
    }

    private void uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storageReference.child("profile_images/doctor").child(_mobileNo+".jpg");
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Doctor").child(_mobileNo);


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
                                            _profile_pic = download_url;
                                            mDatabase.child("profile_image").setValue(download_url)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                fun1();
                                                                Toast.makeText(getApplicationContext(), "Pic Uploaded Successfully", Toast.LENGTH_LONG).show();
                                                                progressDialog.dismiss();
                                                                Intent intent = new Intent(getApplicationContext(), DoctorDashboard.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                startActivity(intent);
                                                                // Toast.makeText(getApplicationContext(),"Pic Uploaded Successfully",Toast.LENGTH_LONG).show();
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

    private void fun1() {


        _name = nameText.getText().toString();
        //_city = cityText.getText().toString();
        _dob = dobText.getText().toString();
        if (emailText.getText().toString().equals("Does Not Exist")) {
            _email = "none";
        } else {
            _email = emailText.getText().toString();
        }
        _gender = genderText.getText().toString();
        SessionManager session = new SessionManager(EditProfileDoctor.this, SessionManager.USER_LOGIN_SESSION);
        session.logout();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = mDatabase.getReference().child("Users").child("Doctor").child(_mobileNo);
        reference.child("name").setValue(_name);
        reference.child("dob").setValue(_dob);
        reference.child("email").setValue(_email);
        reference.child("gender").setValue(_gender);
        //reference.child("city").setValue(_city);
        session.createLoginSession(_name,_mobileNo,_reg_no,_password,_gender,_dob,_email,_profile_pic,_token);

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
                    profile_pic.setImageBitmap(bitmap);
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