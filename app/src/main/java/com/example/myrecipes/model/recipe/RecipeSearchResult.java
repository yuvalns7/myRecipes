package com.example.myrecipes.model.recipe;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeSearchResult {
    @SerializedName("meals")
    List<RecipeApiObj> recipes;

    public List<RecipeApiObj> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<RecipeApiObj> recipesList) {
        this.recipes = recipesList;
    }
}
