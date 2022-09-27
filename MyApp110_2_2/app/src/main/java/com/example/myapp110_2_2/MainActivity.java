package com.example.myapp110_2_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myapp110_2_2.agenda._agenda;
import com.example.myapp110_2_2.agenda._agenda2;
import com.example.myapp110_2_2.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("主畫面");
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btn9.setText("event setting");
        binding.btn10.setText("calendar");
        binding.btn11.setText("time Clac");
        binding.btn12.setText("G_calendar");
        binding.btn13.setText("agenda");
        binding.btn9.setOnClickListener(clickListener);
        binding.btn10.setOnClickListener(clickListener);
        binding.btn11.setOnClickListener(clickListener);
        binding.btn12.setOnClickListener(clickListener);
        binding.btn13.setOnClickListener(clickListener);


    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent it = new Intent();

            if( view == binding.btn9)
                it.setClass(MainActivity.this,_9.class);
            if(view == binding.btn10)
                it.setClass(MainActivity.this,_group.class);
            if(view == binding.btn11)
                it.setClass(MainActivity.this, _agenda2.class);
            if(view == binding.btn12)
                it.setClass(MainActivity.this,_timeclac.class);
            if(view == binding.btn13)
                it.setClass(MainActivity.this, _agenda.class);

            startActivity(it);
        }
    };
}