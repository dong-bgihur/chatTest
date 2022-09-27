package com.example.myfinal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class adapter_item extends RecyclerView.Adapter<adapter_item.ViewHolder> {

    Context mContext;
    ArrayList<Product> product_T;

    public adapter_item(Context mContext, ArrayList<Product> products_total) {
        this.mContext = mContext;
        this.product_T = products_total;
    }

    @NonNull
    @Override
    public adapter_item.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View itemView = layoutInflater.inflate(R.layout.total,parent,false);  // load total page

        ViewHolder holder = new ViewHolder(itemView);

        holder.textView = itemView.findViewById(R.id.tV_name3);
        holder.textView2 = itemView.findViewById(R.id.tV_num3);
        holder.textView3 = itemView.findViewById(R.id.tV_price3);
        holder.imageViewItem = itemView.findViewById(R.id.imageViewItem);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_item.ViewHolder holder, int position) {
        Product product = product_T.get(position);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        /*將Data 寫入adapter中*/
        db.collection("TestData")
                .whereEqualTo("id",product.id)  //用輸入的id去比對DB的id
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot doc : task.getResult()){

                                String name = doc.getString("name");
                                int price = Integer.parseInt(doc.get("price").toString());

                                Glide.with(mContext)
                                        .load(doc.getString("img"))
                                        .into(holder.imageViewItem);

                                /* 獲取買家設定的商品數量 */
                                db.collection("TotalData")
                                        .whereEqualTo("id",product.id)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                                        int quantity = Integer.parseInt(doc.get("quantity").toString());
                                                        int P = quantity * price;  // 計算總額

                                                        holder.textView.setText(name);
                                                        holder.textView2.setText(""+quantity);
                                                        holder.textView3.setText(""+ P);
                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });

        /* 購物車長按後刪除品項 */
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int id = holder.itemView.getId();
                new AlertDialog.Builder(mContext)
                        .setTitle("刪除確認")
                        .setMessage("確認要刪除"+product.name+"這項產品?")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.collection("TotalData").document(product.id).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("su","success");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("fail",e.getMessage());
                                    }
                                });
                                Log.d("del",""+product.id);

                                notifyItemRemoved(id);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消返回", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(mContext, "~", Toast.LENGTH_SHORT).show();
                            }
                        }).show();

                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return product_T.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView,textView2,textView3;  // 版面設定
        ImageView imageViewItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
