package com.example.etkinliktakipsystemi.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.etkinliktakipsystemi.Models.History;
import com.example.etkinliktakipsystemi.R;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private ArrayList<History> mDataList;
    private LayoutInflater inflater;
    private Context mContext;

    public HistoryAdapter(Context context, ArrayList<History> data) {
        inflater = LayoutInflater.from(context);

        this.mDataList = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.one_history_item, parent, false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        History currentHistory = mDataList.get(position);
        holder.setData(currentHistory,position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvProcessTime;
        TextView tvScanResult;
        TextView tvIsSuccessful;
        GifImageView gifImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProcessTime = itemView.findViewById(R.id.tvTime);
            tvScanResult = itemView.findViewById(R.id.tvScanResult);
            gifImage = itemView.findViewById(R.id.gifImage);
            tvIsSuccessful = itemView.findViewById(R.id.tvIsSuccesful);
        }

        public void setData(History currentHistory, int position) {

            android.text.format.DateFormat df = new android.text.format.DateFormat();


            tvProcessTime.setText( df.format("yyyy/MM/dd hh:mm:ss a", currentHistory.getProcess_time()));
            tvScanResult.setText("Tarama İçeriği: " + currentHistory.getScan_result());
            if(currentHistory.isSuccessful()){
                gifImage.setImageResource(R.drawable.success);
                tvIsSuccessful.setText("Başarılı");
            }
            else{
                gifImage.setImageResource(R.drawable.fail);
                tvIsSuccessful.setText("Başarısız");
            }

        }
    }
}
