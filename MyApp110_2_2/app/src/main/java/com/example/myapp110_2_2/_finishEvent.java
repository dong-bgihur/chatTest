package com.example.myapp110_2_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapp110_2_2.agenda._agenda2;

public class _finishEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_event);

        getSupportActionBar().hide();

        findViewById(R.id.button5).setOnClickListener(v->{
            startActivity(new Intent(this, _agenda2.class));
        });

    }
}