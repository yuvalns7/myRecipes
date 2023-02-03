package com.example.myrecipes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.myrecipes.databinding.FragmentAddRecipeBinding;
import com.example.myrecipes.model.recipe.Recipe;
import com.example.myrecipes.model.recipe.RecipeApiModel;
import com.example.myrecipes.model.recipe.RecipeApiReturnObj;
import com.example.myrecipes.model.recipe.RecipeModel;
import com.example.myrecipes.model.user.UserModel;

import java.io.InputStream;

public class AddRecipeFragment extends Fragment {

    FragmentAddRecipeBinding binding;
    ActivityResultLauncher<Void> cameraLauncher;
    ActivityResultLauncher<String> galleryLauncher;
    Boolean isAvatarSelected = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity parentActivity = getActivity();
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.removeItem(R.id.addRecipeFragment);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        },this, Lifecycle.State.RESUMED);

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                if (result != null) {
                    binding.recipeImg.setImageBitmap(result);
                    isAvatarSelected = true;
                }
            }
        });
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null){
                    binding.recipeImg.setImageURI(result);
                    isAvatarSelected = true;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddRecipeBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        binding.saveBtn.setOnClickListener(view1 -> {
            String name = binding.nameEt.getText().toString();
            String category = binding.categoryEt.getText().toString();
            String instructions = binding.instructionsEt.getText().toString();
            String ingredients = binding.ingredientsEt.getText().toString();
            String userId = UserModel.instance().getUserProfileDetails().getId();

            if (isRecipeFormValid(name, category,instructions, ingredients)) {
                Recipe rcp = new Recipe(name, category, instructions, ingredients, userId);

                if (isAvatarSelected) {
                    binding.recipeImg.setDrawingCacheEnabled(true);
                    binding.recipeImg.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) binding.recipeImg.getDrawable()).getBitmap();
                    RecipeModel.instance().uploadImage(rcp.getName(), bitmap, url -> {
                        if (url != null) {
                            rcp.setImgUrl(url);
                        }
                        RecipeModel.instance().addRecipe(rcp, (unused) -> {
                            Navigation.findNavController(view1).popBackStack();
                        });
                    });
                } else {
                    RecipeModel.instance().addRecipe(rcp, (unused) -> {
                        Navigation.findNavController(view1).popBackStack();
                    });
                }
            }
        });

        binding.cancellBtn.setOnClickListener(view1 -> Navigation.findNavController(view1).popBackStack(R.id.userProfileFragment,false));

        binding.cameraButton.setOnClickListener(view1->{
            cameraLauncher.launch(null);
        });

        binding.galleryButton.setOnClickListener(view1->{
            galleryLauncher.launch("image/*");
        });

        binding.generateRcpBtn.setOnClickListener(view1 -> {
            LiveData<RecipeApiReturnObj> data = RecipeApiModel.instance().getRandomRecipe();
            data.observe(getViewLifecycleOwner(),recipe->{
                binding.nameEt.setText(recipe.getName());
                binding.categoryEt.setText(recipe.getCategory());
                binding.instructionsEt.setText(recipe.getInstructions());
                binding.ingredientsEt.setText(recipe.getIngredients());
                LiveData<InputStream> imgData = RecipeApiModel.instance().getImg(recipe.getImagePath());
                imgData.observe(getViewLifecycleOwner(),imgStream->{
                    isAvatarSelected = true;
                    binding.recipeImg.setImageBitmap(BitmapFactory.decodeStream(imgStream));
                });
            });

        });
        return view;
    }

    private boolean isRecipeFormValid(String name, String  category, String instructions, String ingredients) {
        boolean valid = true;

        if (name.isEmpty()) {
            binding.nameEt.setError("recipe name is required");
            binding.nameEt.requestFocus();
            valid = false;
        }
        if (category.isEmpty()) {
            binding.categoryEt.setError("recipe category is required");
            binding.categoryEt.requestFocus();
        }
        if (instructions.isEmpty()) {
            binding.instructionsEt.setError("recipe instructions is required");
            binding.instructionsEt.requestFocus();
        }
        if (ingredients.isEmpty()) {
            binding.ingredientsEt.setError("recipe ingredients is required");
            binding.ingredientsEt.requestFocus();
        }

        return valid;
    }
}