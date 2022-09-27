package com.example.myapp110_2_2.adapter;

import android.app.TimePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp110_2_2.R;

import java.util.Calendar;
import java.util.List;

public class Timeadapter extends  RecyclerView.Adapter<TimeVH>{

    List<String> items;
    String datetime="";
    String datetime1="";
    String datetime2="";

    public Timeadapter(List<String> items) {
        this.items = items;
    }
    @NonNull
    @Override
    public TimeVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timepickeritem,parent,false);
        return new TimeVH(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeVH holder, int position) {
        holder.textView.setText(items.get(position));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                int hourOfDay = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);

                new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        String MhourOfDay = hourOfDay < 10 ? "0"+ hourOfDay : ""+ hourOfDay ;
                        String Mminute =  minute < 10 ? "0"+ minute : ""+ minute ;

                        datetime2 = datetime1 +"~" + MhourOfDay + ":" + Mminute;
                        holder.textView.setText(""+datetime2);  //取得選定的時間指定給時間編輯框
                    }
                }, hourOfDay, minute, true).show();

                new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        String MhourOfDay = hourOfDay < 10 ? "0"+ hourOfDay : ""+ hourOfDay ;
                        String Mminute =  minute < 10 ? "0"+ minute : ""+ minute ;

                        datetime1 =datetime+"  "+ MhourOfDay + ":" + Mminute;
                        holder.textView.setText(datetime1);  //取得選定的時間指定給時間編輯框
                    }
                }, hourOfDay, minute, true).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

class TimeVH extends RecyclerView.ViewHolder{

    TextView textView;
    private Timeadapter tadapter;

    public TimeVH(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.textViewtime1);
    }

    public TimeVH linkAdapter(Timeadapter adapter){
        this.tadapter = adapter;
        return this;
    }

}