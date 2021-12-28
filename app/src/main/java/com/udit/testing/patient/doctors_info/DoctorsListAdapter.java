package com.udit.testing.patient.doctors_info;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.udit.testing.Model.Doctor;
import com.udit.testing.R;
import com.udit.testing.patient.AppointmentRequest;

import java.util.ArrayList;
import java.util.Calendar;

public class DoctorsListAdapter extends RecyclerView.Adapter<DoctorsListAdapter.ImageViewHolder> {
    private Context context;
    private ArrayList<Doctor> mDoctors;

    private ArrayList<Doctor> mDoctorFull;
    String token;

    public DoctorsListAdapter(Context context, ArrayList<Doctor> mDoctors) {
        this.context = context;
        this.mDoctors = mDoctors;
        mDoctorFull = new ArrayList<>(mDoctors);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.doctors_info, parent, false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(v);

        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Doctor doctorCurrent = mDoctors.get(position);
        holder.textViewName.setText(doctorCurrent.getName());
        holder.textViewDegree.setText(doctorCurrent.getDegree());
        holder.textViewSpecialist.setText(doctorCurrent.getDoctor_type());
        holder.fee.setText(doctorCurrent.getFee() + " " + " \u20B9");
        //token=doctorCurrent.getToken();
        if (doctorCurrent.getRating().equals("none")) {
            holder.ratingBar.setVisibility(View.GONE);
            holder.noRating.setVisibility(View.VISIBLE);
        } else {
            holder.noRating.setVisibility(View.GONE);
            String ratingValue = doctorCurrent.getRating();
            float actualRating = Float.valueOf(ratingValue);
            holder.ratingBar.setRating(actualRating);
        }

        if (!doctorCurrent.getProfile_image().equals("none")) {
            Picasso.get().load(doctorCurrent.getProfile_image()).placeholder(R.drawable.doctor_pic).into(holder.imageview);
        }
    }

    @Override
    public int getItemCount() {
        return mDoctors.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textViewName, textViewDegree, textViewSpecialist, fee, noRating;
        public ImageView imageview;
        public RatingBar ratingBar;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textViewName = itemView.findViewById(R.id.doctor_name);
            textViewDegree = itemView.findViewById(R.id.doctor_degree);
            textViewSpecialist = itemView.findViewById(R.id.doctor_speciality);
            fee = itemView.findViewById(R.id.doctor_fees_actual);
            noRating = itemView.findViewById(R.id.no_rating);
            ratingBar = itemView.findViewById(R.id.doctor_rating_bar);
            imageview = itemView.findViewById(R.id.image_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Doctor appointmentCurrent = mDoctorFull.get(position);

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext(),
                            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background);
                    View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.
                            doctor_details, null);


                    TextView name = dialogView.findViewById(R.id.dr_name);
                    TextView type = dialogView.findViewById(R.id.dr_type);
                    TextView address = dialogView.findViewById(R.id.dr_clinic_address);
                    TextView degree = dialogView.findViewById(R.id.dr_degree);
                    TextView fee = dialogView.findViewById(R.id.dr_fee);
                    TextView experience = dialogView.findViewById(R.id.dr_experience);
                    TextView ratingValue = dialogView.findViewById(R.id.dr_rating_value);
                    TextView city = dialogView.findViewById(R.id.dr_clinic_city);
                    RatingBar ratingBar = dialogView.findViewById(R.id.dr_rating_bar);
                    TextView rat = dialogView.findViewById(R.id.rat);
                    Button bookAppointment = dialogView.findViewById(R.id.book_appointment);
                    RoundedImageView pic = dialogView.findViewById(R.id.dr_pic);

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                            .child("Users");

                    Query query = reference.child("Doctor").child(appointmentCurrent.getMobile_no());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String  _experience, _ratingValue, _fee, _city, _address;
                                String starting_year = snapshot.child("clinic").child("starting_year").getValue().toString();
                                _city = snapshot.child("clinic").child("city").getValue().toString();
                                _address = snapshot.child("clinic").child("address").getValue().toString();
                                _ratingValue = snapshot.child("rating").getValue().toString();
                                _fee = snapshot.child("fee").getValue().toString();

                                int current_year = Calendar.getInstance().get(Calendar.YEAR);
                                _experience = String.valueOf(current_year - Integer.parseInt(starting_year));
                                name.setText("Dr. " + appointmentCurrent.getName());
                                type.setText(appointmentCurrent.getDoctor_type());
                                degree.setText( appointmentCurrent.getDegree());
                                fee.setText("Doctor's Fee "+_fee+" \u20B9");
                                address.setText("Clinic's Address: "+_address);
                                city.setText("City: "+_city);
                                experience.setText("with experience of "+_experience+" years" );

                                Typeface font = ResourcesCompat.getFont(context, R.font.roboto_slab);
                                if (font != null) {

                                    if (name != null) name.setTypeface(font, Typeface.BOLD);
                                    if (degree != null) degree.setTypeface(font,Typeface.BOLD);
                                    if (type != null) type.setTypeface(font,Typeface.BOLD_ITALIC);
                                    if (experience != null) experience.setTypeface(font,Typeface.BOLD_ITALIC);
                                    if (fee != null) fee.setTypeface(font);
                                    if (city != null) city.setTypeface(font);
                                    if (address != null) address.setTypeface(font);

                                }

                                if(_ratingValue.equals("none")){
                                    ratingBar.setVisibility(View.GONE);
                                    ratingValue.setText("No Rating");
                                    ratingValue.setTextColor(Color.RED);
                                }else{
                                    ratingValue.setText( _ratingValue+" / 5");
                                }

                                Typeface font1 = ResourcesCompat.getFont(context, R.font.baloo);
                                if (font1 != null) {

                                    if (ratingValue != null) ratingValue.setTypeface(font1);
                                    rat.setTypeface(font1);
                                    //bookAppointment.setTextSize(TypedValue.COMPLEX_UNIT_SP,1f);

                                }

                                if (!appointmentCurrent.getProfile_image().equals("none")) {
                                    Picasso.get().load(appointmentCurrent.getProfile_image()).placeholder(R.drawable.doctor_pic).into(pic);
                                } else {
                                    Picasso.get().load(R.drawable.doctor_pic).into(pic);
                                }
                                Float _ratingBar = Float.valueOf(_ratingValue);

                                ratingBar.setRating(_ratingBar);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            Toast.makeText(v.getRootView().getContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });


                    bookAppointment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getRootView().getContext(), AppointmentRequest.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("mobile_no",appointmentCurrent.getMobile_no());
                            intent.putExtra("rating",appointmentCurrent.getRating());
                           // intent.putExtra("token",appointmentCurrent.getToken());
                            v.getRootView().getContext().startActivity(intent);
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

    public void filterList(ArrayList<Doctor> filteredList) {
        mDoctors = filteredList;
        notifyDataSetChanged();
    }
}
