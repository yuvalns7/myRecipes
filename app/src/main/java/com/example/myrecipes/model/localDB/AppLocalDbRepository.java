package com.example.myrecipes.model.localDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.myrecipes.model.recipe.Recipe;
import com.example.myrecipes.model.recipe.RecipeDao;
import com.example.myrecipes.model.user.User;
import com.example.myrecipes.model.user.UserDao;

@Database(entities = {Recipe.class, User.class}, version = 1)
public abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract RecipeDao recipeDao();

    public abstract UserDao userDao();
}
