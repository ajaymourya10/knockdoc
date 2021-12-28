package com.udit.testing.doctor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udit.testing.R;

import java.util.ArrayList;

public class ViewRatingAdapter extends RecyclerView.Adapter<ViewRatingAdapter.ImageViewHolder> {
    private float total;
    private float pos;
    private Context context;
    private ArrayList<HelperRating> mPendingAppointment;

    private ArrayList<HelperRating> mPendingAppointmentFull;

    public ViewRatingAdapter(Context context, ArrayList<HelperRating> mPendingAppointment) {
        this.context = context;
        this.mPendingAppointment = mPendingAppointment;
        mPendingAppointmentFull = new ArrayList<>(mPendingAppointment);
    }

    @NonNull
    @Override
    public ViewRatingAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.viewadapter, parent, false);
        ViewRatingAdapter.ImageViewHolder imageViewHolder = new ViewRatingAdapter.ImageViewHolder(v);

        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewRatingAdapter.ImageViewHolder holder, int position) {
        HelperRating appointmentCurrent = mPendingAppointment.get(position);
        //  if(appointmentCurrent.getStatus().equals("approved")) {
        holder.ratingBar.setRating(Float.parseFloat(appointmentCurrent.getRating()));
        holder.feedback.setText(appointmentCurrent.getFeedback());

        total+=Float.parseFloat(appointmentCurrent.getRating());
        pos=mPendingAppointment.size();
        Log.d("tooooo","total "+total+pos);

    }

    @Override
    public int getItemCount() {
        return mPendingAppointment.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView feedback;
        public RatingBar ratingBar;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            ratingBar = itemView.findViewById(R.id.Rating);
            feedback = itemView.findViewById(R.id.Feedback);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HelperRating helperRating=mPendingAppointment.get(getAdapterPosition());
                    String tot=""+total/pos;
                    Toast.makeText(context," "+tot,Toast.LENGTH_SHORT).show();
                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users").child("Doctor")
                            .child(helperRating.getMob()).child("rating");
                    reference.setValue(tot);


                }
            });

        }

        @Override
        public void onClick(View v) {


        }

    }

//    public String toDisplayCase(String s) {
//
//        final String ACTIONABLE_DELIMITERS = " '-/"; // these cause the character following
//        // to be capitalized
//
//        StringBuilder sb = new StringBuilder();
//        boolean capNext = true;
//
//        for (char c : s.toCharArray()) {
//            c = (capNext)
//                    ? Character.toUpperCase(c)
//                    : Character.toLowerCase(c);
//            sb.append(c);
//            capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0); // explicit cast not needed
//        }
//        return sb.toString();
//    }


    public void filterList(ArrayList<HelperRating> filteredList) {
        mPendingAppointment = filteredList;
        notifyDataSetChanged();
    }

}
