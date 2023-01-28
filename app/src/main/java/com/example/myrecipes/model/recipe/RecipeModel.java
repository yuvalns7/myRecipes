package com.example.myrecipes.model.recipe;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeModel {
    final public static RecipeModel instance = new RecipeModel();

    final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    Retrofit retrofit;
    RecipeApi movieApi;

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
}
