package com.udit.testing.doctor.prescription;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.udit.testing.R;
import com.udit.testing.doctor.pending_appointment_doctor.AppointmentDoctor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class RequestedPresAdapter extends RecyclerView.Adapter<RequestedPresAdapter.ImageViewHolder>{

    private Context context;
    private ArrayList<AppointmentDoctor> mPendingAppointment;

    private ArrayList<AppointmentDoctor> mPendingAppointmentFull;

    public RequestedPresAdapter (Context context, ArrayList<AppointmentDoctor> mPendingAppointment) {
        this.context = context;
        this.mPendingAppointment = mPendingAppointment;
        mPendingAppointmentFull = new ArrayList<>(mPendingAppointment);
    }

    @NonNull
    @Override
    public RequestedPresAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.prescription_info_doc, parent, false);
        RequestedPresAdapter.ImageViewHolder imageViewHolder = new RequestedPresAdapter.ImageViewHolder(v);

        return imageViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RequestedPresAdapter.ImageViewHolder holder, int position) {
        AppointmentDoctor appointmentCurrent = mPendingAppointment.get(position);


        if (appointmentCurrent.getPatient_gender().toLowerCase().equals("male")) {
            holder.name.setText("Mr. " + appointmentCurrent.getPatient_name());
        } else if (appointmentCurrent.getPatient_gender().toLowerCase().equals("female")) {
            holder.name.setText("Mrs. " + appointmentCurrent.getPatient_name());
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

        LocalDate date1 = LocalDate.parse(d, formatter);
        int age = calculateAge(date, date1);

        holder.age.setText("Age: " + age + " Yr. (" + appointmentCurrent.getPatient_gender() + ")");

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference()
                .child("Appointments").child("pending").child("patient").child(appointmentCurrent.getPatient_mobile()).child(appointmentCurrent.getCount());

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChild("prescription")){
                    holder.request.setText("Already Prescribed");
                    holder.request.setTextColor(Color.RED);
                }
                else
                    holder.request.setText("Requested for prescription");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       // holder.request.setText("Requested for prescription");

        Picasso.get().load(appointmentCurrent.getPatient_image()).placeholder(R.drawable.doctor_pic).into(holder.imageView);
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

            // DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Appointments").child("Prescription");
            request = itemView.findViewById(R.id.prec_req);
            status = itemView.findViewById(R.id.status);
            age = itemView.findViewById(R.id.age);
            date = itemView.findViewById(R.id.date);
            name = itemView.findViewById(R.id.name);
            imageView = itemView.findViewById(R.id.image_view);

            request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                        int position = getAdapterPosition();
                        AppointmentDoctor appointmentCurrent = mPendingAppointmentFull.get(position);
                        // Query query=reference.child(appointmentCurrent.get)
                        Intent intent = new Intent(v.getRootView().getContext(), SendPrescription.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("name", appointmentCurrent.getPatient_name());
                        intent.putExtra("mobile_no", appointmentCurrent.getPatient_mobile());
                        intent.putExtra("image", appointmentCurrent.getPatient_image());
                        intent.putExtra("count", appointmentCurrent.getCount());
                        intent.putExtra("date", appointmentCurrent.getAppointment_date());
                        intent.putExtra("dob", appointmentCurrent.getPatient_dob());
                        intent.putExtra("gender", appointmentCurrent.getPatient_gender());
                        intent.putExtra("token",appointmentCurrent.getToken());

                        v.getRootView().getContext().startActivity(intent);
//                    else {
//                        Toast.makeText(v.getRootView().getContext(), "Already Sent prescription", Toast.LENGTH_SHORT).show();
//                    }
                }
            });

        }

        @Override
        public void onClick(View v) {


        }

    }



    public void filterList(ArrayList<AppointmentDoctor> filteredList) {
        mPendingAppointment = filteredList;
        notifyDataSetChanged();
    }

}
