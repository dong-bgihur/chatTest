package com.example.rextest.activities;


import androidx.annotation.RequiresApi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.rextest.R;
import com.example.rextest.adapters.RecentConversationsAdapter;
import com.example.rextest.databinding.ActivityMainBinding;
import com.example.rextest.listeners.ConversionListener;
import com.example.rextest.models.ChatMessage;
import com.example.rextest.models.User;
import com.example.rextest.utilities.Constants;
import com.example.rextest.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity implements ConversionListener {

    private ActivityMainBinding binding;
    private PreferenceManager preferenceManager;
    private List<ChatMessage> conversations;
    private RecentConversationsAdapter conversationsAdapter;
    private FirebaseFirestore db;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        preferenceManager = new PreferenceManager(getApplicationContext());
        loadUserDetails();
        getToken();
        setListeners();
        listenConversation();
    }

    private void  init(){
        conversations = new ArrayList<>();
        conversationsAdapter = new RecentConversationsAdapter(conversations,this);
        binding.conversationsRecyclerView.setAdapter(conversationsAdapter);
        db =FirebaseFirestore.getInstance();
        binding.navigation.setSelectedItemId(R.id.talk);
        setTitle("JoinUs");
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(this.getResources().getColor(R.color.black));//狀態列顏色
    }


    private void setListeners(){
        binding.textName.setOnClickListener(v ->{  signout(); });
        binding.fabNewChat.setOnClickListener( view -> {
            startActivity(new Intent(getApplicationContext(), UsersActivity.class));
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadUserDetails(){
        binding.textName.setText(preferenceManager.getString(Constants.KEY_NAME));
        byte[] bytes = Base64.getDecoder().decode(preferenceManager.getString(Constants.KEY_IMAGE));
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void listenConversation(){
        db.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
        db.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_RECEIVER_ID,preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = ((value, error) -> {
       if(error != null){
           return;
       }
       if(value != null){
           for(DocumentChange documentChange : value.getDocumentChanges()){
               if(documentChange.getType() == DocumentChange.Type.ADDED){
                   String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                   String recevierId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                   ChatMessage chatMessage = new ChatMessage();
                   chatMessage.senderId = senderId;
                   chatMessage.receiverId = recevierId;
                   if(preferenceManager.getString(Constants.KEY_USER_ID).equals(senderId)){
                       chatMessage.conversionImage = documentChange.getDocument().getString(Constants.KEY_RECEIVER_IMAGE);
                       chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME);
                       chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                   }else{
                       chatMessage.conversionImage = documentChange.getDocument().getString(Constants.KEY_SENDER_IMAGE);
                       chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
                       chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                   }
                   chatMessage.message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                   chatMessage.dateObjcet = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                   conversations.add(chatMessage);
               }else if(documentChange.getType() == DocumentChange.Type.MODIFIED){
                   for(int i=0;i < conversations.size(); i++){
                       String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                       String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                       if(conversations.get(i).senderId.equals(senderId) && conversations.get(i).receiverId.equals(receiverId)){
                           conversations.get(i).message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                           conversations.get(i).dateObjcet = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                           break;
                       }
                   }
               }
           }//for loop
           Collections.sort(conversations,(obj1,obj2) -> obj2.dateObjcet.compareTo(obj1.dateObjcet));
           conversationsAdapter.notifyDataSetChanged();
           binding.conversationsRecyclerView.smoothScrollToPosition(0);
           binding.conversationsRecyclerView.setVisibility(View.VISIBLE);
           binding.progressBar.setVisibility(View.GONE);
       }
    });

    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token){
        preferenceManager.putString(Constants.KEY_FCM_TOKEN,token);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                db.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        documentReference.update(Constants.KEY_FCM_TOKEN,token)
//                .addOnSuccessListener(unused -> showToast("Token update successfully"))
                .addOnFailureListener(e -> showToast("Unable to update token"));
    }

    private void signout(){
        showToast("Signing out");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                db.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String,Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());  // delete token
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(),sing_in.class));
                    finish();
                })
                .addOnFailureListener(e -> showToast("unable to sign out"));
    }

    @Override
    public void onConversionClicked(User user) {
        Intent it = new Intent(getApplicationContext(),ChatActivity.class);
        it.putExtra(Constants.KEY_USER,user);
        startActivity(it);
    }
}