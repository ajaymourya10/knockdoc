package com.udit.testing.doctor.pending_appointment_doctor;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.udit.testing.Model.Doctor;
import com.udit.testing.Model.Test;
import com.udit.testing.Model.sessions.SessionManager;
import com.udit.testing.R;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PendingAppointmentDoctorAdapter extends RecyclerView.Adapter<PendingAppointmentDoctorAdapter.ImageViewHolder> {
    private Context context;
    private ArrayList<AppointmentDoctor> mAppointmentDoctor;
    private Dialog myDialog;
    private ArrayList<Doctor> doctor;
    private String _mobileNo,token;
    private DatabaseReference databaseReference;
    private DatabaseReference succref;
    String patient_img,_name,patient_name,patient_gender,patient_dob,patient_mobile,patient_aadhar,count,appointment_date,appointmnet_day,time_slot,doc_type,doc_pic;

    private ArrayList<AppointmentDoctor> mAppointmentDoctorFull;

    public PendingAppointmentDoctorAdapter(Context context, ArrayList<AppointmentDoctor> mAppointmentDoctor) {
        this.context = context;
        this.mAppointmentDoctor = mAppointmentDoctor;
        mAppointmentDoctorFull = new ArrayList<>(mAppointmentDoctor);

    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //FirebaseMessaging.getInstance().subscribeToTopic("all");
        View v = LayoutInflater.from(context).inflate(R.layout.appointment_view_doc, parent, false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(v);


        return imageViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        AppointmentDoctor appointmentCurrent = mAppointmentDoctor.get(position);

        if(appointmentCurrent.getPatient_gender().toLowerCase().equals("male")){
            holder.name.setText("Mr. "+appointmentCurrent.getPatient_name());
        }else  if(appointmentCurrent.getPatient_gender().toLowerCase().equals("female")){
            holder.name.setText("Ms. "+appointmentCurrent.getPatient_name());
        }else {
            holder.name.setText(appointmentCurrent.getPatient_name());
        }
        patient_mobile=appointmentCurrent.getPatient_mobile();
        count=appointmentCurrent.getCount();
        patient_img=appointmentCurrent.getPatient_image();
        patient_name=appointmentCurrent.getPatient_name();
        patient_dob=appointmentCurrent.getPatient_dob();
        patient_aadhar=appointmentCurrent.getPatient_aadhar();
        patient_gender=appointmentCurrent.getPatient_gender();
        appointment_date=appointmentCurrent.getAppointment_date();
        appointmnet_day=appointmentCurrent.getAppointment_day();
        time_slot=appointmentCurrent.getTime_slot();
        token=appointmentCurrent.getToken();

        String check=appointmentCurrent.getPatient_mobile()+"_"+appointmentCurrent.getCount();

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Appointments").child("successful").child("doctor");

        Query query= databaseReference.child(appointmentCurrent.getDoc_mob());

       query.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
               if (snapshot.hasChild(check))
               {
                   holder.accept.setEnabled(false);
                   holder.accept.setText("Accepted");
                   holder.accept.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                   //holder.accept.setVisibility(View.INVISIBLE);
                   holder.cancel.setEnabled(false);
                   holder.cancel.setVisibility(View.INVISIBLE);
               }
           }

           @Override
           public void onCancelled(@NonNull @NotNull DatabaseError error) {

           }
       });

       String _dob = appointmentCurrent.getPatient_dob();
        String _age = "25";
        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        }
        LocalDate date = LocalDate.parse(_dob, formatter);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date2 = new Date();
        String d = dateFormat.format(date2);

        LocalDate date1 = LocalDate.parse(d, formatter);
        int age = calculateAge(date,date1);

        holder.age.setText("Age: "+age+" Yr. ("+appointmentCurrent.getPatient_gender()+")");

        holder.date.setText(appointmentCurrent.getAppointment_date());
        holder.timeSlot.setText(appointmentCurrent.getTime_slot());

        Picasso.get().load(appointmentCurrent.getPatient_image()).placeholder(R.drawable.doctor_pic).into(holder.pic);






    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return mAppointmentDoctor.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name,age,date,timeSlot;
        public ImageView iBtn;
        public CircleImageView pic;
        public Button accept,cancel;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            name = itemView.findViewById(R.id.name);
            age = itemView.findViewById(R.id.age_gender);
            date = itemView.findViewById(R.id.appointment_date);
            timeSlot = itemView.findViewById(R.id.time_slot);
            iBtn = itemView.findViewById(R.id.i_btn);
            accept = itemView.findViewById(R.id.accept_btn);
            cancel = itemView.findViewById(R.id.cancel_btn);
            pic = itemView.findViewById(R.id.photo);

            iBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Click Done", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                    View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.abc,null);

                    Button button = dialogView.findViewById(R.id.clickme);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getRootView().getContext(),Test.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            v.getRootView().getContext().startActivity(intent);
                        }
                    });

                    builder.setView(dialogView);
                    builder.show();


                }
            });
            accept.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    AppointmentDoctor appointmentCurrent = mAppointmentDoctorFull.get(position);
                    AlertDialog builder = new AlertDialog.Builder(v.getRootView().getContext(),
                            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background).create();
                    View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.
                            accept_appointment, null);

                    TextView name = dialogView.findViewById(R.id.name);
                    TextView time = dialogView.findViewById(R.id.time);
                    TextView dob = dialogView.findViewById(R.id.age);
                    TextView show = dialogView.findViewById(R.id.showText);
                    Button bookAppointment = dialogView.findViewById(R.id.accept_btn);
                    RoundedImageView pic = dialogView.findViewById(R.id.photo);

                    Typeface font1 = ResourcesCompat.getFont(context, R.font.baloo);
                    if (font1 != null) {
                        if (show != null) show.setTypeface(font1);
                    }

                    Typeface font = ResourcesCompat.getFont(context, R.font.roboto_slab);
                    if (font != null) {
                        if (name != null) name.setTypeface(font, Typeface.BOLD);
                        if (name != null) time.setTypeface(font, Typeface.BOLD);
                        if (name != null) dob.setTypeface(font, Typeface.BOLD);
                    }

                    if (appointmentCurrent.getPatient_gender().toLowerCase().equals("male")) {
                        name.setText("Mr. " + appointmentCurrent.getPatient_name());
                    } else if (appointmentCurrent.getPatient_gender().toLowerCase().equals("female")) {
                        name.setText("Ms. " + appointmentCurrent.getPatient_name());
                    } else {
                        name.setText(appointmentCurrent.getPatient_name());
                    }

                    String _dob = appointmentCurrent.getPatient_dob();
                    DateTimeFormatter formatter = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    }
                    LocalDate date = LocalDate.parse(_dob, formatter);
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date2 = new Date();
                    String d = dateFormat.format(date2);

                    LocalDate date1 = LocalDate.parse(d, formatter);
                    int age = calculateAge(date, date1);


                    dob.setText("Age: " + age + " Yr. (" + appointmentCurrent.getPatient_gender() + ")");
                    time.setText("Appointment On: " + appointmentCurrent.getAppointment_date() + " at " + appointmentCurrent.getTime_slot());

                    Picasso.get().load(appointmentCurrent.getPatient_image()).placeholder(R.drawable.doctor_pic).into(pic);


                    bookAppointment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ProgressDialog dialog = new ProgressDialog(v.getRootView().getContext());
                            dialog.setTitle("Please Wait...");
                            dialog.setCancelable(false);
                            dialog.show();

                            //Toast.makeText(v.getRootView().getContext(), "Clicked", Toast.LENGTH_SHORT).show();

                            SessionManager sessionManager = new SessionManager(v.getRootView().getContext(), SessionManager.USER_LOGIN_SESSION);
                            HashMap<String, String> doctorDetails = sessionManager.getUserDetailFromSession();

                            String _mobileNo = doctorDetails.get(SessionManager.KEY_PHONE_NUMBER);
                            String name= doctorDetails.get(SessionManager.KEY_NAME);

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                                    .child("Appointments").child("successful").child("doctor").child(_mobileNo);

                            Map<String, String> hasMap = new HashMap<>();
                            hasMap.put("patient_name", appointmentCurrent.getPatient_name());
                            hasMap.put("patient_image", appointmentCurrent.getPatient_image());
                            hasMap.put("patient_dob", appointmentCurrent.getPatient_dob());
                            hasMap.put("appointment_date", appointmentCurrent.getAppointment_date());
                            hasMap.put("appointment_day", appointmentCurrent.getAppointment_day());
                            hasMap.put("patient_mobile", appointmentCurrent.getPatient_mobile());
                            hasMap.put("count", appointmentCurrent.getCount());
                            hasMap.put("patient_gender", appointmentCurrent.getPatient_gender());
                            // hasMap.put("patient_address", appointmentCurrent.getPatient_address());
                            hasMap.put("patient_aadhar", appointmentCurrent.getPatient_aadhar());
                            hasMap.put("time_slot", appointmentCurrent.getTime_slot());
                            hasMap.put("token",appointmentCurrent.getToken());
                            hasMap.put("status", "accepted");

                            reference.child(appointmentCurrent.getPatient_mobile() + "_" + appointmentCurrent.getCount())
                                    .setValue(hasMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    String status = "accepted";
                                    Task<Void> databaseReference = FirebaseDatabase.getInstance().getReference()
                                            .child("Appointments").child("pending").child("doctor").child(_mobileNo)
                                            .child(appointmentCurrent.getPatient_mobile() + "_" + appointmentCurrent.getCount())
                                            .child("status").setValue(status)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("Appointments").child("pending").child("patient")
                                                            .child(appointmentCurrent.getPatient_mobile())
                                                            .child(appointmentCurrent.getCount()).child("status")
                                                            .setValue("approved")
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    show.setText("Appointment Accepted");
                                                                    bookAppointment.setVisibility(View.INVISIBLE);
                                                                    accept.setVisibility(View.GONE);
                                                                    dialog.dismiss();
                                                                    builder.dismiss();
                                                                }
                                                            });




                                                }
                                            })
                                            .addOnCanceledListener(new OnCanceledListener() {
                                                @Override
                                                public void onCanceled() {
                                                    Toast.makeText(v.getRootView().getContext(), "Error in Code", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                    builder.dismiss();
                                                }
                                            });

                                }
                            });


                            DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference().child("Users").child("Doctor")
                            .child(_mobileNo);
                    databaseReference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            doc_pic= dataSnapshot.child("profile_image").getValue().toString();
                            doc_type=dataSnapshot.child("doctor_type").getValue().toString();
                                   // doctor.setDoctor_type(doc_type);
                                    //doctor.setProfile_image(doc_pic);
                            Intent intent=new Intent(context,FinalAcceptance.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("ptname",patient_name);
                            intent.putExtra("docname",name);
                            intent.putExtra("ad",appointment_date);
                            intent.putExtra("doctype",doc_type);
                            intent.putExtra("docpic",doc_pic);
                            intent.putExtra("ptpic",patient_img);
                            intent.putExtra("token",token);
                            intent.putExtra("count",count);
                            intent.putExtra("ptmobile",patient_mobile);
                            intent.putExtra("ptgender",patient_gender);
                            intent.putExtra("ptdob",patient_dob);
                            context.startActivity(intent);

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }


                    });
                        }


                    });

                    builder.setView(dialogView);
                    builder.show();








