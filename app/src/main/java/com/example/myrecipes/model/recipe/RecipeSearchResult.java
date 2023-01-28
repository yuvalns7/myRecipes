package com.example.myrecipes.model.recipe;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeSearchResult {
    @SerializedName("meals")
    List<Recipe> recipes;

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipesList) {
        this.recipes = recipesList;
    }
}
