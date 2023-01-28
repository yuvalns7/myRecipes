package com.example.myrecipes.model.recipe;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {
    @GET("/api/json/v1/1/random.php")
    Call<RecipeSearchResult> getRandomRecipe();
}
