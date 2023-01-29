package com.example.myrecipes;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myrecipes.databinding.FragmentUserProfileBinding;
import com.example.myrecipes.model.user.User;
import com.example.myrecipes.model.user.UserModel;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

public class UserProfileFragment extends Fragment {

    FragmentUserProfileBinding binding;
    User user;
    ActivityResultLauncher<Void> cameraLauncher;
    ActivityResultLauncher<String> galleryLauncher;
    Boolean isAvatarSelected = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                if (result != null) {
                    binding.profileImage.setImageBitmap(result);
                    isAvatarSelected = true;
                }
            }
        });
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null){
                    binding.profileImage.setImageURI(result);
                    isAvatarSelected = true;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserProfileBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        user = UserModel.instance().getUserProfileDetails();

        binding.fullName.setText(user.getName());
        binding.fullNameProfile.getEditText().setText(user.getName());

        if (user.getAvatarUrl() != null && user.getAvatarUrl().length() > 5) {
            Picasso.get().load(user.getAvatarUrl()).placeholder(R.drawable.avatar).into(binding.profileImage);
        }else{
            binding.profileImage.setImageResource(R.drawable.avatar);
        }

        binding.updateProfileBtn.setOnClickListener((view1) -> {
            String name = binding.fullNameProfile.getEditText().getText().toString();

            if (isFormValid(name)) {
                Bitmap bitmap = null;
                user.setName(name);

                if (isAvatarSelected){
                    binding.profileImage.setDrawingCacheEnabled(true);
                    binding.profileImage.buildDrawingCache();
                    bitmap = ((BitmapDrawable) binding.profileImage.getDrawable()).getBitmap();
                }

                UserModel.instance().updateUserProfile(user, bitmap,  (task) -> {
                    if (task.isSuccessful()) {
                        binding.fullName.setText(user.getName());
                        Toast.makeText(getActivity(), "update user profile Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "update user profile failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



        binding.cameraButton.setOnClickListener(view1->{
            cameraLauncher.launch(null);
        });

        binding.galleryButton.setOnClickListener(view1->{
            galleryLauncher.launch("image/*");
        });

        return view;
    }

    public boolean isFormValid(String name) {
        binding.fullNameProfile.setError(null);
        if (name.isEmpty()) {
            binding.fullNameProfile.setError("Enter full name");
            binding.fullNameProfile.requestFocus();
        }

        return  !name.isEmpty();
    }
}