package com.example.myrecipes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myrecipes.model.recipe.Recipe;
import com.example.myrecipes.model.recipe.RecipeModel;

import java.util.List;

public class RecipesListFragmentViewModel extends ViewModel {
    private LiveData<List<Recipe>> data = RecipeModel.instance().getAllRecipes();

    public LiveData<List<Recipe>> getData(){
        return data;
    }
}
