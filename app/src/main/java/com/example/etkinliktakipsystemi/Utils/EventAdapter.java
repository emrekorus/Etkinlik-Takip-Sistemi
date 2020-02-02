package com.example.etkinliktakipsystemi.Utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.etkinliktakipsystemi.Activities.EventActivity;
import com.example.etkinliktakipsystemi.Models.Event;
import com.example.etkinliktakipsystemi.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private ArrayList<Event> mDataList;
    private LayoutInflater inflater;
    private Context mContext;
    public EventAdapter(Context context, ArrayList<Event> data) {
        inflater = LayoutInflater.from(context);

        this.mDataList = data;

        this.mContext = context;
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(context);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = inflater.inflate(R.layout.one_event_item, parent, false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Event clickingEvent = mDataList.get(position);
        holder.setData(clickingEvent,position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mEventName;
        ImageView mEventImage;
        ProgressBar mProgressBar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mEventName = itemView.findViewById(R.id.tvEventName);
            mEventImage = itemView.findViewById(R.id.imgEvent);
            mProgressBar = itemView.findViewById(R.id.progressBar);
        }

        public void setData(final Event clickingEvent, int position) {
            this.mEventName.setText(clickingEvent.getEventName());
            UniversalImageLoader.setImage(clickingEvent.getEventImage(),mEventImage,mProgressBar);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, EventActivity.class);
                    EventBus.getDefault().postSticky(new EventBusDataEvents.SendEvent(clickingEvent));
                    mContext.startActivity(intent);


                }
            });
        }
    }

}
