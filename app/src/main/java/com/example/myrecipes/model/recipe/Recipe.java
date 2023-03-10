package com.example.myrecipes.model.recipe;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.myrecipes.MyApplication;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Recipe implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @NonNull
    private String name="";
    private String category="";
    private String instructions="";
    private String imgUrl="";
    private String ingredients = "";
    private String userId = "";
    private String username = "";
    private Long lastUpdated;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] photo;

    public Recipe(){
    }

    public Recipe(String name, String category, String instructions, String imgUrl, String ingredients, String userId, String username) {
        this.name = name;
        this.category = category;
        this.instructions = instructions;
        this.imgUrl = imgUrl;
        this.ingredients = ingredients;
        this.userId = userId;
        this.username = username;
    }

    public Recipe(String name, String category, String instructions, String ingredients, String userId, String username) {
        this.name = name;
        this.category = category;
        this.instructions = instructions;
        this.ingredients = ingredients;
        this.userId = userId;
        this.username = username;
    }

    public static final String USER_ID = "userId";
    public static final String NAME = "name";
    public static final String CATEGORY = "category";
    public static final String INSTRUCTIONS = "instructions";
    public static final String IMG_URL = "imgUrl";
    public static final String INGREDIENTS = "ingredients";

    public static final String USERNAME = "username";


    public static final String COLLECTION = "recipes";
    public static final String LAST_UPDATED = "lastUpdated";
    public static final String LOCAL_LAST_UPDATED = "recipes_local_last_update";

    public static Recipe fromJson(Map<String,Object> json){
        String name = (String)json.get(NAME);
        String category = (String)json.get(CATEGORY);
        String instructions = (String) json.get(INSTRUCTIONS);
        String imgUrl = (String) json.get(IMG_URL);
        String ingredients = (String) json.get(INGREDIENTS);
        String userId = (String) json.get(USER_ID);
        String username = (String) json.get(USERNAME);

        Recipe rcp = new Recipe(name, category,instructions,imgUrl, ingredients, userId, username);
        try{
            Timestamp time = (Timestamp) json.get(LAST_UPDATED);
            rcp.setLastUpdated(time.getSeconds());
        }catch(Exception e){}
        return rcp;
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
        json.put(NAME, getName());
        json.put(CATEGORY, getCategory());
        json.put(INSTRUCTIONS, getInstructions());
        json.put(IMG_URL, getImgUrl());
        json.put(INGREDIENTS, getIngredients());
        json.put(LAST_UPDATED, FieldValue.serverTimestamp());
        json.put(USER_ID, getUserId());
        json.put(USERNAME, getUsername());

        return json;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}
