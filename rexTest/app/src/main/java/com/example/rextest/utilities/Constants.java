package com.example.rextest.utilities;

import java.util.HashMap;

public class Constants {
    public static final String KEY_COLLECTION_USERS = "users";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PERFERENCE_NAME = "chatAppPreference";
    public static final String KEY_IS_SIGNED_IN = "isSingnedIn";
    public static final String KEY_USER_ID = "usedId";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_FCM_TOKEN = "femToken";
    public static final String KEY_USER = "user";
    //
    public static final String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIMESTAMP = "timestamp";
    //
    public static final String KEY_COLLECTION_CONVERSATIONS ="conversations";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_RECEIVER_NAME = "receiverName";
    public static final String KEY_SENDER_IMAGE = "senderImage";
    public static final String KEY_RECEIVER_IMAGE = "receiverImage";
    public static final String KEY_LAST_MESSAGE = "lastMessage";
    // login highlight
    public static final String KEY_AVAILABILITY = "availability";
    // ep 12 > notifications
    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String RENOTE_MSG_CONTENT_TYPE = "Content-Type";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";


    public static HashMap<String, String> remotoMsgHeaders = null;

    public static HashMap<String, String> getRemoteMsgHeaders(){
        if(remotoMsgHeaders == null){
            remotoMsgHeaders = new HashMap<>();
            remotoMsgHeaders.put(
                    REMOTE_MSG_AUTHORIZATION,
                    "key=AAAAuBMq9K0:APA91bH3L8Uy3Nn2WtZnRfRGMDHZ9REsvICGtqRhRpGgBxqMWHIHCEs99N56wRGc-LnqYqtgGjBbc7RoU2bibfqL8gXOu4pIS9j-wUdeD5vHpF-6om2ZH3qpaRwuqamgQ0p0ZxbOqrwh"
            );
            remotoMsgHeaders.put(
                    RENOTE_MSG_CONTENT_TYPE,
                    "application/json"
            );
        }
        return remotoMsgHeaders;
    }


}
