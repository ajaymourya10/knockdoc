package com.udit.testing.doctor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.udit.testing.Model.sessions.SessionManager;
import com.udit.testing.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.santalu.maskara.widget.MaskEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditDoctorClinic extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView nameText,feeText, startingDateText, cityText, pinText, stateText, specialistText, doctorTypeText, addressText, degreeText;
    private EditText nameEditText,feeEditText, degreeEditText, addressEditText;
    private MaskEditText pinEditText;
    private String mobileNo, _title,_fee, _degree, _starting_year, _type, _specialist_in, _address, _state, _city, _pin;
    private String mobileNo1, _title1,_fee1, _degree1, _starting_year1, _type1, _specialist_in1, _address1, _state1, _city1, _pin1;
    private AutoCompleteTextView cityEditText, startingDateEditText, stateEditText, specialistEditText, doctorTypeEditText;
    private ImageView nameIcon, feeIcon, startingDateIcon, cityIcon, pinIcon, stateIcon, specialistIcon, doctorTypeIcon, addressIcon, degreeIcon;
    private Button setName,setFee, setStartingDate, setCity, setPin, setState, setSpecialist, setDoctorType, setAddress, setDegree, save;
    private DatabaseReference mDatabase;


    private static final String[] STATE = new String[]{"Andhra Pradesh",
            "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh",
            "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur",
            "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana",
            "Tripura", "Uttarakhand", "Uttar Pradesh", "West Bengal",

            "Andaman and Nicobar Islands", "Chandigarh", "Dadra and Nagar Haveli", "Daman and Diu",
            "Delhi", "Lakshadweep", "Puducherry"};

    private static final String[] SPECILITY = new String[]{"Child Specialist", "Heart Specialist"};

    private static final String[] TYPE = new String[]{"Physician", "Surgeon", "Neurosurgeon"};

    private static final String[] CITIES = new String[]{"Bareilly", "Sonipat", "Rishikesh", "Gorakhpur", "Kolkata", "Noida",
            "Jaipur", "Udaypur", "New Delhi", "Faridabad", "Meerut"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_doctor_clinic);

        SessionManager sessionManager = new SessionManager(this, SessionManager.USER_LOGIN_SESSION);
        HashMap<String, String> doctorDetails = sessionManager.getUserDetailFromSession();
        mobileNo = doctorDetails.get(SessionManager.KEY_PHONE_NUMBER);

        _title = "none";
        _degree = "none";
        _starting_year = "none";
        _type = "none";
        _address = "none";
        _state = "none";
        _pin = "none";
        _city = "none";
        _fee = "none";

        settingViewByID();
        settingAdapter();
        settingOnClick();

    }

    private void settingAdapter() {

        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, STATE);
        stateEditText.setAdapter(stateAdapter);


        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, TYPE);
        doctorTypeEditText.setAdapter(typeAdapter);

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, CITIES);
        cityEditText.setAdapter(cityAdapter);
    }

    private void settingOnClick() {


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        nameIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_title.equals("none")) {
                    nameEditText.setText("");
                } else {
                    nameEditText.setText(_title);
                }
                nameText.setVisibility(View.GONE);
                nameEditText.setVisibility(View.VISIBLE);
                save.setEnabled(false);
                setName.setVisibility(View.VISIBLE);
                nameIcon.setVisibility(View.GONE);

                setName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        save.setEnabled(true);
                        if (nameEditText.getText().toString().equals("")) {
                            nameText.setText("Not Mentioned");
                            nameText.setTextColor(Color.RED);
                        } else {
                            nameText.setText(nameEditText.getText());
                            nameText.setTextColor(Color.BLACK);
                            _title = nameText.getText().toString();
                        }
                        _title = nameEditText.getText().toString();
                        setName.setVisibility(View.GONE);
                        nameEditText.setVisibility(View.GONE);
                        nameText.setVisibility(View.VISIBLE);
                        nameIcon.setVisibility(View.VISIBLE);

                    }
                });
            }
        });

        feeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_fee.equals("none")) {
                    nameEditText.setText("");
                } else {
                    feeEditText.setText(_fee);
                }
                feeText.setVisibility(View.GONE);
                feeEditText.setVisibility(View.VISIBLE);
                save.setEnabled(false);
                setFee.setVisibility(View.VISIBLE);
                feeIcon.setVisibility(View.GONE);

                setFee.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        save.setEnabled(true);
                        if (feeEditText.getText().toString().equals("")) {
                            feeText.setText("Not Mentioned");
                            feeText.setTextColor(Color.RED);
                        } else {
                            feeText.setText(feeEditText.getText());
                            feeText.setTextColor(Color.BLACK);
                            _fee = feeText.getText().toString();
                        }
                        _fee = feeEditText.getText().toString();
                        setFee.setVisibility(View.GONE);
                        feeEditText.setVisibility(View.GONE);
                        feeText.setVisibility(View.VISIBLE);
                        feeIcon.setVisibility(View.VISIBLE);

                    }
                });
            }
        });

        stateIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_state.equals("none")) {
                    stateEditText.setText("");
                } else {
                    stateEditText.setText(_state);
                }
                stateText.setVisibility(View.GONE);
                stateEditText.setVisibility(View.VISIBLE);
                save.setEnabled(false);
                setState.setVisibility(View.VISIBLE);
                stateIcon.setVisibility(View.GONE);

                setState.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (stateEditText.getText().toString().equals("")) {
                            stateText.setText("Not Mentioned");
                            stateText.setTextColor(Color.RED);
                        } else {
                            stateText.setText(stateEditText.getText());
                            String temp = stateEditText.getText().toString();
                            List<String> nameList = new ArrayList<>(Arrays.asList(STATE));
                            stateText.setTextColor(Color.BLACK);
                            if (!nameList.contains(temp)) {
                                stateEditText.setError("Wrong State");
                                return;
                            }
                            stateEditText.setError(null);

                            _state = stateText.getText().toString();
                        }
                        save.setEnabled(true);
                        setState.setVisibility(View.GONE);
                        stateEditText.setVisibility(View.GONE);
                        stateText.setVisibility(View.VISIBLE);
                        stateIcon.setVisibility(View.VISIBLE);

                    }
                });
            }
        });

        degreeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_degree.equals("none")) {
                    degreeEditText.setText("");
                } else {
                    degreeEditText.setText(_degree);
                }
                degreeText.setVisibility(View.GONE);
                degreeEditText.setVisibility(View.VISIBLE);
                save.setEnabled(false);
                setDegree.setVisibility(View.VISIBLE);
                degreeIcon.setVisibility(View.GONE);

                setDegree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        save.setEnabled(true);
                        if (degreeEditText.getText().toString().equals("")) {
                            degreeText.setText("Not Mentioned");
                            degreeText.setTextColor(Color.RED);
                        } else {
                            degreeText.setText(degreeEditText.getText());
                            degreeText.setTextColor(Color.BLACK);
                            _degree = degreeText.getText().toString();
                        }
                        setDegree.setVisibility(View.GONE);
                        degreeEditText.setVisibility(View.GONE);
                        degreeText.setVisibility(View.VISIBLE);
                        degreeIcon.setVisibility(View.VISIBLE);

                    }
                });
            }
        });

        startingDateIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_starting_year.equals("none")) {
                    startingDateEditText.setText("");
                } else {
                    startingDateEditText.setText(_starting_year);
                }
                startingDateText.setVisibility(View.GONE);
                startingDateEditText.setVisibility(View.VISIBLE);
                save.setEnabled(false);
                setStartingDate.setVisibility(View.VISIBLE);
                startingDateIcon.setVisibility(View.GONE);

                setStartingDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        save.setEnabled(true);
                        if (startingDateEditText.getText().toString().equals("")) {
                            startingDateText.setText("Not Mentioned");
                            startingDateText.setTextColor(Color.RED);
                        } else {
                            startingDateText.setText(startingDateEditText.getText());
                            startingDateText.setTextColor(Color.BLACK);
                            _starting_year = startingDateText.getText().toString();
                        }
                        //startingDateText.setText(startingDateEditText.getText());
                        setStartingDate.setVisibility(View.GONE);
                        startingDateEditText.setVisibility(View.GONE);
                        startingDateText.setVisibility(View.VISIBLE);
                        startingDateIcon.setVisibility(View.VISIBLE);

                    }
                });
            }
        });

        doctorTypeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_type.equals("none")) {
                    doctorTypeEditText.setText("");
                } else {
                    doctorTypeEditText.setText(_type);
                }
                doctorTypeText.setVisibility(View.GONE);
                doctorTypeEditText.setVisibility(View.VISIBLE);
                save.setEnabled(false);
                setDoctorType.setVisibility(View.VISIBLE);
                doctorTypeIcon.setVisibility(View.GONE);

                setDoctorType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        save.setEnabled(true);
                        if (doctorTypeEditText.getText().toString().equals("")) {
                            doctorTypeText.setText("Not Mentioned");
                            doctorTypeText.setTextColor(Color.RED);
                        } else {
                            doctorTypeText.setText(doctorTypeEditText.getText());
                            doctorTypeText.setTextColor(Color.BLACK);
                            _type = doctorTypeText.getText().toString();
                        }
                        setDoctorType.setVisibility(View.GONE);
                        doctorTypeEditText.setVisibility(View.GONE);
                        doctorTypeText.setVisibility(View.VISIBLE);
                        doctorTypeIcon.setVisibility(View.VISIBLE);

                    }
                });
            }
        });


        addressIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_address.equals("none")) {
                    addressEditText.setText("");
                } else {
                    addressEditText.setText(_address);
                }
                addressText.setVisibility(View.GONE);
                addressEditText.setVisibility(View.VISIBLE);
                save.setEnabled(false);
                setAddress.setVisibility(View.VISIBLE);
                addressIcon.setVisibility(View.GONE);

                setAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        save.setEnabled(true);
                        if (addressEditText.getText().toString().equals("")) {
                            addressText.setText("Not Mentioned");
                            addressText.setTextColor(Color.RED);
                        } else {
                            addressText.setText(addressEditText.getText());
                            addressText.setTextColor(Color.BLACK);
                            _address = addressText.getText().toString();
                        }
                        setAddress.setVisibility(View.GONE);
                        addressEditText.setVisibility(View.GONE);
                        addressText.setVisibility(View.VISIBLE);
                        addressIcon.setVisibility(View.VISIBLE);

                    }
                });
            }
        });

        cityIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_city.equals("none")) {
                    cityEditText.setText("");
                } else {
                    cityEditText.setText(_city);
                }
                cityText.setVisibility(View.GONE);
                cityEditText.setVisibility(View.VISIBLE);
                save.setEnabled(false);
                setCity.setVisibility(View.VISIBLE);
                cityIcon.setVisibility(View.GONE);

                setCity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        save.setEnabled(true);
                        if (cityEditText.getText().toString().equals("")) {
                            cityText.setText("Not Mentioned");
                            cityText.setTextColor(Color.RED);
                        } else {
                            cityText.setText(cityEditText.getText());
                            cityText.setTextColor(Color.BLACK);
                            _city = cityText.getText().toString();
                        }
                        setCity.setVisibility(View.GONE);
                        cityEditText.setVisibility(View.GONE);
                        cityText.setVisibility(View.VISIBLE);
                        cityIcon.setVisibility(View.VISIBLE);

                    }
                });
            }
        });

        pinIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_pin.equals("none")) {
                    pinEditText.setText("");
                } else {
                    pinEditText.setText(_pin);
                }
                pinText.setVisibility(View.GONE);
                pinEditText.setVisibility(View.VISIBLE);
                save.setEnabled(false);
                setPin.setVisibility(View.VISIBLE);
                pinIcon.setVisibility(View.GONE);

                setPin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        save.setEnabled(true);
                        if (pinEditText.getText().toString().equals("")) {
                            pinText.setText("Not Mentioned");
                            pinText.setTextColor(Color.RED);
                        } else {
                            pinText.setText(pinEditText.getMasked());
                            pinText.setTextColor(Color.BLACK);
                            _pin = pinText.getText().toString();
                        }
                        setPin.setVisibility(View.GONE);
                        pinEditText.setVisibility(View.GONE);
                        pinText.setVisibility(View.VISIBLE);
                        pinIcon.setVisibility(View.VISIBLE);

                    }
                });
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_title.equals("none") | _state.equals("none") | _degree.equals("none") | _starting_year.equals("none") |
                        _type.equals("none") | _address.equals("none")
                        | _pin.equals("none") | _city.equals("none")| _fee.equals("none")) {

                    Toast.makeText(getApplicationContext(), "Please fill all the fields..", Toast.LENGTH_LONG).show();
                    return;
                }

                if (_title.equals(_title1 + "") && _degree.equals(_degree1 + "") && _starting_year.equals(_starting_year1 + "") &&
                        _type.equals(_type1 + "")  && _address.equals(_address1 + "") &&
                        _city.equals(_city1 + "") && _pin.equals(_pin1 + "") && _state.equals(_state1 + "") &&_fee.equals(_fee1 + "")) {

                    Toast.makeText(getApplicationContext(), "No Change in Any Field", Toast.LENGTH_LONG).show();
                    //onBackPressed();
                    return;
                }

                ProgressDialog progressDialog = new ProgressDialog(EditDoctorClinic.this);
                progressDialog.setTitle("Saving Details...");
                progressDialog.setMessage("Please wait while data is inserting into database");
                progressDialog.setCancelable(false);
                progressDialog.show();

                mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Doctor")
                        .child(mobileNo).child("clinic");
                Map<String, String> hasMap = new HashMap<>();
                hasMap.put("title", _title);
                hasMap.put("degree", _degree.toUpperCase());
                hasMap.put("starting_year", _starting_year);
                hasMap.put("doctor_type", _type);
                hasMap.put("address", _address);
                hasMap.put("state", _state);
                hasMap.put("city", _city);
                hasMap.put("fee", _fee);
                hasMap.put("pin_code", _pin);
                mDatabase.setValue(hasMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                                .child("Users").child("Doctor").child(mobileNo);


                        databaseReference.child("degree").setValue(_degree)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                databaseReference.child("doctor_type").setValue(_type)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                                databaseReference.child("fee").setValue(_fee)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        progressDialog.dismiss();
                                                        Intent intent = new Intent(getApplicationContext(), DoctorDashboard.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);
                                                        Toast.makeText(getApplicationContext(), "Details Uploaded", Toast.LENGTH_LONG).show();

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

    private void settingViewByID() {

        save = findViewById(R.id.saveEditProfileClinic);

        nameIcon = findViewById(R.id.clinic_name_edit_profile_icon);
        nameText = findViewById(R.id.clinic_name_edit_profile);
        nameEditText = findViewById(R.id.edit_text_clinic_name);
        setName = findViewById(R.id.btn_set_clinic_name);

        nameEditText.setVisibility(View.GONE);
        setName.setVisibility(View.GONE);

        feeIcon = findViewById(R.id.clinic_fee_edit_profile_icon);
        feeText = findViewById(R.id.clinic_fee_edit_profile);
        feeEditText = findViewById(R.id.edit_text_clinic_fee);
        setFee = findViewById(R.id.btn_set_clinic_fee);

        feeEditText.setVisibility(View.GONE);
        setFee.setVisibility(View.GONE);

        degreeIcon = findViewById(R.id.doctor_degree_edit_profile_icon);
        degreeText = findViewById(R.id.doctor_degree_edit_profile);
        degreeEditText = findViewById(R.id.edit_text_doctor_degree);
        setDegree = findViewById(R.id.btn_set_doctor_degree);

        degreeEditText.setVisibility(View.GONE);
        setDegree.setVisibility(View.GONE);

        startingDateIcon = findViewById(R.id.doctor_practice_edit_profile_icon);
        startingDateText = findViewById(R.id.doctor_practice_edit_profile);
        startingDateEditText = findViewById(R.id.edit_text_doctor_practice);
        setStartingDate = findViewById(R.id.btn_set_doctor_practice);

        startingDateEditText.setVisibility(View.GONE);
        setStartingDate.setVisibility(View.GONE);

        doctorTypeIcon = findViewById(R.id.doctor_type_edit_profile_icon);
        doctorTypeText = findViewById(R.id.doctor_type_edit_profile);
        doctorTypeEditText = findViewById(R.id.edit_text_doctor_type);
        setDoctorType = findViewById(R.id.btn_set_doctor_type);

        doctorTypeEditText.setVisibility(View.GONE);
        setDoctorType.setVisibility(View.GONE);


        stateIcon = findViewById(R.id.doctor_state_edit_profile_icon);
        stateText = findViewById(R.id.doctor_state_edit_profile);
        stateEditText = findViewById(R.id.edit_text_doctor_state);
        setState = findViewById(R.id.btn_set_doctor_state);

        stateEditText.setVisibility(View.GONE);
        setState.setVisibility(View.GONE);

        cityIcon = findViewById(R.id.doctor_city_edit_profile_icon);
        cityText = findViewById(R.id.doctor_city_edit_profile);
        cityEditText = findViewById(R.id.edit_text_doctor_city);
        setCity = findViewById(R.id.btn_set_doctor_city);

        cityEditText.setVisibility(View.GONE);
        setCity.setVisibility(View.GONE);

        pinIcon = findViewById(R.id.doctor_pin_edit_profile_icon);
        pinText = findViewById(R.id.doctor_pin_edit_profile);
        pinEditText = findViewById(R.id.edit_text_doctor_pin);
        setPin = findViewById(R.id.btn_set_doctor_pin);

        pinEditText.setVisibility(View.GONE);
        setPin.setVisibility(View.GONE);

        addressIcon = findViewById(R.id.doctor_address_edit_profile_icon);
        addressText = findViewById(R.id.doctor_address_edit_profile);
        addressEditText = findViewById(R.id.edit_text_doctor_address);
        setAddress = findViewById(R.id.btn_set_doctor_address);

        addressEditText.setVisibility(View.GONE);
        setAddress.setVisibility(View.GONE);

        toolbar = findViewById(R.id.clinic_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Clinic/Hospital Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ProgressDialog progressDialog = new ProgressDialog(EditDoctorClinic.this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Please wait while we are fetching data from Database...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        DatabaseReference checkUser = FirebaseDatabase.getInstance().getReference().child("Users").child("Doctor")
                .child(mobileNo).child("clinic");

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    _title = snapshot.child("title").getValue().toString();
                    _degree = snapshot.child("degree").getValue().toString();
                    _starting_year = snapshot.child("starting_year").getValue().toString();
                    _type = snapshot.child("doctor_type").getValue().toString();
                    _address = snapshot.child("address").getValue().toString();
                    _state = snapshot.child("state").getValue().toString();
                    _city = snapshot.child("city").getValue().toString();
                    _pin = snapshot.child("pin_code").getValue().toString();
                    _fee = snapshot.child("fee").getValue().toString();

                    _title1 = _title;
                    _type1 = _type;
                    _degree1 = _degree;
                    _state1 = _state;
                    _starting_year1 = _starting_year;
                    _address1 = _address;
                    _pin1 = _pin;
                    _city1 = _city;
                    _fee1 = _fee;


                    nameText.setText(_title);
                    degreeText.setText(_degree);
                    startingDateText.setText(_starting_year);
                    doctorTypeText.setText(_type);

                    addressText.setText(_address);
                    stateText.setText(_state);
                    pinText.setText(_pin);
                    cityText.setText(_city);
                    feeText.setText(_fee);

                } else {

                    nameText.setText("Not Mentioned");
                    nameText.setTextColor(Color.RED);

                    degreeText.setText("Not Mentioned");
                    degreeText.setTextColor(Color.RED);

                    startingDateText.setText("Not Mentioned");
                    startingDateText.setTextColor(Color.RED);

                    doctorTypeText.setText("Not Mentioned");
                    doctorTypeText.setTextColor(Color.RED);



                    addressText.setText("Not Mentioned");
                    addressText.setTextColor(Color.RED);

                    stateText.setText("Not Mentioned");
                    stateText.setTextColor(Color.RED);

                    pinText.setText("Not Mentioned");
                    pinText.setTextColor(Color.RED);

                    cityText.setText("Not Mentioned");
                    cityText.setTextColor(Color.RED);

                    feeText.setText("Not Mentioned");
                    feeText.setTextColor(Color.RED);

                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

}