package com.example.myrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myrecipes.model.user.User;
import com.example.myrecipes.model.user.UserModel;
import com.google.android.material.textfield.TextInputLayout;

public class UserProfileActivity extends AppCompatActivity {

    TextView fullNameTitle ;
    TextInputLayout fullNameInput, emailInput;
    Button updateBtn;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        fullNameTitle = findViewById(R.id.full_name);
        fullNameInput = findViewById(R.id.full_name_profile);
        emailInput = findViewById(R.id.email_profile);
        updateBtn = findViewById(R.id.update_profile_btn);

        user = UserModel.instance().getUserProfileDetails();

        fullNameTitle.setText(user.getName());
        fullNameInput.getEditText().setText(user.getName());
        emailInput.getEditText().setText(user.getEmail());

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = fullNameInput.getEditText().getText().toString();
                String email = emailInput.getEditText().getText().toString();

                user.setName(name);
                user.setEmail(email);

                UserModel.instance().updateUserProfile(user, null,  (task) -> {
                    if (task.isSuccessful()) {
                        fullNameTitle.setText(user.getName());
                      } else {
                    }
                });
            }
        });
    }
}