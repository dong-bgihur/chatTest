package com.example.myfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.myfinal.databinding.ActivityIndexBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class index extends AppCompatActivity {

    private ActivityIndexBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth Auth = FirebaseAuth.getInstance();
    /* 新增一個Arrtylist物件 */
    ArrayList<Product> products_index = new ArrayList<>();
    /* 新增一個adapter_set 物件，並傳入值(index 跟 上面的 arrlist 物件) */
    adapter_set adapter = new adapter_set(index.this,products_index);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityIndexBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerview.setAdapter(adapter);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(index.this));

        checkadmin();

        /* 從DB中抓取商品資料 */
        db.collection("TestData")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot doc : task.getResult()){
                                Product product = doc.toObject(Product.class);
                                products_index.add(product);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

        /* 賣家新增商品品項 */
        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(index.this,additems.class);
                startActivity(it);
            }
        });

        /* 買家選好品項後，前往統計畫面 */
        binding.buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(index.this,total.class);
                startActivity(it);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        products_index.clear();  // 清空原本的adapter data
        // 重新載入data
        db.collection("TestData")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot doc : task.getResult()){
                                Product product = doc.toObject(Product.class);
                                products_index.add(product);

                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    /* 載入選單 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* 選單功能列表 */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.item1:  // 登出
                new AlertDialog.Builder(index.this)
                        .setTitle("sign out")
                        .setMessage("確定要登出??")
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String userEmail = Auth.getCurrentUser().getEmail();

                                Auth.signOut();

                                Intent it = new Intent(index.this,MainActivity.class);
                                startActivity(it);

                                Toast.makeText(index.this,userEmail + "已登出" ,Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;

            case R.id.item2:  // 修改密碼

                Intent it = getIntent();
                String UserEmail = it.getStringExtra("user");

                new AlertDialog.Builder(index.this)
                        .setTitle("修改密碼")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                /* 發送修該密碼的信件 */
                                Auth.sendPasswordResetEmail(UserEmail)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(index.this,"已送出",Toast.LENGTH_SHORT).show();
                                                Log.d("Demo","已送出 密碼更新連結");
                                            }
                                        });
                            }
                        })
                        .show();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /* 判斷是否為管理員(可新增選項) */
    void checkadmin(){
        Intent it = getIntent();
        String admin = it.getStringExtra("user");
//
//        if(admin.equals(" ) ){
//            binding.buttonAdd.setVisibility(View.VISIBLE);
//        }
//        else{
//            binding.buttonAdd.setVisibility(View.GONE);
//        }
    }
}
