package com.example.myrecipes.model.recipe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myrecipes.model.firebase.FirebaseModel;
import com.example.myrecipes.model.localDB.AppLocalDb;
import com.example.myrecipes.model.localDB.AppLocalDbRepository;
import com.example.myrecipes.model.user.UserModel;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RecipeModel {
    final public static RecipeModel _instance = new RecipeModel();

    private Executor executor = Executors.newSingleThreadExecutor();
    private FirebaseModel firebaseModel = new FirebaseModel();
    AppLocalDbRepository localDb = AppLocalDb.getAppDb();

    public static RecipeModel instance(){
        return _instance;
    }

    public enum LoadingState{
        LOADING,
        NOT_LOADING
    }
    final public MutableLiveData<LoadingState> EventListLoadingState = new MutableLiveData<LoadingState>(LoadingState.NOT_LOADING);

    public interface Listener<T>{
        void onComplete(T data);
    }

    private LiveData<List<Recipe>> recipesList;
    public LiveData<List<Recipe>> getAllRecipes() {
        if(recipesList == null){
            recipesList = localDb.recipeDao().getAll();
            refreshAllRecipes();
        }
        return recipesList;
    }

    public void refreshAllRecipes(){
        EventListLoadingState.setValue(LoadingState.LOADING);
        // get local last update
        Long localLastUpdate = Recipe.getLocalLastUpdate();
        // get all updated recorde from firebase since local last update
        firebaseModel.getAllRecipesSince(localLastUpdate,list->{
            executor.execute(()->{
                Log.d("TAG", " firebase return : " + list.size());
                Long time = localLastUpdate;
                for(Recipe rcp:list){
                    // insert new records into ROOM

                    rcp.setPhoto(urlToByteArr(rcp.getImgUrl()));

                    localDb.recipeDao().insertAll(rcp);
                    if (time < rcp.getLastUpdated()){
                        time = rcp.getLastUpdated();
                    }
                }
                // update local last update
                Recipe.setLocalLastUpdate(time);
                setRecipeCount();
                EventListLoadingState.postValue(LoadingState.NOT_LOADING);
            });
        });
    }

    public void addRecipe(Recipe rcp, Listener<Void> listener){
        firebaseModel.addRecipe(rcp,(Void)->{
            refreshAllRecipes();
            listener.onComplete(null);
        });
    }

    public void uploadImage(String name, Bitmap bitmap, Listener<String> listener) {
        firebaseModel.uploadImage(name,bitmap,listener);
    }

    private Integer userRecipeCount = -1;
    public Integer getUserRecipeCount() {
        if(userRecipeCount == -1){
            setRecipeCount();
        }
        return userRecipeCount;
    }

    private void setRecipeCount() {
        userRecipeCount = localDb.recipeDao().countRecipeByUser(firebaseModel.getUserId());
    }

    public void resetDataOnLogout() {
        userRecipeCount = -1;
    }

    public boolean isRecipeNameExists(String recipeName) {
        Recipe recipe = localDb.recipeDao().findByName(recipeName);
        return recipe != null;
    }

    public byte[] urlToByteArr(String link) {
        byte[] imageBytes = null;
        try {
            URL url = new URL(link);
            InputStream inputStream = url.openStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            imageBytes = outputStream.toByteArray();
            inputStream.close();
            outputStream.close();

        } catch(Exception e) {
            System.out.println(e);
        } finally {
            return imageBytes;
        }
    }
}
