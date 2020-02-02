package com.example.etkinliktakipsystemi.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.etkinliktakipsystemi.Models.Event;
import com.example.etkinliktakipsystemi.Models.History;
import com.example.etkinliktakipsystemi.Models.People;
import com.example.etkinliktakipsystemi.R;
import com.example.etkinliktakipsystemi.Utils.EventBusDataEvents;
import com.example.etkinliktakipsystemi.Utils.UniversalImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.nio.channels.Channels;
import java.text.DateFormat;

import pl.droidsonroids.gif.GifImageView;

public class QrResultActivity extends AppCompatActivity {



    DatabaseReference mRef;

    TextView tvQrResult;
    ImageView imgBack;

    Event mEvent;
    String scanResult;
    GifImageView gifImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_result);

        mRef = FirebaseDatabase.getInstance().getReference();

        gifImageView = findViewById(R.id.gifImage);
        tvQrResult = findViewById(R.id.tvQrResult);
        imgBack = findViewById(R.id.imgBack);


        Intent intent = getIntent();
        scanResult = intent.getStringExtra("scanResult");



        tvQrResult.setText(scanResult);

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

        final TextView tvStatus = findViewById(R.id.tvStatus);
        final TextView tvNameSurname = findViewById(R.id.tvNameSurname);
        final TextView tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        TextView tvEventName = findViewById(R.id.tvEventName);
        tvEventName.setText(mEvent.getEventName());

        if(scanResult != null){

            final History history = new History();
            history.setScan_result(scanResult);
            history.setProcess_time(System.currentTimeMillis());
            mRef.child("events").child(mEvent.getEventID().toString()).child("People").addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChildren()){

                        boolean isFind = false;
                        for (DataSnapshot ds: dataSnapshot.getChildren()){

                            String qr_id = ds.getValue(People.class).getQr_id();

                            if(qr_id.equals(scanResult)){
                                People currentPeople = new People();

                                currentPeople.setQr_id(qr_id);
                                currentPeople.setName_surname(ds.getValue(People.class).getName_surname());
                                currentPeople.setPhone_number(ds.getValue(People.class).getPhone_number());
                                gifImageView.setImageResource(R.drawable.success);
                                isFind = true;



                                tvStatus.setText("Kayıt Başarılı");

                                tvNameSurname.setVisibility(View.VISIBLE);
                                tvPhoneNumber.setVisibility(View.VISIBLE);

                                tvNameSurname.setText("Adı Soyadı: "+currentPeople.getName_surname());
                                tvPhoneNumber.setText("Telefon: "+currentPeople.getPhone_number());

                                mRef.child("events").child(mEvent.getEventID().toString()).child("Attendance").child(qr_id).setValue(currentPeople);

                                history.setSuccessful(true);

                                mRef.child("events").child(mEvent.getEventID().toString()).child("history").child(history.getProcess_time().toString()).setValue(history);
                            }

                        }

                        if(!isFind){

                            gifImageView.setImageResource(R.drawable.fail);
                            tvStatus.setText("Başvuru Yapılmamış. Kayıt Başarısız");

                            tvNameSurname.setVisibility(View.GONE);
                            tvPhoneNumber.setVisibility(View.GONE);

                            history.setSuccessful(false);
                            mRef.child("events").child(mEvent.getEventID().toString()).child("history").child(history.getProcess_time().toString()).setValue(history);

                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

           // mRef.child("events").child(mEvent.getEventID().toString()).child("history").child(history.getProcess_time().toString()).setValue(history);
        }
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
