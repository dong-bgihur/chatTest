package com.example.myapp110_2_2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp110_2_2.agenda._agenda;
import com.example.myapp110_2_2.agenda._agenda2;
import com.example.myapp110_2_2.databinding.Activity9Binding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;

public class _9 extends AppCompatActivity {

    private Activity9Binding binding;
    String datetime = "";
    String datetime1 = "";
    String datetime2 = "";
    boolean isSelect=false;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = Activity9Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("行程設定");
        getSupportActionBar().hide();

        String dateStr1 = "2022-07-10 20:30";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime datelocalTime1 = LocalDateTime.parse(dateStr1, formatter);

        /* 送出資料，*/
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<Object,String> data = new HashMap<>();
                data.put("title",binding.edTextTitle.getText().toString());
                data.put("startTime",binding.textViewSt.getText().toString());
                data.put("endTime",binding.textViewEt.getText().toString());
                data.put("space",binding.editText.getText().toString());
                data.put("description",binding.editText2.getText().toString());

                db.collection("joinData").document()
                        .set(data)
                        .addOnSuccessListener(v -> {
                            Log.d("result","success");
                        })
                        .addOnFailureListener(v ->{
                            Log.d("result","fail");
                        });

                Intent it = new Intent(_9.this, _agenda.class);
                startActivity(it);

            }
        });

        binding.switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isSelect){
                    binding.textViewSt.setVisibility(View.GONE);
                    binding.textViewEt.setVisibility(View.GONE);
                    isSelect= true;
                }else {

                    binding.textViewSt.setVisibility(View.VISIBLE);
                    binding.textViewEt.setVisibility(View.VISIBLE);
                    isSelect =false;
                }
            }
        });

        binding.textViewRm.setOnClickListener(v->{
            String[] strings={"一小時前","三小時前","一天前"};

            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setSingleChoiceItems(strings, 2, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();//結束對話框
                }
            });
            builder.show();
        });

    }



    public void dayPicker(View v) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);      //取得現在的日期年月日
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                int cMonth = month+1; // month value is [0:11] , so add 1

                String Mmonth = cMonth < 10 ? "0"+ cMonth : ""+ cMonth;  // (string)月份 = (數字)月份 if 小於十 則前面補0 :(else)  直接填入數字，以下天數、小時、分鐘都一樣寫法
                String Mday = day < 10 ? "0"+ day : ""+day;

                datetime = String.valueOf(year) + "-" + Mmonth  + "-" + Mday;

                Log.v("date",datetime);
                binding.textViewday.setText(datetime);   //取得選定的日期指定給日期編輯框

            }
        }, year, month, day).show();
    }

    public void timePicker(View v) {
        Calendar calendar = Calendar.getInstance();

        int hourOfDay = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        if (v == binding.textViewSt) {
            new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    String MhourOfDay = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
                    String Mminute = minute < 10 ? "0" + minute : "" + minute;

                    datetime1 = datetime + " " + MhourOfDay + ":" + Mminute;
                    binding.textViewSt.setText(datetime1);  //取得選定的時間指定給時間編輯框
                }
            }, hourOfDay, minute, true).show();
        }

        if (v == binding.textViewEt) {
            new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    String MhourOfDay = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
                    String Mminute = minute < 10 ? "0" + minute : "" + minute;

                    datetime2 = datetime + " " + MhourOfDay + ":" + Mminute;
                    binding.textViewEt.setText(datetime2);  //取得選定的時間指定給時間編輯框
                }
            }, hourOfDay, minute, true).show();
        }
    }

}