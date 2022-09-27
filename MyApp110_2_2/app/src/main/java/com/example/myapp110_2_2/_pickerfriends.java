package com.example.myapp110_2_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapp110_2_2.adapter.Demoadapter;
import com.example.myapp110_2_2.databinding.ActivityPickerfriendsBinding;
import java.util.LinkedList;
import java.util.List;

public class _pickerfriends extends AppCompatActivity {

    private ActivityPickerfriendsBinding binding;
    String[] name = {"A","B","C","D","E"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPickerfriendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        List<String> items = new LinkedList<>();

        RecyclerView recyclerView = findViewById(R.id.recycleView_fpicker);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Demoadapter adapter = new Demoadapter(items);
        recyclerView.setAdapter(adapter);

        for(String i : name){
            items.add(i);
        }
        adapter.notifyDataSetChanged();

        binding.BtnAddfpicker.setOnClickListener(view -> startActivity(new Intent(this,_timePicker.class)));

    }
}