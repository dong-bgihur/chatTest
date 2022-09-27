package com.example.myfinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myfinal.databinding.ActivityAdditemsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class additems extends AppCompatActivity {
    private ActivityAdditemsBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageRef;
    private String name ="";
    private String img ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdditemsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storageRef = FirebaseStorage.getInstance().getReference();

        binding.btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* 設定產品資料 */
                String id = binding.edTid.getText().toString();
                HashMap<String,Object> AddData =   new HashMap<>();
                AddData.put("id",id);
                AddData.put("name",binding.edTname.getText().toString());
                AddData.put("type",binding.edTtype.getText().toString());
                AddData.put("price",Integer.parseInt(binding.edTprice.getText().toString()));
                AddData.put("quantity",0);
                AddData.put("img",img);



                /* 寫出資料 */
                db.collection("TestData")
                        .document(id)
                        .set(AddData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(additems.this, "確認輸入", Toast.LENGTH_SHORT).show();
                                finish(); //回首頁
                            }
                        });
            }
        });

        binding.buttonupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent picker = new Intent(Intent.ACTION_GET_CONTENT);
                picker.setType("image/*");
                startActivityForResult(picker,103);
            }
        });


        /* 回首頁 */
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 103) {
            Uri uri = data.getData();   //intent data >>  回傳圖片資料

            Glide.with(additems.this)
                    .load(uri)
                    .into(binding.imageViewupload);

            name = binding.edTname.getText().toString();
            try {
                Log.d("name", name);

                /*設定該檔案在firebase中的資料夾以及路徑名稱*/
                storageRef.child("store").child(name +".jpg")
                        .putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(additems.this, "success", Toast.LENGTH_SHORT).show();
                            Log.d("url","success");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(additems.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("url","fail");
                            }
                        });

                storageRef.child("store/" + name + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.v("url", "url=" + uri);
                        img = ""+  uri;

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("url", e.getMessage());
                    }
                });
            }catch (Exception e){
                Log.d("error", e.getMessage());
            }

        }
    }
}