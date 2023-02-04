package com.example.myrecipes.model.recipe;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecipeDao {
    @Query("select * from Recipe")
    LiveData<List<Recipe>> getAll();

    @Query("SELECT COUNT(*) FROM Recipe Where userId = :userId")
    Integer countRecipeByUser(String userId);

    @Query("SELECT * FROM Recipe Where name = :name")
    Recipe findByName(String name);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Recipe... recipes);
}

