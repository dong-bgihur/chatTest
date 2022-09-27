package com.example.myapp110_2_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.example.myapp110_2_2.adapter.Timeadapter;
import com.example.myapp110_2_2.databinding.ActivityTimePickerBinding;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class _timePicker extends AppCompatActivity {

    private String datetime;
    private ActivityTimePickerBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimePickerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        List<String> items = new LinkedList<>();

        binding.button6.setOnClickListener(v->{
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
                    binding.button6.setText(datetime);
                }
            }, year, month, day).show();
        });

        binding.button7.setOnClickListener(v->{
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
                    binding.button7.setText(datetime);
                }
            }, year, month, day).show();
        });

        RecyclerView recyclerView = findViewById(R.id.recycleView_timepicker);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Timeadapter adapter = new Timeadapter(items);
        recyclerView.setAdapter(adapter);

        items.add("時段設定");  // first item
        binding.timepickerAddButton1.setOnClickListener(v ->{
        items.add("時段設定");
        adapter.notifyItemChanged(items.size()-1);
        });

        binding.BtnAddTimepicker.setOnClickListener(view -> startActivity(new Intent(this,_finishEvent.class)));

    }
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };
}