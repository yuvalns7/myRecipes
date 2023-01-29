package com.example.myrecipes;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myrecipes.model.user.User;
import com.example.myrecipes.model.user.UserModel;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {

    TextView fullNameTitle ;
    TextInputLayout fullNameInput;
    ImageView profileImg;
    Button updateBtn;
    ImageButton cameraBtn, galleryBtn;
    User user;
    ActivityResultLauncher<Void> cameraLauncher;
    ActivityResultLauncher<String> galleryLauncher;
    Boolean isAvatarSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        fullNameTitle = findViewById(R.id.full_name);
        fullNameInput = findViewById(R.id.full_name_profile);
        updateBtn = findViewById(R.id.update_profile_btn);
        profileImg = findViewById(R.id.profile_image);
        cameraBtn = findViewById(R.id.cameraButton);
        galleryBtn = findViewById(R.id.galleryButton);

        user = UserModel.instance().getUserProfileDetails();

        fullNameTitle.setText(user.getName());
        fullNameInput.getEditText().setText(user.getName());

        if (user.getAvatarUrl() != null && user.getAvatarUrl().length() > 5) {
            Picasso.get().load(user.getAvatarUrl()).placeholder(R.drawable.avatar).into(profileImg);
        }else{
            profileImg.setImageResource(R.drawable.avatar);
        }

        updateBtn.setOnClickListener((view) -> {
            String name = fullNameInput.getEditText().getText().toString();

            if (isFormValid(name)) {
                Bitmap bitmap = null;
                user.setName(name);

                if (isAvatarSelected){
                    profileImg.setDrawingCacheEnabled(true);
                    profileImg.buildDrawingCache();
                    bitmap = ((BitmapDrawable) profileImg.getDrawable()).getBitmap();
                }

                UserModel.instance().updateUserProfile(user, bitmap,  (task) -> {
                    if (task.isSuccessful()) {
                        fullNameTitle.setText(user.getName());
                        Toast.makeText(UserProfileActivity.this, "update user profile Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UserProfileActivity.this, "update user profile failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                if (result != null) {
                    profileImg.setImageBitmap(result);
                    isAvatarSelected = true;
                }
            }
        });
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null){
                    profileImg.setImageURI(result);
                    isAvatarSelected = true;
                }
            }
        });

        cameraBtn.setOnClickListener(view1->{
            cameraLauncher.launch(null);
        });

        galleryBtn.setOnClickListener(view1->{
            galleryLauncher.launch("image/*");
        });
    }

    public boolean isFormValid(String name) {
        fullNameInput.setError(null);
        if (name.isEmpty()) {
            fullNameInput.setError("Enter full name");
            fullNameInput.requestFocus();
        }

        return  !name.isEmpty();
    }
}