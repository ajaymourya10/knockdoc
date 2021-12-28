package com.udit.testing.patient.slotbooking;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udit.testing.Model.Common;
import com.udit.testing.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import dmax.dialog.SpotsDialog;



public class MyCalendarDoctorActivity extends AppCompatActivity implements ITimeSlotLoadListener {



    ITimeSlotLoadListener iTimeSlotLoadListener;
    AlertDialog alertDialog;
    //com.udit.testing.Model.slotAdapter slotAdapter;
    TimeSlot timeSlot;
    List<TimeSlot> timeSlots;

    private String _name, _mobileNo, _gender,rating,doc_type, _address, _dob,_day, token,_appointmentDate,_ptPic, _aadharNo,_doctorMobileNo,profile;
    MyTimeSlotAdapter myTimeSlotAdapter;

    private RecyclerView recycler_time_slot;
   // com.udit.testing.Model.slotAdapter slotAdapter;

    HorizontalCalendarView calendarView;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_calendar_doctor);

        recycler_time_slot=findViewById(R.id.recycle_time_slot2);
        calendarView=findViewById(R.id.calendarView2);
        timeSlots=new ArrayList<>();
        timeSlot =new TimeSlot();
        Intent intent1 = getIntent();
        timeSlot.set_name(intent1.getStringExtra("name"));
        Log.d("tttttt","mamm"+intent1.getStringExtra("name"));
        timeSlot.set_dob(intent1.getStringExtra("dob"));
        timeSlot.set_appointmentDate(intent1.getStringExtra("appointment_date"));
        timeSlot.set_address(intent1.getStringExtra("address"));
        timeSlot.set_aadharNo(intent1.getStringExtra("aadhar"));
        timeSlot.set_doctorMobileNo(intent1.getStringExtra("doctor_mobile"));
        timeSlot.set_gender(intent1.getStringExtra("gender"));
        timeSlot.set_day(intent1.getStringExtra("day"));
        timeSlot.set_mobileNo(intent1.getStringExtra("mobile")); //intent1.getStringExtra("mobile"));
        timeSlot.setRating(intent1.getStringExtra("rating"));
        timeSlot.setToken(intent1.getStringExtra("token"));
        timeSlot.set_gender(intent1.getStringExtra("gender"));
        timeSlots.add(timeSlot);
        myTimeSlotAdapter=new MyTimeSlotAdapter(getApplicationContext(), timeSlots);
        recycler_time_slot.setAdapter(myTimeSlotAdapter);
        //ButterKnife.bind(this);
        init();
       // iTimeSlotLoadListener.onTimeSlotLoadEmpty();
        ClearAll();



    }



    private void init() {
        //iTimeSlotLoadListener = this;
        alertDialog = new SpotsDialog.Builder().setCancelable(false).setContext(this)
                .build();
        Calendar date = Calendar.getInstance();
        date.add(Calendar.DATE,0);
        //loadAvailabelTimeSlotOfDoctor(Common.CurreentDoctor,simpleDateFormat.format(date.getTime()));
        recycler_time_slot.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        recycler_time_slot.setLayoutManager(gridLayoutManager);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE,0);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE,5);
        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this,R.id.calendarView2)
                .range(startDate,endDate)
                .datesNumberOnScreen(1)
                .mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(startDate)
                .build();
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                if(Common.currentDate.getTimeInMillis() != date.getTimeInMillis()){
                    Common.currentDate = date;
                       // timeSlot.set_appointmentDate(simpleDateFormat.format(date.getTime()));
                  //  loadAvailabelTimeSlotOfDoctor(Common.CurreentDoctor,simpleDateFormat.format(date.getTime()));

                }

            }
        });

    }



    @Override
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList) {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(this,timeSlotList);
        recycler_time_slot.setAdapter(adapter);
        alertDialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadFailed(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadEmpty() {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(this);
        recycler_time_slot.setAdapter(adapter);
        alertDialog.dismiss();
    }

    private void ClearAll() {
        if (timeSlots != null) {
            timeSlots.clear();
            if (myTimeSlotAdapter != null)
                myTimeSlotAdapter.notifyDataSetChanged();
        }
        timeSlots = new ArrayList<>();
    }
}
