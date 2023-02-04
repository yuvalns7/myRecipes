package com.example.myrecipes;

import android.app.ProgressDialog;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myrecipes.databinding.FragmentAddEditRecipeBinding;
import com.example.myrecipes.model.recipe.Recipe;
import com.example.myrecipes.model.recipe.RecipeApiModel;
import com.example.myrecipes.model.recipe.RecipeApiReturnObj;
import com.example.myrecipes.model.recipe.RecipeModel;
import com.example.myrecipes.model.user.User;
import com.example.myrecipes.model.user.UserModel;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddEditRecipeFragment extends Fragment {

    FragmentAddEditRecipeBinding binding;
    ActivityResultLauncher<Void> cameraLauncher;
    ActivityResultLauncher<String> galleryLauncher;

    Recipe recipeParam;
    Boolean isAvatarSelected = false;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args!= null) {
            recipeParam = (Recipe) args.get("recipe");
            setLabel();
        }

        progressDialog = new ProgressDialog(getActivity());
        FragmentActivity parentActivity = getActivity();
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.removeItem(R.id.addEditRecipeFragment);
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
        binding = FragmentAddEditRecipeBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        if (recipeParam != null) {
            setEditRecipeData(recipeParam);
        }

        binding.saveBtn.setOnClickListener(view1 -> {
            String name = binding.nameEt.getText().toString();
            String category = binding.categoryEt.getText().toString();
            String instructions = binding.instructionsEt.getText().toString();
            String ingredients = binding.ingredientsEt.getText().toString();

            User user = UserModel.instance().getUserProfileDetails();
            String username = user.getName();
            String userId = user.getId();


            if (isRecipeFormValid(name, category,instructions, ingredients)) {
                Recipe rcp = new Recipe(name, category, instructions, ingredients, userId, username);
                progressDialog.setMessage("Please wait while your recipe is being added...");
                progressDialog.setTitle("Adding Recipe");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (isAvatarSelected) {
                            binding.recipeImg.setDrawingCacheEnabled(true);
                            binding.recipeImg.buildDrawingCache();
                            Bitmap bitmap = ((BitmapDrawable) binding.recipeImg.getDrawable()).getBitmap();

                            if(RecipeModel.instance().isRecipeNameExists(rcp.getName())) {

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        binding.nameEt.setError("recipe name already exists");
                                        binding.nameEt.requestFocus();
                                        openErrorToast(view, "Recipe with the same name already exist");
                                    }
                                });
                                return;
                            }
                            RecipeModel.instance().uploadImage(rcp.getName(), bitmap, url -> {
                                if (url != null) {
                                    rcp.setImgUrl(url);
                                }

                                addRecipe(view1, rcp);
                            });
                        } else {
                            addRecipe(view1, rcp);
                        }
                    }
                });


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

    private void addRecipe(View view, Recipe rcp) {
        RecipeModel.instance().addRecipe(rcp, (unused) -> {
            Navigation.findNavController(view).popBackStack();
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Recipe added successfully", Toast.LENGTH_SHORT).show();
        });
    }

    private void openErrorToast(View view, String error) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) view.findViewById(R.id.toast_layout_root));

        TextView text = layout.findViewById(R.id.text);
        text.setText(error);

        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    private void setLabel() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.navhost);
        NavDestination currentDestination = navController.getCurrentDestination();
        if (currentDestination.getId() == R.id.addEditRecipeFragment) {
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Edit recipe");
        }
    }

    private void setEditRecipeData(Recipe rcp) {
        binding.nameEt.setText(rcp.getName());
        binding.categoryEt.setText(rcp.getCategory());
        binding.instructionsEt.setText(rcp.getInstructions());
        binding.ingredientsEt.setText(rcp.getIngredients());

        if (rcp.getImgUrl() != null && rcp.getImgUrl().length() > 5) {
            Picasso.get().load(rcp.getImgUrl()).placeholder(R.drawable.chef_avatar).into(binding.recipeImg);
        }else{
            binding.recipeImg.setImageResource(R.drawable.chef_avatar);
        }

        binding.saveBtn.setText("update");
        getActivity().setTitle("edit recipe");

        binding.generateRcpBtn.setVisibility(View.GONE);
    }
}