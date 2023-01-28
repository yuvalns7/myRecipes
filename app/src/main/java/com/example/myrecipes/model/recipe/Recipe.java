package com.example.myrecipes.model.recipe;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.myrecipes.MyApplication;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Recipe {
    @PrimaryKey
    @NonNull
    private String id="";
    private String name="";
    private String category="";
    private String area="";
    private String instructions="";
    private String imgUrl="";
//    private List<String> ingredients = new ArrayList<>();
    private Long lastUpdated;

    public Recipe(){
    }

    public Recipe(@NonNull String id,String name, String category, String area, String instructions, String imgUrl
//            ,  List<String> ingredients
    ) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.area = area;
        this.instructions = instructions;
        this.imgUrl = imgUrl;
//        this.ingredients = ingredients;
    }

    static final String ID = "id";
    static final String NAME = "name";
    static final String CATEGORY = "category";
    static final String AREA = "area";
    static final String INSTRUCTIONS = "instructions";
    static final String IMG_URL = "imgUrl";
    static final String INGREDIENTS = "ingredients";


    public static final String COLLECTION = "recipes";
    public static final String LAST_UPDATED = "lastUpdated";
    public static final String LOCAL_LAST_UPDATED = "recipes_local_last_update";

    public static Recipe fromJson(Map<String,Object> json){
        String id = (String)json.get(ID);
        String name = (String)json.get(NAME);
        String category = (String)json.get(CATEGORY);
        String area = (String)json.get(AREA);
        String instructions = (String) json.get(INSTRUCTIONS);
        String imgUrl = (String) json.get(IMG_URL);
//        List<String> ingredients = (List<String>) json.get(INGREDIENTS);

        Recipe rcp = new Recipe(id,name, category,area,instructions,imgUrl );
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
        json.put(ID, getId());
        json.put(NAME, getName());
        json.put(CATEGORY, getCategory());
        json.put(AREA, getArea());
        json.put(INSTRUCTIONS, getInstructions());
        json.put(IMG_URL, getImgUrl());
//        json.put(INGREDIENTS, getIngredients());
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

//    public List<String> getIngredients() {
//        return ingredients;
//    }
//
//    public void setIngredients(List<String> ingredients) {
//        this.ingredients = ingredients;
//    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
