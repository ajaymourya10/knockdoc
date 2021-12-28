package com.udit.testing.patient.slotbooking;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udit.testing.Model.Common;
import com.udit.testing.R;
import com.udit.testing.patient.AppointmentRequest3;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.MyViewHolder> {


    Context context;
    List<TimeSlot> timeSlotList;
    List<TimeSlot> timeSlotFull;
    List<CardView> cardViewList;
    TimeSlot timeSlot;
    String time,x;
    //slotAdapter slotAdapter;
    LocalBroadcastManager localBroadcastManager;
    SimpleDateFormat simpleDateFormat;

    public MyTimeSlotAdapter(Context context) {
        this.context = context;
        this.timeSlotList = new ArrayList<>();
        //this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    public MyTimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.context = context;
        this.timeSlotList = timeSlotList;
        this.timeSlotFull = new ArrayList<>(timeSlotList);
        // this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.layout_time_slot,parent,false);
        simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
        MyViewHolder imageViewHolder=new MyViewHolder(v);
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        //TimeSlot timeSlot=timeSlotList.get(position);

            holder.txt_time_slot.setText(new StringBuilder(Common.convertTimeSlotToString(position)).toString());

            holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));

            holder.txt_time_slot_description.setText("Available");
            holder.txt_time_slot_description.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.black));

    }

    @Override
    public int getItemCount() {
        return 21;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_time_slot,txt_time_slot_description;
                CardView card_time_slot;
        public MyViewHolder(View v) {
            super(v);
            card_time_slot = (CardView)v.findViewById(R.id.card_time_slot);
            txt_time_slot = (TextView)v.findViewById(R.id.txt_time_slot);
            txt_time_slot_description = (TextView)v.findViewById(R.id.txt_time_slot_description);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_light));
                        Common.currentTimeSlot=getAdapterPosition();
                        String time=Common.currentTimeSlot+"";
                        String timesl=Common.convertTimeSlotToString(Common.currentTimeSlot);
                           TimeSlot timeSlot= timeSlotFull.get(0);
                            Log.d("Pttttt","msg :- "+getAdapterPosition());
                            Intent intent=new Intent(v.getRootView().getContext(), AppointmentRequest3.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("time_slot",timesl);
                            intent.putExtra("name",timeSlot.get_name());
                            intent.putExtra("dob",timeSlot.get_dob());
                            intent.putExtra("appointment_date",timeSlot.get_appointmentDate());
                            intent.putExtra("address",timeSlot.get_address());
                            intent.putExtra("aadhar",timeSlot.get_aadharNo());
                            intent.putExtra("doctor_mobile",timeSlot.get_doctorMobileNo());
                            intent.putExtra("gender",timeSlot.get_gender());
                            intent.putExtra("day",timeSlot.get_day());
                            intent.putExtra("mobile",timeSlot.get_mobileNo());
                            intent.putExtra("rating",timeSlot.getRating());
                            intent.putExtra("token",timeSlot.getToken());
                            intent.putExtra("gender",timeSlot.get_gender());
                           // intent.putExtra("position",time);
                            Log.d("tttttttttttttttttt","msg:- "+timesl+timeSlot.get_name()+timeSlot.get_appointmentDate()+time);
                            v.getRootView().getContext().startActivity(intent);

//                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Appointments").
//                                child("time").child(timeSlot.get_doctorMobileNo())
//                                        .child(timeSlot.get_appointmentDate()).child(time);
//                        Map<String, String> hasMap = new HashMap<>();
//                        hasMap.put("position", time);
//                        hasMap.put("time_slot", timesl);
//                        reference.setValue(hasMap);


                    }
                });

        }

        @Override
        public void onClick(View v) {

        }
    }
}
