package com.example.etkinliktakipsystemi.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.etkinliktakipsystemi.Models.Event;
import com.example.etkinliktakipsystemi.Models.History;
import com.example.etkinliktakipsystemi.R;
import com.example.etkinliktakipsystemi.Utils.EventBusDataEvents;
import com.example.etkinliktakipsystemi.Utils.HistoryAdapter;
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

public class HistoryActivity extends AppCompatActivity {



    RecyclerView recyclerViewHistory;
    private DatabaseReference mDatabaseRef;
    ProgressBar mProgressBar;
    ArrayList<History> allHistory;

    Event mEvent;
    TextView tvEventName;
    ImageView imgBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        tvEventName = findViewById(R.id.tvEventName);
        imgBack = findViewById(R.id.imgBack);
        recyclerViewHistory = findViewById(R.id.recyclerViewHistory);

        allHistory = new ArrayList<History>();



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

        allHistory.clear();
        bringHistoryInformation(mEvent);
    }

    private void bringHistoryInformation(Event mEvent) {

        mDatabaseRef.child("events").child(mEvent.getEventID().toString()).child("history").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){

                    for (DataSnapshot ds: dataSnapshot.getChildren()){

                        History history = new History();

                        history.setScan_result(ds.getValue(History.class).getScan_result());
                        history.setProcess_time(ds.getValue(History.class).getProcess_time());
                        history.setSuccessful(ds.getValue(History.class).isSuccessful());

                        allHistory.add(history);
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

        HistoryAdapter historyAdapter = new HistoryAdapter(this,allHistory);
        recyclerViewHistory.setAdapter(historyAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,true);
        recyclerViewHistory.setLayoutManager(linearLayoutManager);
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
