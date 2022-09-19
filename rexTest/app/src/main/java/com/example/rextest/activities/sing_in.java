package com.example.rextest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.rextest.databinding.ActivitySingInBinding;
import com.example.rextest.utilities.Constants;
import com.example.rextest.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class sing_in extends AppCompatActivity {

    private ActivitySingInBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("this is login in");
        preferenceManager = new PreferenceManager(getApplicationContext());

        binding.btnSignin.setOnClickListener(v -> {
            if (isVaildSignInDetails()) {
                signin();
            }
        });

        binding.textnewacc.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), sign_up.class)));
    }

    private void signin(){
        db.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL,binding.etacc.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD,binding.etpasswd.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null
                            && task.getResult().getDocuments().size() >0 ){
                        DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                        preferenceManager.putString(Constants.KEY_USER_ID, doc.getId());
                        preferenceManager.putString(Constants.KEY_NAME,doc.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_IMAGE,doc.getString(Constants.KEY_IMAGE));
                        Intent it = new Intent(getApplicationContext(), MainActivity.class);
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                        startActivity(it);
                    }else {
                        showToast("Unable to sign in");
                    }
                })
                .addOnFailureListener(exception ->{

                });

    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Boolean isVaildSignInDetails(){
        if(binding.etacc.getText().toString().isEmpty()){
            showToast("Enter email");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.etacc.getText().toString()).matches()) {  //  attention!! matcher只包字，matches在外面
            showToast("Enter vaild email");
            return false;
        }else if(binding.etpasswd.getText().toString().isEmpty()){
            showToast("Enter password");
            return false;
        }else{
            return true;
        }

    }

}