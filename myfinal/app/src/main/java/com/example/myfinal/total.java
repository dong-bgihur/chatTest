package com.example.myfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myfinal.databinding.ActivityTotalBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class total extends AppCompatActivity {
    private ActivityTotalBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTotalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* 設定adapter */
        ArrayList<Product> products_total = new ArrayList<>();
        adapter_item adapter = new adapter_item(total.this,products_total);
        binding.recyclerview.setAdapter(adapter);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(total.this));

//        products_total.clear();
        /* 取出資料， 計算總和 */
        db.collection("TotalData")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            int sum =0;

                            for (QueryDocumentSnapshot doc : task.getResult()){
                                Product product = doc.toObject(Product.class);
                                products_total.add(product);

                                int p = Integer.parseInt(doc.get("price").toString());
                                int q = Integer.parseInt(doc.get("quantity").toString());
                                int Pri = q * p;
                                sum += Pri;
                            }
                            binding.textViewT.setText("總計 = "+sum);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("demo",e.getMessage());
            }
        });

        /* 回首頁 */
        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}