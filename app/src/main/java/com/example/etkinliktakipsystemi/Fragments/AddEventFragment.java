package com.example.etkinliktakipsystemi.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.etkinliktakipsystemi.Models.Event;
import com.example.etkinliktakipsystemi.Models.People;
import com.example.etkinliktakipsystemi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;


public class AddEventFragment extends DialogFragment {


    private DatePickerDialog.OnDateSetListener mDataSetListener;
    private final int SELECT_IMAGE = 100;

    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;

    private ImageView imgEventImage;
    private EditText etEventName;
    private EditText etEventPlace;
    private Calendar cal;
    private TextView startDate;

    private Uri eventImageUri;
    Long eventID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();


        View view = inflater.inflate(R.layout.fragment_add_event, null);


        TextView tvEventStartingDate = view.findViewById(R.id.tvEventStartingTime);
        Button btnSave = view.findViewById(R.id.btnSave);
        TextView tvLoadImage = view.findViewById(R.id.tvLoadImage);
        imgEventImage = view.findViewById(R.id.imgEventImage);
        etEventName = view.findViewById(R.id.etEventName);
        etEventPlace = view.findViewById(R.id.etEventPlace);
        startDate = view.findViewById(R.id.tvStartDate);


        tvEventStartingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDataSetListener,
                        year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDataSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String currentDateString = DateFormat.getDateInstance().format(cal.getTime());
                startDate.setText(currentDateString);

                Toast.makeText(getActivity(), cal.getTimeInMillis() + "  " + System.currentTimeMillis(), Toast.LENGTH_LONG).show();
            }
        };


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etEventName.getText().toString().matches("") && !etEventPlace.getText().toString().matches("") && !startDate.getText().toString().matches("") && imgEventImage.getDrawable() != null) {

                    eventID = System.currentTimeMillis();
                    if (eventImageUri != null) {

                        // Code for showing progressDialog while uploading
                        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setCancelable(false);
                        progressDialog.setTitle("Uploading...");
                        progressDialog.show();

                        final StorageReference filePath = mStorageRef.child(eventID.toString());
                        mStorageRef.child(eventID.toString()).putFile(eventImageUri)
                                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Toast
                                                    .makeText(getActivity(),
                                                            "Image Uploaded!!",
                                                            Toast.LENGTH_SHORT)
                                                    .show();

                                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    WriteToDatabaseEventInfo(uri.toString());
                                                    dismiss();
                                                }
                                            });

                                        }

                                    }
                                })

                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Error, Image not uploaded
                                        progressDialog.dismiss();
                                        Toast
                                                .makeText(getActivity(),
                                                        "Etkinlik Kaydedilemedi!! Failed " + e.getMessage(),
                                                        Toast.LENGTH_SHORT)
                                                .show();

                                    }
                                })

                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                        double progress
                                                = (100.0
                                                * taskSnapshot.getBytesTransferred()
                                                / taskSnapshot.getTotalByteCount());
                                        progressDialog.setMessage(
                                                "Uploaded "
                                                        + (int) progress + "%");
                                    }

                                });
                    }

                } else {
                    Toast.makeText(getActivity(), "Tüm alanları doldurun", Toast.LENGTH_LONG).show();
                }
            }
        });

        tvLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, SELECT_IMAGE);
            }
        });


        return view;
    }

    private void WriteToDatabaseEventInfo(String eventImageURL) {

        Event newEvent = new Event(eventID, etEventName.getText().toString(), etEventPlace.getText().toString(), cal.getTimeInMillis(), eventImageURL,null);
        mDatabaseRef.child("events").child(eventID.toString()).setValue(newEvent);

        for (int i=0; i<10; i++){
            mDatabaseRef.child("events").child(eventID.toString()).child("People").child("10"+i).setValue(new People("10"+i,"people"+i,"154"+i*3));
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == SELECT_IMAGE && resultCode == Activity.RESULT_OK && data.getData() != null) {

            eventImageUri = data.getData();
            imgEventImage.setImageURI(eventImageUri);
            imgEventImage.setVisibility(View.VISIBLE);
        }

    }
}
