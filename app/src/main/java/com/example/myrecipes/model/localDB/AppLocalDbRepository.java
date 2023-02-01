package com.example.myrecipes.model.localDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.myrecipes.model.recipe.Recipe;
import com.example.myrecipes.model.recipe.RecipeDao;
import com.example.myrecipes.model.user.User;

@Database(entities = {Recipe.class, User.class}, version = 10)
public abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract RecipeDao recipeDao();
}
