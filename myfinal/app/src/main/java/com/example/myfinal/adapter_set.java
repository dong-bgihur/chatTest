package com.example.myfinal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;

public class adapter_set extends RecyclerView.Adapter<adapter_set.ViewHolder> {

    private FirebaseAuth Auth = FirebaseAuth.getInstance();
    private android.content.Context mContext;
    ArrayList<Product> products;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public adapter_set(Context mContext, ArrayList<Product> products) {
        this.mContext = mContext;
        this.products = products;
    }

    @NonNull
    @Override
    public adapter_set.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View itemView = layoutInflater.inflate(R.layout.set,parent,false);

        /* adapter 版面設定 */
        ViewHolder holder =  new ViewHolder(itemView);
        holder.button1 = itemView.findViewById(R.id.btn_add);
        holder.button2 = itemView.findViewById(R.id.btn_reduce);
        holder.textView = itemView.findViewById(R.id.tV_name);
        holder.textView2 = itemView.findViewById(R.id.tV_num);
        holder.textView3 = itemView.findViewById(R.id.tV_price);
        holder.imageViewSet = itemView.findViewById(R.id.imageViewSet);

        Auth = FirebaseAuth.getInstance();

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_set.ViewHolder holder, int position) {
        Product product = products.get(position);
//        db.collection("TotalData").document(product.name).delete();

        holder.textView.setText(product.name);
        holder.textView2.setText(""+product.quantity);
        holder.textView3.setText("$"+product.price);

        Glide.with(mContext)
                .load(product.img)
                .into(holder.imageViewSet);

        /* "+"按鈕 */
        holder.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> TotalData = new HashMap<>();

                int i = Integer.parseInt( holder.textView2.getText().toString());
                i=i+1;
                holder.textView2.setText(""+i);
                if(i> 0){
                    TotalData.put("id",product.id);
                    TotalData.put("name",product.name);
                    TotalData.put("price",product.price);
                    TotalData.put("quantity",i);

                    db.collection("TotalData")
                            .document(product.name)
                            .set(TotalData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("demo","+1");
                                }
                            });
                }
            }
        });

        /* "-"按鈕 */
        holder.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> TotalData =   new HashMap<>();
                int i = Integer.parseInt( holder.textView2.getText().toString());
                if(i>=1){
                    i=i-1;
                    holder.textView2.setText(""+i);
                }
                if(i> 0){
                    TotalData.put("id",product.id);
                    TotalData.put("name",product.name);
                    TotalData.put("price",product.price);
                    TotalData.put("quantity",i);

                    db.collection("TotalData")
                            .document(product.name)
                            .set(TotalData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("demo","-1");
                                }
                            });

                }else if(i==0){
                    db.collection("TotalData")
                            .document(product.name)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("demo","0");
                                }
                            });

                }
            }
        });

        String user =  Auth.getCurrentUser().getEmail();
        if(user.equals("4a890075@stust.edu.tw")) {
            /* 長按後刪除 */
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int id = holder.itemView.getId();
                    new AlertDialog.Builder(mContext)
                            .setTitle("刪除確認")
                            .setMessage("確認要刪除" + product.name + "這項產品?")
                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    db.collection("TestData").document(product.id).delete();
                                    Log.d("del", "" + id);
                                    notifyItemRemoved(id);
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("取消返回", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(mContext, "繼續瀏覽資料", Toast.LENGTH_SHORT).show();
                                }
                            }).show();

                    return false;
                }
            });

            /* 點擊後修改 */
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                    View update = layoutInflater.inflate(R.layout.update, null);

                    db.collection("TestData")
                            .document(product.id)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    /* 設定修改視窗的元件，先findId */
                                    EditText edName = update.findViewById(R.id.edTname2);
                                    EditText edId = update.findViewById(R.id.edTid2);
                                    EditText edType = update.findViewById(R.id.edTtype2);
                                    EditText edPrice = update.findViewById(R.id.edTprice2);
                                    /* 從DB抓資料來設定文字 */
                                    edName.setText(product.name);
                                    edId.setText(product.id);
                                    edType.setText(product.type);
                                    edPrice.setText("" + product.price);
                                    /*輸入data*/
                                    new AlertDialog.Builder(mContext)
                                            .setTitle("修改")
                                            .setView(update)
                                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    HashMap<String, Object> AddData = new HashMap<>();
                                                    AddData.put("id", edId.getText().toString());
                                                    AddData.put("name", edName.getText().toString());
                                                    AddData.put("type", edType.getText().toString());
                                                    AddData.put("price", Integer.parseInt(edPrice.getText().toString()));
                                                    AddData.put("quantity", 0);

                                                    db.collection("TestData")
                                                            .document(product.id)
                                                            .set(AddData)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    notifyDataSetChanged();
                                                                    Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            })
                                            .setNegativeButton("取消", null)
                                            .show();
                                }
                            });
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView,textView2,textView3;
        Button button1,button2;
        ImageView imageViewSet;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
