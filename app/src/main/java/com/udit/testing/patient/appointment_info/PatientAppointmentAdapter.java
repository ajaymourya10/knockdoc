package com.udit.testing.patient.appointment_info;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.udit.testing.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientAppointmentAdapter extends RecyclerView.Adapter<PatientAppointmentAdapter.ImageViewHolder> {
    private Context context;
    private ArrayList<PendingAppointment> mPendingAppointment;

    private ArrayList<PendingAppointment> mPendingAppointmentFull;

    public PatientAppointmentAdapter(Context context, ArrayList<PendingAppointment> mPendingAppointment) {
        this.context = context;
        this.mPendingAppointment = mPendingAppointment;
        mPendingAppointmentFull = new ArrayList<>(mPendingAppointment);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.appointment_view, parent, false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(v);

        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        PendingAppointment appointmentCurrent = mPendingAppointment.get(position);
        holder.drName.setText("Dr. " + appointmentCurrent.getDoctor_name());
        //holder.textViewDegree.setText(appointmentCurrent.getDegree());
        holder.textViewSpecialist.setText(appointmentCurrent.getDoctor_type());
        holder.appointmentdate.setText(appointmentCurrent.getAppointment_date());

        if (!appointmentCurrent.getStatus().equals("")) {
            holder.status.setText("Pending Appointment");
            holder.status.setTextColor(Color.RED);
        }

        if (appointmentCurrent.getRating().equals("none")) {
            holder.ratingBar.setVisibility(View.GONE);
            holder.noRating.setVisibility(View.VISIBLE);
        } else {
            holder.noRating.setVisibility(View.GONE);
            String ratingValue = appointmentCurrent.getRating();
            float actualRating = Float.valueOf(ratingValue);
            holder.ratingBar.setRating(actualRating);
        }

        if (!appointmentCurrent.getDoctor_dp().equals("none")) {
            Picasso.get().load(appointmentCurrent.getDoctor_dp()).placeholder(R.drawable.doctor_pic).into(holder.imageview);
        }

    }

    @Override
    public int getItemCount() {
        return mPendingAppointment.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView drName, noRating, textViewDegree, textViewSpecialist, appointmentdate, status;
        public ImageView imageview;
        public RatingBar ratingBar;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            drName = itemView.findViewById(R.id.name);
            textViewDegree = itemView.findViewById(R.id.degree);
            textViewSpecialist = itemView.findViewById(R.id.speciality);
            appointmentdate = itemView.findViewById(R.id.appointment_date);
            status = itemView.findViewById(R.id.status);
            noRating = itemView.findViewById(R.id.rating);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            imageview = itemView.findViewById(R.id.photo);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    PendingAppointment appointmentCurrent = mPendingAppointmentFull.get(position);

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                    View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.
                            appointment_info_patient, null);

                    RoundedImageView ptImage = dialogView.findViewById(R.id.pt_pic);
                    CircleImageView docImage = dialogView.findViewById(R.id.dr_pic);
                    TextView docNameTextView = dialogView.findViewById(R.id.dr_name);
                    TextView docTypeTextView = dialogView.findViewById(R.id.dr_type);
                    TextView ptNameTextView = dialogView.findViewById(R.id.pt_name);
                    TextView dobTextView = dialogView.findViewById(R.id.pt_dob);
                    TextView dateTextView = dialogView.findViewById(R.id.appointment_date);
                    TextView dayTextView = dialogView.findViewById(R.id.appointment_day);
                    TextView timeSlotTextView = dialogView.findViewById(R.id.time_slot);
                    TextView statusTextView = dialogView.findViewById(R.id.appointment_status);
                    RatingBar ratingBar = dialogView.findViewById(R.id.rating_bar);
                    TextView noRating = dialogView.findViewById(R.id.rating);
                    TextView dummy = dialogView.findViewById(R.id.abcd);

                    docNameTextView.setText("Dr. " + appointmentCurrent.getDoctor_name());
                    docTypeTextView.setText(appointmentCurrent.getDegree()+" ("+appointmentCurrent.getDoctor_type()+")");

                    if (appointmentCurrent.getRating().equals("none")) {
                        ratingBar.setVisibility(View.GONE);
                        noRating.setVisibility(View.VISIBLE);
                    } else {
                        noRating.setVisibility(View.GONE);
                        float actualRating = Float.parseFloat(appointmentCurrent.getRating());
                        ratingBar.setRating(actualRating);
                    }

                    if (appointmentCurrent.getGender().toLowerCase().equals("male")) {
                        ptNameTextView.setText("Mr. " + appointmentCurrent.getPatient_name());
                    } else if (appointmentCurrent.getGender().toLowerCase().equals("female")) {
                        ptNameTextView.setText("Ms. " + appointmentCurrent.getPatient_name());
                    } else {
                        ptNameTextView.setText(appointmentCurrent.getPatient_name());
                    }

                    dobTextView.setText(appointmentCurrent.getPatient_dob());
                    dateTextView.setText(appointmentCurrent.getAppointment_date());
                    dayTextView.setText(toDisplayCase(appointmentCurrent.getAppointment_day()));
                    timeSlotTextView.setText(appointmentCurrent.getTime_slot());
                    timeSlotTextView.setTextIsSelectable(true);

                    if (!appointmentCurrent.getStatus().equals("")) {
                        statusTextView.setText("Pending Appointment");
                        statusTextView.setTextColor(Color.RED);
                    }

                    if (!appointmentCurrent.getDoctor_dp().equals("none")) {
                        Picasso.get().load(appointmentCurrent.getDoctor_dp()).placeholder(R.drawable.doctor_pic).into(docImage);
                    } else {
                        Picasso.get().load(R.drawable.doctor_pic).placeholder(R.drawable.doctor_pic).into(docImage);
                    }

                    Typeface font1 = ResourcesCompat.getFont(context, R.font.baloo);
                    if (font1 != null) {

                        if (dummy != null) dummy.setTypeface(font1);
                        //bookAppointment.setTextSize(TypedValue.COMPLEX_UNIT_SP,1f);

                    }

                    Picasso.get().load(appointmentCurrent.getPatient_dp()).placeholder(R.drawable.doctor_pic).into(ptImage);


                    builder.setView(dialogView);
                    builder.show();
                }
            });

        }

        @Override
        public void onClick(View v) {


        }

    }

    public String toDisplayCase(String s) {

        final String ACTIONABLE_DELIMITERS = " '-/"; // these cause the character following
        // to be capitalized

        StringBuilder sb = new StringBuilder();
        boolean capNext = true;

        for (char c : s.toCharArray()) {
            c = (capNext)
                    ? Character.toUpperCase(c)
                    : Character.toLowerCase(c);
            sb.append(c);
            capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0); // explicit cast not needed
        }
        return sb.toString();
    }


    public void filterList(ArrayList<PendingAppointment> filteredList) {
        mPendingAppointment = filteredList;
        notifyDataSetChanged();
    }
}

