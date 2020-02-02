package com.example.etkinliktakipsystemi.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.etkinliktakipsystemi.Models.Event;
import com.example.etkinliktakipsystemi.Models.History;
import com.example.etkinliktakipsystemi.R;
import com.example.etkinliktakipsystemi.Utils.EventBusDataEvents;
import com.example.etkinliktakipsystemi.Utils.UniversalImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DateFormat;

public class EventActivity extends AppCompatActivity {


    Event mEvent;
    TextView tvEventName;
    TextView tvEventDate;
    TextView tvEventPlace;
    ImageView imgEventImage;
    ImageView imgBack;
    ImageView imgScan;
    ImageView imgShowHistory;
    ImageView imgShowAttendance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


        tvEventDate = findViewById(R.id.tvEventDate);
        tvEventPlace = findViewById(R.id.tvEventPlace);
        imgEventImage = findViewById(R.id.imgEventImage);
        tvEventName = findViewById(R.id.tvEventName);
        imgBack = findViewById(R.id.imgBack);
        imgScan = findViewById(R.id.imgScan);
        imgShowHistory = findViewById(R.id.imgShowHistory);
        imgShowAttendance = findViewById(R.id.imgShowAttendance);

        imgShowHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, HistoryActivity.class);
                EventBus.getDefault().postSticky(new EventBusDataEvents.SendEvent(mEvent));
                startActivity(intent);
            }
        });

        imgShowAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, AttendanceActivity.class);
                EventBus.getDefault().postSticky(new EventBusDataEvents.SendEvent(mEvent));
                startActivity(intent);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        imgScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this,ScanActivity.class);
                EventBus.getDefault().postSticky(new EventBusDataEvents.SendEvent(mEvent));
                startActivity(intent);
            }
        });


    }


    @Subscribe(sticky = true)
    public void onTakeEventInfo(EventBusDataEvents.SendEvent event){
        mEvent = event.getEvent();
        tvEventName.setText(mEvent.getEventName());
        tvEventDate.setText(DateFormat.getDateInstance().format(mEvent.getEventFinishDate()));
        tvEventPlace.setText(mEvent.getEventPlace());
        UniversalImageLoader.setImage(mEvent.getEventImage(),imgEventImage,null);
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
