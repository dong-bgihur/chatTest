package com.example.myapp110_2_2.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp110_2_2.R;

import java.util.List;

public class Demoadapter extends  RecyclerView.Adapter<DemoVH>{

    List<String> items;
    boolean isSelect = false;

    public Demoadapter(List<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public DemoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fripickeritem,parent,false);
        return new DemoVH(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull DemoVH holder, int position) {  // 點後觸發事件
        holder.textView.setText(items.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "", Toast.LENGTH_SHORT).show();
                if(!isSelect){
                    holder.imageSelected.setVisibility(View.VISIBLE);
                    holder.itemView.setBackgroundColor(Color.parseColor("#F5F5DC"));
                    isSelect= true;
                }else {
                    isSelect =false;
                    holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    holder.imageSelected.setVisibility(View.GONE);
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}


class DemoVH extends RecyclerView.ViewHolder{

    ImageView imageView,imageSelected;
    TextView textView;
    private Demoadapter dadapter;

    public DemoVH(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView2);
        textView = itemView.findViewById(R.id.textView2);
        imageSelected = itemView.findViewById(R.id.imageViewSelected);
    }

    public DemoVH linkAdapter(Demoadapter adapter){
        this.dadapter = adapter;
        return this;
    }

}