package com.example.myrecipes.model.recipe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import androidx.navigation.Navigation;

import com.example.myrecipes.utils.StringUtils;

import java.io.IOException;
import java.net.URL;

public class RecipeApiObj {

    String idMeal;
    String strMeal;
    String strCategory;
    String strArea;
    String strInstructions;
    String strMealThumb;
    String strIngredient1;
    String strIngredient2;
    String strIngredient3;
    String strIngredient4;
    String strIngredient5;
    String strIngredient6;
    String strIngredient7;
    String strIngredient8;
    String strIngredient9;
    String strIngredient10;
    String strIngredient11;
    String strIngredient12;
    String strIngredient13;
    String strIngredient14;
    String strIngredient15;
    String strIngredient16;
    String strIngredient17;
    String strIngredient18;
    String strIngredient19;
    String strIngredient20;
    String strMeasure1;
    String strMeasure2;
    String strMeasure3;
    String strMeasure4;
    String strMeasure5;
    String strMeasure6;
    String strMeasure7;
    String strMeasure8;
    String strMeasure9;
    String strMeasure10;
    String strMeasure11;
    String strMeasure12;
    String strMeasure13;
    String strMeasure14;
    String strMeasure15;
    String strMeasure16;
    String strMeasure17;
    String strMeasure18;
    String strMeasure19;
    String strMeasure20;

    public RecipeApiObj(String idMeal, String strMeal, String strCategory, String strArea, String strInstructions, String strMealThumb, String strIngredient1, String strIngredient2, String strIngredient3, String strIngredient4, String strIngredient5, String strIngredient6, String strIngredient7, String strIngredient8, String strIngredient9, String strIngredient10, String strIngredient11, String strIngredient12, String strIngredient13, String strIngredient14, String strIngredient15, String strIngredient16, String strIngredient17, String strIngredient18, String strIngredient19, String strIngredient20, String strMeasure1, String strMeasure2, String strMeasure3, String strMeasure4, String strMeasure5, String strMeasure6, String strMeasure7, String strMeasure8, String strMeasure9, String strMeasure10, String strMeasure11, String strMeasure12, String strMeasure13, String strMeasure14, String strMeasure15, String strMeasure16, String strMeasure17, String strMeasure18, String strMeasure19, String strMeasure20) {
        this.idMeal = idMeal;
        this.strMeal = strMeal;
        this.strCategory = strCategory;
        this.strArea = strArea;
        this.strInstructions = strInstructions;
        this.strMealThumb = strMealThumb;
        this.strIngredient1 = strIngredient1;
        this.strIngredient2 = strIngredient2;
        this.strIngredient3 = strIngredient3;
        this.strIngredient4 = strIngredient4;
        this.strIngredient5 = strIngredient5;
        this.strIngredient6 = strIngredient6;
        this.strIngredient7 = strIngredient7;
        this.strIngredient8 = strIngredient8;
        this.strIngredient9 = strIngredient9;
        this.strIngredient10 = strIngredient10;
        this.strIngredient11 = strIngredient11;
        this.strIngredient12 = strIngredient12;
        this.strIngredient13 = strIngredient13;
        this.strIngredient14 = strIngredient14;
        this.strIngredient15 = strIngredient15;
        this.strIngredient16 = strIngredient16;
        this.strIngredient17 = strIngredient17;
        this.strIngredient18 = strIngredient18;
        this.strIngredient19 = strIngredient19;
        this.strIngredient20 = strIngredient20;
        this.strMeasure1 = strMeasure1;
        this.strMeasure2 = strMeasure2;
        this.strMeasure3 = strMeasure3;
        this.strMeasure4 = strMeasure4;
        this.strMeasure5 = strMeasure5;
        this.strMeasure6 = strMeasure6;
        this.strMeasure7 = strMeasure7;
        this.strMeasure8 = strMeasure8;
        this.strMeasure9 = strMeasure9;
        this.strMeasure10 = strMeasure10;
        this.strMeasure11 = strMeasure11;
        this.strMeasure12 = strMeasure12;
        this.strMeasure13 = strMeasure13;
        this.strMeasure14 = strMeasure14;
        this.strMeasure15 = strMeasure15;
        this.strMeasure16 = strMeasure16;
        this.strMeasure17 = strMeasure17;
        this.strMeasure18 = strMeasure18;
        this.strMeasure19 = strMeasure19;
        this.strMeasure20 = strMeasure20;
    }

    public RecipeApiReturnObj toRecipe() {
        RecipeApiReturnObj rcp = new RecipeApiReturnObj();
        rcp.setName(this.strMeal);
        rcp.setCategory(this.strCategory);
        rcp.setInstructions(this.strInstructions);
        rcp.setIngredients(getIngredients());
        String[] parts = this.strMealThumb.split("/");
        String lastPart = parts[parts.length - 1];
        rcp.setImagePath(lastPart);

        return rcp;
    }

    private String getIngredients() {
        String ingredients = !StringUtils.isBlank(strIngredient1) ? ""  : (strIngredient1 + " - " + strMeasure1);
        ingredients += !StringUtils.isBlank(strIngredient2) ? ""  : (", " +strIngredient2 + " - " + strMeasure2);
        ingredients += !StringUtils.isBlank(strIngredient3) ? ""  : (", " +strIngredient3 + " - " + strMeasure3);
        ingredients += !StringUtils.isBlank(strIngredient4) ? ""  : (", " +strIngredient4 + " - " + strMeasure4);
        ingredients += !StringUtils.isBlank(strIngredient5) ? ""  : (", " +strIngredient5 + " - " + strMeasure5);
        ingredients += !StringUtils.isBlank(strIngredient6) ? ""  : (", " +strIngredient6 + " - " + strMeasure6);
        ingredients += !StringUtils.isBlank(strIngredient7) ? ""  : (", " +strIngredient7 + " - " + strMeasure7);
        ingredients += !StringUtils.isBlank(strIngredient8) ? ""  : (", " +strIngredient8 + " - " + strMeasure8);
        ingredients += !StringUtils.isBlank(strIngredient9) ? ""  : (", " +strIngredient9 + " - " + strMeasure9);
        ingredients += !StringUtils.isBlank(strIngredient10) ? ""  : (", " +strIngredient10 + " - " + strMeasure10);
        ingredients += !StringUtils.isBlank(strIngredient11) ? ""  : (", " +strIngredient11 + " - " + strMeasure11);
        ingredients += !StringUtils.isBlank(strIngredient12) ? ""  : (", " +strIngredient12 + " - " + strMeasure12);
        ingredients += !StringUtils.isBlank(strIngredient13) ? ""  : (", " +strIngredient13 + " - " + strMeasure13);
        ingredients += !StringUtils.isBlank(strIngredient14) ? ""  : (", " +strIngredient14 + " - " + strMeasure14);
        ingredients += !StringUtils.isBlank(strIngredient15) ? ""  : (", " +strIngredient15 + " - " + strMeasure15);
        ingredients += !StringUtils.isBlank(strIngredient16) ? ""  : (", " +strIngredient16 + " - " + strMeasure16);
        ingredients += !StringUtils.isBlank(strIngredient17) ? ""  : (", " +strIngredient17 + " - " + strMeasure17);
        ingredients += !StringUtils.isBlank(strIngredient18) ? ""  : (", " +strIngredient18 + " - " + strMeasure18);
        ingredients += !StringUtils.isBlank(strIngredient19) ? ""  : (", " +strIngredient19 + " - " + strMeasure19);
        ingredients += !StringUtils.isBlank(strIngredient20) ? ""  : (", " +strIngredient20 + " - " + strMeasure20);
        return ingredients;
    }
}
