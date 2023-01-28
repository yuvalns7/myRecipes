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

public class RecipeApiModel {

    final public static RecipeApiModel _instance = new RecipeApiModel();

    final String BASE_URL = "https://www.themealdb.com/";
    Retrofit retrofit;
    RecipeApi recipeApi;

    private RecipeApiModel(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        recipeApi = retrofit.create(RecipeApi.class);
    }

    public LiveData<List<RecipeApiObj>> getRandomRecipe(){
        MutableLiveData<List<RecipeApiObj>> data = new MutableLiveData<>();
        Call<RecipeSearchResult> call = recipeApi.getRandomRecipe();
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
