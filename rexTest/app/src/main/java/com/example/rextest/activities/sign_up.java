package com.example.rextest.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.rextest.databinding.ActivitySignUpBinding;
import com.example.rextest.utilities.Constants;
import com.example.rextest.utilities.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.O)
public class sign_up extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private String[] hobby1 ={"美妝保養","教育學習","居家婦幼","醫療保健","視聽娛樂","流行服飾","旅遊休閒","3C娛樂"};
    private boolean[] hobbyInChecked={false,false,false,false,false,false,false,false};
    private String encodedImage;
    private PreferenceManager preferenceManager;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//    Map<String, Object> user = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());
        setLiseteners();

        binding.btnHubby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(sign_up.this)
                        .setMultiChoiceItems(hobby1,hobbyInChecked, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                hobbyInChecked[which] = isChecked;
                            }
                        })
                        .setPositiveButton("送出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String myhobby ="";
                                for(int i=0; i<hobbyInChecked.length; i++)
                                {
                                    if(hobbyInChecked[i])
                                        myhobby += " " + hobby1[i];
                                }
                                binding.btnHubby.setText(myhobby);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        });

    }

    private void setLiseteners(){
        binding.btnsubmit.setOnClickListener(v -> {
            if(isVaildSignUpDetails()){
                signup();
            }
        });
        binding.fr.setOnClickListener(v -> {
            Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(it);
        });
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void signup(){
        loading(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME,binding.et1name.getText().toString());
        user.put(Constants.KEY_EMAIL,binding.et2email.getText().toString());
        user.put(Constants.KEY_PASSWORD,binding.etpasswd.getText().toString());
        user.put(Constants.KEY_IMAGE,encodedImage);
        db.collection(Constants.KEY_COLLECTION_USERS)
        .add(user)
        .addOnSuccessListener(documentReference -> {
            loading(false);
            preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
            preferenceManager.putString(Constants.KEY_USER_ID,documentReference.getId());
            preferenceManager.putString(Constants.KEY_NAME,binding.et1name.getText().toString());
            preferenceManager.putString(Constants.KEY_IMAGE,encodedImage);
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        })
        .addOnFailureListener(exception ->{
            loading(false);
            showToast(exception.getMessage());
        });
    }

    private  String enodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth , previewHeight,false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(bytes);
    }
    
    private final ActivityResultLauncher<Intent> pickerImage = registerForActivityResult(
           new ActivityResultContracts.StartActivityForResult(),
            result -> {
               if(result.getResultCode() == RESULT_OK){
                   Uri imageUri = result.getData().getData();
                   try{
                       InputStream inputStream = getContentResolver().openInputStream(imageUri);
                       Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                       binding.imageProfile.setImageBitmap(bitmap);
                       encodedImage = encodedImage(bitmap);
                   }catch (FileNotFoundException e){
                       e.printStackTrace();
                   }
               }
            }
    );

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String encodedImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(bytes);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK){
                    if(result.getData() != null){
                        Uri imageUri = result.getData().getData();
                        try{
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageProfile.setImageBitmap(bitmap);
                            binding.textAddImage.setVisibility(View.GONE);
                            encodedImage = encodedImage(bitmap);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private Boolean isVaildSignUpDetails(){
        if (encodedImage == null){
            showToast("Select profile image");
            return  false;
        }else if(binding.et1name.getText().toString().trim().isEmpty()){
            showToast("Enter name");
            return false;
        }else if(binding.et2email.getText().toString().trim().isEmpty()){
            showToast("Enter email");
            return false;
        }else if(binding.et1name.getText().toString().trim().isEmpty()){
            showToast("Enter name");
            return false;
//        }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.et1name.getText().toString()).matches()){
//            showToast("Enter valid email");
//            return false;
        }else if(binding.etpasswd.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
//        }else if(!binding.et4repasswd.getText().toString().equals(binding.et2email.getText().toString())) {
//            showToast("password and confirm password must be same");
//            return false;
        }else if(binding.btnHubby.getText().toString().equals("嗜好類別")){
            showToast("select the hubby");
            return false;
        }else{
            return true;
        }

    }

    // progress bar
    private void loading(boolean isloading){
        if(isloading){
            binding.btnsubmit.setVisibility(View.INVISIBLE);
        }else {
            binding.btnsubmit.setVisibility(View.VISIBLE);
        }

    }

}