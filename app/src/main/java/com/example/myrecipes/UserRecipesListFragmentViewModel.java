package com.example.myrecipes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myrecipes.model.recipe.Recipe;
import com.example.myrecipes.model.recipe.RecipeModel;
import com.example.myrecipes.model.user.UserModel;

import java.util.List;

public class UserRecipesListFragmentViewModel extends ViewModel {
    private LiveData<List<Recipe>> data = RecipeModel.instance().getAllUserRecipes(UserModel.instance().getUserId());

    public LiveData<List<Recipe>> getData(){
        return data;
    }
}
