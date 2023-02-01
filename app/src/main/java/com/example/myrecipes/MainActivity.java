package com.example.myrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.example.myrecipes.model.user.UserModel;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (UserModel.instance().isUserLoggedIn()) {
            sendUserToNextActivity(HomeActivity.class);
        } else {
            sendUserToNextActivity(LoginActivity.class);
        }
    }

    private void sendUserToNextActivity(Class clazz) {
        Intent intent = new Intent(MainActivity.this,clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}