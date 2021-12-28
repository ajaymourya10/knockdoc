package com.udit.testing.patient.rating;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.udit.testing.R;
import com.udit.testing.patient.appointment_info.PendingAppointment;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ApprovedAdapter extends RecyclerView.Adapter<ApprovedAdapter.ImageViewHolder>{

    private Context context;
    private ArrayList<PendingAppointment> mPendingAppointment;

    private ArrayList<PendingAppointment> mPendingAppointmentFull;

    public ApprovedAdapter(Context context, ArrayList<PendingAppointment> mPendingAppointment) {
        this.context = context;
        this.mPendingAppointment = mPendingAppointment;
        mPendingAppointmentFull = new ArrayList<>(mPendingAppointment);
    }

    @NonNull
    @Override
    public ApprovedAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.appointment_view, parent, false);
        ApprovedAdapter.ImageViewHolder imageViewHolder = new ApprovedAdapter.ImageViewHolder(v);

        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ApprovedAdapter.ImageViewHolder holder, int position) {
        PendingAppointment appointmentCurrent = mPendingAppointment.get(position);
      //  if(appointmentCurrent.getStatus().equals("approved")) {
            holder.drName.setText("Dr. " + appointmentCurrent.getDoctor_name());
            //holder.textViewDegree.setText(appointmentCurrent.getDegree());
            holder.textViewSpecialist.setText(appointmentCurrent.getDoctor_type());
            holder.appointmentdate.setText(appointmentCurrent.getAppointment_date());

            if (!appointmentCurrent.getStatus().equals("")) {
                holder.status.setText("Tap to Rate");
                holder.status.setTextColor(Color.YELLOW);
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
       // }

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
                            docTypeTextView.setText(appointmentCurrent.getDegree() + " (" + appointmentCurrent.getDoctor_type() + ")");

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


                                statusTextView.setText("Tap to Rate");
                                statusTextView.setTextColor(Color.GREEN);


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
                 dialogView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Intent intent=new Intent(context,HomeActivity.class);
                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                         intent.putExtra("ptname",appointmentCurrent.getPatient_name());
                         intent.putExtra("drmob",appointmentCurrent.getDoctor_mobile());
                         context.startActivity(intent);
                     }
                 });

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
