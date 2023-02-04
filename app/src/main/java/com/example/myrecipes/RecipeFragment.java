package com.example.myrecipes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myrecipes.databinding.FragmentRecipeBinding;
import com.example.myrecipes.model.recipe.Recipe;
import com.squareup.picasso.Picasso;


public class RecipeFragment extends Fragment {

    FragmentRecipeBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecipeBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        Bundle args = getArguments();
        if (args != null) {
            Recipe recipe = (Recipe) getArguments().getSerializable("RECIPE");

            if (recipe.getImgUrl() != null && recipe.getImgUrl().length() > 5) {
                Picasso.get().load(recipe.getImgUrl()).placeholder(R.drawable.chef_avatar).into(binding.recipeImage);
            }else{
                binding.recipeImage.setImageResource(R.drawable.chef_avatar);
            }
            binding.recipeName.setText(recipe.getName());
            binding.recipeCategory.setText(recipe.getCategory());
            binding.recipeInstructions.setText(recipe.getInstructions());
            binding.recipeInstructions.setText(recipe.getIngredients());

            // to do change to user name
            binding.recipeUserName.setText(recipe.getUserId());
        }
        return inflater.inflate(R.layout.fragment_recipe, container, false);
    }
}