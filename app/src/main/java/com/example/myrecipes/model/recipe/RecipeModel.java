package com.example.myrecipes.model.recipe;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myrecipes.model.firebase.FirebaseModel;
import com.example.myrecipes.model.localDB.AppLocalDb;
import com.example.myrecipes.model.localDB.AppLocalDbRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeModel {
    final public static RecipeModel _instance = new RecipeModel();

    private Executor executor = Executors.newSingleThreadExecutor();
    private FirebaseModel firebaseModel = new FirebaseModel();
    AppLocalDbRepository localDb = AppLocalDb.getAppDb();

    final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    Retrofit retrofit;
    RecipeApi movieApi;

    public static RecipeModel instance(){
        return _instance;
    }

    private RecipeModel(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        movieApi = retrofit.create(RecipeApi.class);
    }

    public LiveData<List<Recipe>> getRandomRecipe(){
        MutableLiveData<List<Recipe>> data = new MutableLiveData<>();
        Call<RecipeSearchResult> call = movieApi.getRandomRecipe();
        call.enqueue(new Callback<RecipeSearchResult>() {
            @Override
            public void onResponse(Call<RecipeSearchResult> call, Response<RecipeSearchResult> response) {
                if (response.isSuccessful()){
                    RecipeSearchResult res = response.body();
                    data.setValue(res.getRecipes());
                }else{
                    Log.d("TAG","----- getRandomRecipe response error");
                }
            }

            @Override
            public void onFailure(Call<RecipeSearchResult> call, Throwable t) {
                Log.d("TAG","----- getRandomRecipe fail");
            }
        });
        return data;
    }

    public enum LoadingState{
        LOADING,
        NOT_LOADING
    }
    final public MutableLiveData<LoadingState> EventStudentsListLoadingState = new MutableLiveData<LoadingState>(LoadingState.NOT_LOADING);

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
        EventStudentsListLoadingState.setValue(LoadingState.LOADING);
        // get local last update
        Long localLastUpdate = Recipe.getLocalLastUpdate();
        // get all updated recorde from firebase since local last update
        firebaseModel.getAllRecipesSince(localLastUpdate,list->{
            executor.execute(()->{
                Log.d("TAG", " firebase return : " + list.size());
                Long time = localLastUpdate;
                for(Recipe rcp:list){
                    // insert new records into ROOM
                    localDb.recipeDao().insertAll(rcp);
                    if (time < rcp.getLastUpdated()){
                        time = rcp.getLastUpdated();
                    }
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // update local last update
                Recipe.setLocalLastUpdate(time);
                EventStudentsListLoadingState.postValue(LoadingState.NOT_LOADING);
            });
        });
    }


}
