package com.example.myapp110_2_2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapp110_2_2.databinding.ActivityTimeclacBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;


public class _timeclac extends AppCompatActivity {

    private ActivityTimeclacBinding binding;
    String datetime = "";
    String datetime1 = "";
    String datetime2 = "";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimeclacBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("行程設定");


        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String dateStr1 = df.format(new Date());
        dateStr1 =  "2022-07-10 20:30:00";
        Timestamp timestamp = Timestamp.valueOf(dateStr1);
        Log.d("demoTime","" + timestamp);
//        db.collection()



        String dateStr2 = "2022-07-20 10:00";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//        LocalDateTime datelocalTime1 = LocalDateTime.parse(dateStr1, formatter);
        LocalDateTime datelocalTime2 = LocalDateTime.parse(dateStr2, formatter);


        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                Log.v("test","" +  datelocalTime1 + "& " );
//                LocalDateTime datetimepicker = LocalDateTime.parse(datetime1 , formatter);
//
//                if(datetimepicker.isBefore(datelocalTime1)){
//                    binding.button4.setText("is before");
//
//                    Log.v("test", "" + datetimepicker.isBefore(datelocalTime1));
//                    Log.v("time", "" + datetimepicker);
//                    Log.v("time2", "" + datelocalTime1);
//                }else{
//                    binding.button4.setText("is after");
//                }
            }
        });
    }

    public void dayPickerclaclac(View v) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);      //取得現在的日期年月日
        int month = calendar.get(Calendar.MONTH) ;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                int cMonth = month+1; // month value is [0:11] , so add 1

                String Mmonth = cMonth < 10 ? "0"+ cMonth : ""+ cMonth;  // (string)月份 = (數字)月份 if 小於十 則前面補0 :(else)  直接填入數字，以下天數、小時、分鐘都一樣寫法
                String Mday = day < 10 ? "0"+ day : ""+day;

                datetime = String.valueOf(year) + "-" + Mmonth  + "-" + Mday;

                Log.v("date",datetime);
                binding.textViewdayc.setText(datetime);   //取得選定的日期指定給日期編輯框

            }
        }, year, month, day).show();

    }

    public void timePickerclac(View v) {
        Calendar calendar = Calendar.getInstance();

        int hourOfDay = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        if (v == binding.textViewclacSt) {
            new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    String MhourOfDay = hourOfDay < 10 ? "0"+ hourOfDay : ""+ hourOfDay ;
                    String Mminute =  minute < 10 ? "0"+ minute : ""+ minute ;

                    datetime1 = datetime +" " + MhourOfDay + ":" + Mminute;
                    binding.textViewclacSt.setText(datetime1);  //取得選定的時間指定給時間編輯框
                }
            }, hourOfDay, minute, true).show();
        }

        if (v == binding.textViewclacEt) {
            new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    String MhourOfDay = hourOfDay < 10 ? "0"+ hourOfDay : ""+ hourOfDay ;
                    String Mminute =  minute < 10 ? "0"+ minute : ""+ minute ;

                    datetime2 = datetime +" " + MhourOfDay + ":" + Mminute;
                    binding.textViewclacEt.setText(datetime2);  //取得選定的時間指定給時間編輯框
                }
            }, hourOfDay, minute, true).show();
        }
    }
}