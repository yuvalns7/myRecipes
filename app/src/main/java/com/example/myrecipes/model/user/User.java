package com.example.myrecipes.model.user;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.myrecipes.MyApplication;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    public String id="";
    public String email="";
    public String password="";
    public String name="";
    public String avatarUrl="";
    public Long lastUpdated;

    public User(){}

    public User( String email, String password) {
        this.email = email;
        this.password = password;
    }
    public User(@NonNull String id, String email, String password, String name, String avatarUrl) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.avatarUrl = avatarUrl;
    }

    static final String NAME = "name";
    static final String ID = "id";
    static final String EMAIL = "email";
    static final String PASSWORD = "password";
    static final String AVATAR = "avatar";
    static final String COLLECTION = "users";
    static final String LAST_UPDATED = "lastUpdated";
    static final String LOCAL_LAST_UPDATED = "users_local_last_update";

    public static User fromJson(Map<String,Object> json){
        String id = (String)json.get(ID);
        String email = (String)json.get(EMAIL);
        String password = (String)json.get(PASSWORD);
        String name = (String)json.get(NAME);
        String avatar = (String)json.get(AVATAR);

        User user = new User(id,name,avatar, email, password);
        try{
            Timestamp time = (Timestamp) json.get(LAST_UPDATED);
            user.setLastUpdated(time.getSeconds());
        }catch(Exception e){}
        return user;
    }

    public static Long getLocalLastUpdate() {
        SharedPreferences sharedPref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        return sharedPref.getLong(LOCAL_LAST_UPDATED, 0);
    }

    public static void setLocalLastUpdate(Long time) {
        SharedPreferences sharedPref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(LOCAL_LAST_UPDATED,time);
        editor.commit();
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(ID, getId());
        json.put(NAME, getName());
        json.put(AVATAR, getAvatarUrl());
        json.put(LAST_UPDATED, FieldValue.serverTimestamp());
        return json;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
