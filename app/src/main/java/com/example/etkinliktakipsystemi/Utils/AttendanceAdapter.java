package com.example.etkinliktakipsystemi.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.etkinliktakipsystemi.Models.History;
import com.example.etkinliktakipsystemi.Models.People;
import com.example.etkinliktakipsystemi.R;

import java.lang.invoke.CallSite;
import java.util.ArrayList;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.MyViewHolder> {

    private ArrayList<People> mDataList;
    private LayoutInflater inflater;
    private Context mContext;
    public AttendanceAdapter(Context context, ArrayList<People> data) {

        inflater = LayoutInflater.from(context);

        this.mDataList = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public AttendanceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.one_attendance_item, parent, false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceAdapter.MyViewHolder holder, int position) {
        People currentPeople = mDataList.get(position);
        holder.setData(currentPeople,position);

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvQrId;
        TextView tvNameSurname;
        TextView tvPhoneNumber;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvQrId = itemView.findViewById(R.id.tvQrId);
            tvNameSurname = itemView.findViewById(R.id.tvNameSurname);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
        }

        public void setData(People currentPeople, int position) {
            tvQrId.setText(currentPeople.getQr_id());
            tvNameSurname.setText(currentPeople.getName_surname());
            tvPhoneNumber.setText(currentPeople.getPhone_number());
        }
    }
}