//                    SessionManager sessionManager = new SessionManager(context, SessionManager.USER_LOGIN_SESSION);
//                    HashMap<String, String> doctorDetails = sessionManager.getUserDetailFromSession();
//
//                    _mobileNo = doctorDetails.get(SessionManager.KEY_PHONE_NUMBER);
//                    _name=doctorDetails.get(SessionManager.KEY_NAME);
//                    Log.d("moooooooo",_mobileNo+" "+_name);
//
//
//
//                    DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference().child("Users").child("Doctor")
//                            .child(_mobileNo);
//                    databaseReference1.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                            doc_pic= dataSnapshot.child("profile_image").getValue().toString();
//                            doc_type=dataSnapshot.child("doctor_type").getValue().toString();
//                                   // doctor.setDoctor_type(doc_type);
//                                    //doctor.setProfile_image(doc_pic);
//                            Intent intent=new Intent(context,FinalAcceptance.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.putExtra("ptname",patient_name);
//                            intent.putExtra("docname",_name);
//                            intent.putExtra("ad",appointment_date);
//                            intent.putExtra("doctype",doc_type);
//                            intent.putExtra("docpic",doc_pic);
//                            intent.putExtra("ptpic",patient_img);
//                            intent.putExtra("token",token);
//                            intent.putExtra("count",count);
//                            intent.putExtra("ptmobile",patient_mobile);
//                            intent.putExtra("ptgender",patient_gender);
//                            intent.putExtra("ptdob",patient_dob);
//                            context.startActivity(intent);
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//
//
//
//                    GetDataFromFirebase();
//
//
//                    accept.setEnabled(false);
//                    cancel.setEnabled(false);
//
//

                }
            });



        }
        private void GetDataFromFirebase(){
            Log.d("piccccccc","picture "+doc_pic);
            Log.d("typpppppp","type "+doc_type);
            succref = FirebaseDatabase.getInstance().getReference()
                    .child("Appointments").child("successful").child("doctor").child(_mobileNo).child(patient_mobile + "_" +count);

            //doctorRef.child(_mobileNo + _name);
            Map<String, String> hasMap = new HashMap<>();
            hasMap.put("patient_name", patient_name);
            hasMap.put("patient_image", patient_img);
            hasMap.put("patient_dob", patient_dob);
            hasMap.put("appointment_date", appointment_date);
            hasMap.put("appointment_day", appointmnet_day);
            hasMap.put("patient_mobile", patient_mobile);
            hasMap.put("count", count + "");
            hasMap.put("patient_gender", patient_gender);
            hasMap.put("patient_aadhar", patient_aadhar);
            hasMap.put("time_slot", time_slot);
            hasMap.put("token",token);
            hasMap.put("status", "successful");

            succref.setValue(hasMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    Toast.makeText(context, "Appointment accepted", Toast.LENGTH_SHORT).show();
                }
            });




        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            AppointmentDoctor appointmentCurrent = mAppointmentDoctorFull.get(position);

//            Intent intent = new Intent(context, AppointmentInfoPatient.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            context.startActivity(intent);
        }


    }

    public void filterList(ArrayList<AppointmentDoctor> filteredList) {
        mAppointmentDoctor = filteredList;
        notifyDataSetChanged();
    }
}


