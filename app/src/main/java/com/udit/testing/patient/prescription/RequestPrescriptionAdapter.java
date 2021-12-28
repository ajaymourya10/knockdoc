package com.udit.testing.patient.prescription;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.udit.testing.R;
import com.udit.testing.patient.PatientDashboard;
import com.udit.testing.patient.appointment_info.PendingAppointment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RequestPrescriptionAdapter extends RecyclerView.Adapter<RequestPrescriptionAdapter.ImageViewHolder> {

    private Context context;
    private ArrayList<PendingAppointment> mPendingAppointment;

    private ArrayList<PendingAppointment> mPendingAppointmentFull;

    public RequestPrescriptionAdapter(Context context, ArrayList<PendingAppointment> mPendingAppointment) {
        this.context = context;
        this.mPendingAppointment = mPendingAppointment;
        mPendingAppointmentFull = new ArrayList<>(mPendingAppointment);
    }

    @NonNull
    @Override
    public RequestPrescriptionAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.prescription_info_doc, parent, false);
        RequestPrescriptionAdapter.ImageViewHolder imageViewHolder = new RequestPrescriptionAdapter.ImageViewHolder(v);

        return imageViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RequestPrescriptionAdapter.ImageViewHolder holder, int position) {
        PendingAppointment appointmentCurrent = mPendingAppointment.get(position);


        if (appointmentCurrent.getGender().toLowerCase().equals("male")) {
            holder.name.setText("Mr. " + appointmentCurrent.getPatient_name());
        } else if (appointmentCurrent.getGender().toLowerCase().equals("female")) {
            holder.name.setText("Ms. " + appointmentCurrent.getPatient_name());
        } else {
            holder.name.setText(appointmentCurrent.getPatient_name());
        }

        holder.date.setText("Appointment Date: "+appointmentCurrent.getAppointment_date());
        if (appointmentCurrent.getStatus().toLowerCase().equals("accepted")) {
            holder.status.setText("Appointment Accepted");
            holder.status.setTextColor(Color.BLUE);
        }else if(appointmentCurrent.getStatus().toLowerCase().equals("received")){
            holder.status.setText("Pending Appointment");
            holder.status.setTextColor(Color.RED);
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
//
//        LocalDate date1 = LocalDate.parse(d, formatter);
//        int age = calculateAge(date, date1);

        holder.age.setText("Doctor Name:-  Dr. " + appointmentCurrent.getDoctor_name() );
        holder.request.setText("Request for Prescription");

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference()
                .child("Appointments").child("pending").child("patient")
                .child(appointmentCurrent.getPatient_mob()).child(appointmentCurrent.getCount());

//        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if (snapshot.hasChild("prescription")){
//                    holder.request.setText("Already Prescribed");
//                    holder.request.setTextColor(Color.RED);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        Picasso.get().load(appointmentCurrent.getPatient_dp()).placeholder(R.drawable.doctor_pic).into(holder.imageView);
//        Picasso.get().load(appointmentCurrent.getPatient_image()).placeholder(R.drawable.doctor_pic).into(holder.imageView);

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
        return mPendingAppointment.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView name, age, date,status, request;
        public RoundedImageView imageView;




        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);



            request = itemView.findViewById(R.id.prec_req);
            status = itemView.findViewById(R.id.status);
            age = itemView.findViewById(R.id.age);
            date = itemView.findViewById(R.id.date);
            name = itemView.findViewById(R.id.name);
            imageView = itemView.findViewById(R.id.image_view);
            request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!request.getText().equals("Prescription Sent")) {
                        int position = getAdapterPosition();
                        PendingAppointment appointmentCurrent = mPendingAppointmentFull.get(position);
                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Appointments").child("Prescription")
                                .child(appointmentCurrent.getDoctor_mobile()).child(appointmentCurrent.getPatient_mob()+"_"+appointmentCurrent.getCount());
                        Map<String,String> hasMap=new HashMap<>();
                        hasMap.put("count",appointmentCurrent.getCount());
                        hasMap.put("patient_name",appointmentCurrent.getPatient_name());
                        hasMap.put("timeSlot",appointmentCurrent.getTime_slot());
                        hasMap.put("appointment_date",appointmentCurrent.getAppointment_date());
                        hasMap.put("appointment_day",appointmentCurrent.getAppointment_day());
                        hasMap.put("status",appointmentCurrent.getStatus());
                        hasMap.put("patient_dob",appointmentCurrent.getPatient_dob());
                        hasMap.put("patient_mobile",appointmentCurrent.getPatient_mob());
                        hasMap.put("patient_image",appointmentCurrent.getPatient_dp());
                        hasMap.put("gender",appointmentCurrent.getGender());
                        hasMap.put("token",appointmentCurrent.getToken());
                        hasMap.put("patient_mob",appointmentCurrent.getPatient_mob());
                        reference.setValue(hasMap);
                        Toast.makeText(context,"Prescription Request Send",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(context, PatientDashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }else {
                        Toast.makeText(v.getRootView().getContext(), "Already Sent prescription", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        @Override
        public void onClick(View v) {


        }

    }



    public void filterList(ArrayList<PendingAppointment> filteredList) {
        mPendingAppointment = filteredList;
        notifyDataSetChanged();
    }

}
