package com.example.etkinliktakipsystemi.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.etkinliktakipsystemi.Fragments.AddEventFragment;
import com.example.etkinliktakipsystemi.Models.Event;
import com.example.etkinliktakipsystemi.R;
import com.example.etkinliktakipsystemi.Utils.EventAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    private DatabaseReference mDatabaseRef;
    ProgressBar mProgressBar;
    ArrayList<Event> allEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();


        allEvents = new ArrayList<Event>();
        recyclerView = findViewById(R.id.scrollableview);
        mProgressBar = findViewById(R.id.progressBar2);

        allEvents.clear();
        bringEventInformation();





        //getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Event Add", Toast.LENGTH_LONG).show();


                AddEventFragment addEventFragment = new AddEventFragment();
                addEventFragment.show(getSupportFragmentManager(),"add Event Fragment");
            }
        });


    }

    private void bringEventInformation() {


        mProgressBar.setVisibility(View.VISIBLE);
        mDatabaseRef.child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){

                    for (DataSnapshot ds: dataSnapshot.getChildren()){

                        Event event = new Event();
                        event.setEventID(ds.getValue(Event.class).getEventID());
                        event.setEventName(ds.getValue(Event.class).getEventName());
                        event.setEventFinishDate(ds.getValue(Event.class).getEventFinishDate());
                        event.setEventImage(ds.getValue(Event.class).getEventImage());
                        event.setEventPlace(ds.getValue(Event.class).getEventPlace());

                        allEvents.add(event);
                    }

                }
                else{

                    CountDownTimer countDownTimer = new CountDownTimer(200,200) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            mProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFinish() {

                        }
                    }.start();

                    mProgressBar.setVisibility(View.VISIBLE);
                }


                setupRecyclerView();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setupRecyclerView() {

        if(allEvents.size() == 0){
            TextView tvNoEvent = findViewById(R.id.tvNoEvent);
            tvNoEvent.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
        else{
            mProgressBar.setVisibility(View.GONE);
            EventAdapter eventAdapter = new EventAdapter(MainActivity.this, allEvents);
            recyclerView.setAdapter(eventAdapter);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2, RecyclerView.VERTICAL, true);
            recyclerView.setLayoutManager(gridLayoutManager);
        }

    }
}
