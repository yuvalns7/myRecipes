package com.example.myrecipes.model.user;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;

@Entity
public class User {

    @PrimaryKey
    @NonNull
    public String id="";
    public String email="";
    public String password="";
    public String name ="";
    public String avatarUrl;

    public User(){}

    public User( String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User( String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
    public User(String id, String email, String password, String name, String avatarUrl) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.avatarUrl = avatarUrl;
    }

    public User(String id, String email, String name, String avatarUrl) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.avatarUrl = avatarUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
