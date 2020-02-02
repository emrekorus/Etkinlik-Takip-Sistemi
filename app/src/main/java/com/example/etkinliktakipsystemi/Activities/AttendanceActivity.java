package com.example.etkinliktakipsystemi.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.etkinliktakipsystemi.Models.Event;
import com.example.etkinliktakipsystemi.Models.People;
import com.example.etkinliktakipsystemi.R;
import com.example.etkinliktakipsystemi.Utils.AttendanceAdapter;
import com.example.etkinliktakipsystemi.Utils.EventBusDataEvents;
import com.example.etkinliktakipsystemi.Utils.UniversalImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DateFormat;
import java.util.ArrayList;

public class AttendanceActivity extends AppCompatActivity {


    Event mEvent;
    RecyclerView recyclerViewAttendance;
    private DatabaseReference mDatabaseRef;
    ArrayList<People> allAttendPeople;

    TextView tvEventName;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        tvEventName = findViewById(R.id.tvEventName);
        imgBack = findViewById(R.id.imgBack);
        recyclerViewAttendance = findViewById(R.id.recyclerViewAttendance);


        allAttendPeople = new ArrayList<People>();


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Subscribe(sticky = true)
    public void onTakeEventInfo(EventBusDataEvents.SendEvent event){
        mEvent = event.getEvent();
        tvEventName.setText(mEvent.getEventName());

        allAttendPeople.clear();
        bringAttendPeopleInformation(mEvent);
    }

    private void bringAttendPeopleInformation(Event mEvent) {
        mDatabaseRef.child("events").child(mEvent.getEventID().toString()).child("Attendance").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){

                    for (DataSnapshot ds: dataSnapshot.getChildren()){

                        People people = new People();

                        people.setQr_id(ds.getValue(People.class).getQr_id());
                        people.setName_surname(ds.getValue(People.class).getName_surname());
                        people.setPhone_number(ds.getValue(People.class).getPhone_number());

                        allAttendPeople.add(people);
                    }

                }

                setupRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupRecyclerView() {

        AttendanceAdapter attendanceAdapter = new AttendanceAdapter(this,allAttendPeople);
        recyclerViewAttendance.setAdapter(attendanceAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,true);
        recyclerViewAttendance.setLayoutManager(linearLayoutManager);

    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
